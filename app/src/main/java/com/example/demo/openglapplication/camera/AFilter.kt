package com.example.demo.openglapplication.camera

import android.content.res.Resources
import android.opengl.GLES20
import android.opengl.Matrix
import android.util.Log
import java.nio.ByteBuffer
import java.nio.ByteOrder
import android.util.SparseArray
import java.nio.FloatBuffer
import java.nio.ShortBuffer
import java.util.*


abstract class AFilter(val mRes:Resources){

    private val TAG = "Filter"

    val KEY_OUT = 0x101
    val KEY_IN = 0x102
    val KEY_INDEX = 0x201

    var DEBUG = true
    /**
     * 单位矩阵
     */
    val OM = MatrixUtils.getOriginalMatrix()
    /**
     * 程序句柄
     */
    protected var mProgram: Int = 0
    /**
     * 顶点坐标句柄
     */
    protected var mHPosition: Int = 0
    /**
     * 纹理坐标句柄
     */
    protected var mHCoord: Int = 0
    /**
     * 总变换矩阵句柄
     */
    protected var mHMatrix: Int = 0
    /**
     * 默认纹理贴图句柄
     */
    protected var mHTexture: Int = 0



    /**
     * 顶点坐标Buffer
     */
    protected lateinit var mVerBuffer: FloatBuffer

    /**
     * 纹理坐标Buffer
     */
    protected lateinit var mTexBuffer: FloatBuffer

    /**
     * 索引坐标Buffer
     */
    protected var mindexBuffer: ShortBuffer? = null

    protected var mFlag = 0

     var matrix = Arrays.copyOf(OM, 16)

    var textureType = 0      //默认使用Texture2D0
    var textureId = 0
    //顶点坐标
    private val pos = floatArrayOf(-1.0f, 1.0f, -1.0f, -1.0f, 1.0f, 1.0f, 1.0f, -1.0f)

    //纹理坐标
    private val coord = floatArrayOf(0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 0.0f, 1.0f, 1.0f)

    private val mBools: SparseArray<BooleanArray> = SparseArray()
    private var mInts: SparseArray<IntArray> = SparseArray<IntArray>()


    private val mFloats: SparseArray<FloatArray> = SparseArray<FloatArray>()

    init {
        initBuffer()
    }

    private fun initBuffer() {
        val a = ByteBuffer.allocateDirect(32)
        a.order(ByteOrder.nativeOrder())
        mVerBuffer = a.asFloatBuffer()
        mVerBuffer.put(pos)
        mVerBuffer.position(0)
        val b = ByteBuffer.allocateDirect(32)
        b.order(ByteOrder.nativeOrder())
        mTexBuffer = b.asFloatBuffer()
        mTexBuffer.put(coord)
        mTexBuffer.position(0)
    }

    fun create() {
        onCreate()
    }

    abstract fun onCreate()

    fun setSize(width:Int,height:Int){
        onSizeChanged(width,height)
    }

    fun draw(){
        onClear()
        onUseProgram()
        onSetExpandData()
        onBindTexture()
        onDraw()
    }

    fun setInt(type: Int, vararg params: Int) {
        mInts.put(type, params)
    }

    fun getOutputTexture()=-1

    abstract fun onSizeChanged(width: Int, height: Int)

    fun createProgram(vertex:String,fragment:String){
        mProgram= uCreateGlProgram(vertex,fragment);
        mHPosition= GLES20.glGetAttribLocation(mProgram, "vPosition");
        mHCoord=GLES20.glGetAttribLocation(mProgram,"vCoord");
        mHMatrix=GLES20.glGetUniformLocation(mProgram,"vMatrix");
        mHTexture=GLES20.glGetUniformLocation(mProgram,"vTexture");
    }

    protected fun createProgramByAssetsFile(vertex: String, fragment: String) {
        createProgram(uRes(mRes, vertex), uRes(mRes, fragment))
    }

    protected fun onUseProgram() {
        GLES20.glUseProgram(mProgram)
    }

    protected fun onDraw() {
        GLES20.glEnableVertexAttribArray(mHPosition)
        GLES20.glVertexAttribPointer(mHPosition, 2, GLES20.GL_FLOAT, false, 0, mVerBuffer)
        GLES20.glEnableVertexAttribArray(mHCoord)
        GLES20.glVertexAttribPointer(mHCoord, 2, GLES20.GL_FLOAT, false, 0, mTexBuffer)
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4)
        GLES20.glDisableVertexAttribArray(mHPosition)
        GLES20.glDisableVertexAttribArray(mHCoord)
    }

    protected open fun onClear() {
        GLES20.glClearColor(1.0f, 1.0f, 1.0f, 1.0f)
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT or GLES20.GL_DEPTH_BUFFER_BIT)
    }

    protected open fun onSetExpandData() {
        GLES20.glUniformMatrix4fv(mHMatrix, 1, false, matrix, 0)
    }

    protected open fun onBindTexture() {
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0 + textureType)
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureId)
        GLES20.glUniform1i(mHTexture, textureType)
    }


    fun glError(code: Int, index: Any) {
        if (DEBUG && code != 0) {
            Log.e(TAG, "glError:$code---$index")
        }
    }

    //通过路径加载Assets中的文本内容
    fun uRes(mRes: Resources, path: String): String {
        val result = mRes.assets.open(path).bufferedReader().use {
            it.readText()
        }


        return result.toString().replace("\\r\\n".toRegex(), "\n")
    }

    //创建GL程序
    fun uCreateGlProgram(vertexSource: String, fragmentSource: String): Int {
        val vertex = uLoadShader(GLES20.GL_VERTEX_SHADER, vertexSource)
        if (vertex == 0) return 0
        val fragment = uLoadShader(GLES20.GL_FRAGMENT_SHADER, fragmentSource)
        if (fragment == 0) return 0
        var program = GLES20.glCreateProgram()
        if (program != 0) {
            GLES20.glAttachShader(program, vertex)
            GLES20.glAttachShader(program, fragment)
            GLES20.glLinkProgram(program)
            val linkStatus = IntArray(1)
            GLES20.glGetProgramiv(program, GLES20.GL_LINK_STATUS, linkStatus, 0)
            if (linkStatus[0] != GLES20.GL_TRUE) {
                glError(1, "Could not link program:" + GLES20.glGetProgramInfoLog(program))
                GLES20.glDeleteProgram(program)
                program = 0
            }
        }
        return program
    }

    //加载shader
    fun uLoadShader(shaderType: Int, source: String): Int {
        var shader = GLES20.glCreateShader(shaderType)
        if (0 != shader) {
            GLES20.glShaderSource(shader, source)
            GLES20.glCompileShader(shader)
            val compiled = IntArray(1)
            GLES20.glGetShaderiv(shader, GLES20.GL_COMPILE_STATUS, compiled, 0)
            if (compiled[0] == 0) {
                glError(1, "Could not compile shader:$shaderType")
                glError(1, "GLES20 Error:" + GLES20.glGetShaderInfoLog(shader))
                GLES20.glDeleteShader(shader)
                shader = 0
            }
        }
        return shader
    }


}
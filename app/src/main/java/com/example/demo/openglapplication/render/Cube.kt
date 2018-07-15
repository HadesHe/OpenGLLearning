package com.example.demo.openglapplication.render

import android.opengl.GLES20
import android.opengl.Matrix
import android.view.View
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer
import java.nio.ShortBuffer
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

class Cube(view:View):Shape(view){

    private var vertexBuffer: FloatBuffer

    private var colorBuffer: FloatBuffer


    private var mProgram: Int

    private var indexBuffer: ShortBuffer

    init {
        var bb=ByteBuffer.allocateDirect(cubePositions.size*4)
        bb.order(ByteOrder.nativeOrder())
        vertexBuffer=bb.asFloatBuffer()
        vertexBuffer.put(cubePositions)
        vertexBuffer.position(0)

        var dd=ByteBuffer.allocateDirect(color.size*4)
        dd.order(ByteOrder.nativeOrder())
        colorBuffer=dd.asFloatBuffer()
        colorBuffer.put(color)
        colorBuffer.position(0)

        var cc=ByteBuffer.allocateDirect(index.size*2)
        cc.order(ByteOrder.nativeOrder())
        indexBuffer=cc.asShortBuffer()
        indexBuffer.put(index)
        indexBuffer.position(0)

        var vertexShader=loadShader(GLES20.GL_VERTEX_SHADER, vertexShaderCode)
        var fragmentShader=loadShader(GLES20.GL_FRAGMENT_SHADER, fragmentShaderCode)

        mProgram=GLES20.glCreateProgram()
        GLES20.glAttachShader(mProgram,vertexShader)
        GLES20.glAttachShader(mProgram,fragmentShader)
        GLES20.glLinkProgram(mProgram)


    }


    private var mMatrixHandler: Int=0

    private var mPositionHandler: Int = 0

    private var mColorHandler: Int=0

    override fun onDrawFrame(gl: GL10?) {
        GLES20.glUseProgram(mProgram)

        mMatrixHandler=GLES20.glGetUniformLocation(mProgram,"vMatrix")
        GLES20.glUniformMatrix4fv(mMatrixHandler,1,false,mMVPMatrix,0)

        mPositionHandler=GLES20.glGetAttribLocation(mProgram,"vPosition")
        GLES20.glEnableVertexAttribArray(mPositionHandler)
        GLES20.glVertexAttribPointer(mPositionHandler,3,
                GLES20.GL_FLOAT,false,
                0,vertexBuffer)

        mColorHandler=GLES20.glGetAttribLocation(mProgram,"aColor")
        GLES20.glEnableVertexAttribArray(mColorHandler)
        GLES20.glVertexAttribPointer(mColorHandler,4,
                GLES20.GL_FLOAT,false,0,colorBuffer)

        GLES20.glDrawElements(GLES20.GL_TRIANGLES,
                index.size,GLES20.GL_UNSIGNED_SHORT, indexBuffer)
        GLES20.glDisableVertexAttribArray(mPositionHandler)
    }

    private val mProjectMatrix=FloatArray(16)

    private val mViewMatrix=FloatArray(16)

    private val mMVPMatrix=FloatArray(16)

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        var ratio= width.toFloat()/height
        Matrix.frustumM(mProjectMatrix,0,-ratio,ratio,-1.0f,1.0f,3.0f,20.0f)
        Matrix.setLookAtM(mViewMatrix,0,
                5.0f,5.0f,10.0f,
                0.0f,0.0f,0.0f,
                0.0f,1.0f,0.0f)
        Matrix.multiplyMM(mMVPMatrix,0
        ,mProjectMatrix,0,mViewMatrix,0)

    }

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        GLES20.glEnable(GLES20.GL_DEPTH_TEST)
    }

    companion object {
        val vertexShaderCode=
                """
                    attribute vec4 vPosition;
                    uniform mat4 vMatrix;
                    varying vec4 vColor;
                    attribute vec4 aColor;
                    void main(){
                        gl_Position=vMatrix*vPosition;
                        vColor=aColor;
                    }

                """.trimIndent()
        val fragmentShaderCode=
                """
                    precision mediump float;
                    varying vec4 vColor;
                    void main(){
                        gl_FragColor=vColor;
                    }
                """.trimIndent()
        const val COORDS_PER_VERTEX=3
        val cubePositions= floatArrayOf(
                -1.0f,1.0f,1.0f,    //正面左上0
                -1.0f,-1.0f,1.0f,   //正面左下1
                1.0f,-1.0f,1.0f,    //正面右下2
                1.0f,1.0f,1.0f,     //正面右上3
                -1.0f,1.0f,-1.0f,    //反面左上4
                -1.0f,-1.0f,-1.0f,   //反面左下5
                1.0f,-1.0f,-1.0f,    //反面右下6
                1.0f,1.0f,-1.0f    //反面右上7
        )

        val index= shortArrayOf(
                6,7,4,6,4,5,    //后面
                6,3,7,6,2,3,    //右面
                6,5,1,6,1,2,    //下面
                0,3,2,0,2,1,    //正面
                0,1,5,0,5,4,    //左面
                0,7,3,0,4,7  //上面
        )

        val color= floatArrayOf(
                0.0f,1.0f,0.0f,1.0f,
                0.0f,1.0f,0.0f,1.0f,
                0.0f,1.0f,0.0f,1.0f,
                0.0f,1.0f,0.0f,1.0f,
                1.0f,0.0f,0.0f,1.0f,
                1.0f,0.0f,0.0f,1.0f,
                1.0f,0.0f,0.0f,1.0f,
                1.0f,0.0f,0.0f,1.0f
        )

        val vertexCount= cubePositions.size/ COORDS_PER_VERTEX
        val vertexStride= COORDS_PER_VERTEX*4


    }

}
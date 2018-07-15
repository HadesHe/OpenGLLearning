package com.example.demo.openglapplication.render

import android.opengl.GLES20
import android.opengl.Matrix
import android.view.View
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

class Oval(view:View,val height:Float=0.0f) :Shape(view){

    private val shapePos:FloatArray
    private val radius:Float=1.0f
    private var vertexBuffer: FloatBuffer

    private var mProgram: Int

    init {
        shapePos=createPositions()

        var bb=ByteBuffer.allocateDirect(
                shapePos.size*4
        )
        bb.order(ByteOrder.nativeOrder())

        vertexBuffer=bb.asFloatBuffer()
        vertexBuffer.put(shapePos)
        vertexBuffer.position(0)

        var vertexShader=loadShader(GLES20.GL_VERTEX_SHADER, vertexShaderCode)
        var fragmentShader=loadShader(GLES20.GL_FRAGMENT_SHADER, fragmentShaderCode)

        mProgram=GLES20.glCreateProgram()
        GLES20.glAttachShader(mProgram,vertexShader)
        GLES20.glAttachShader(mProgram,fragmentShader)
        GLES20.glLinkProgram(mProgram)

    }

    private fun createPositions(): FloatArray {
        var data=ArrayList<Float>()

        data.add(0.0f)
        data.add(0.0f)
        data.add(height)

        val angDegSpan=360f/ n

        run {
            var i = 0f
            while (i < 360 + angDegSpan) {
                data.add((radius * Math.sin(i * Math.PI / 180f)).toFloat())
                data.add((radius * Math.cos(i * Math.PI / 180f)).toFloat())
                data.add(height)
                i += angDegSpan
            }
        }

            val f = FloatArray(data.size)
            for (i in f.indices) {
                f[i] = data[i]
            }

        return f


    }


    private var mMatrixHandler: Int=0

    private var mPositionHandler=0

    private var mColorHanlder=0

    override fun onDrawFrame(gl: GL10?) {
        GLES20.glUseProgram(mProgram)

        mMatrixHandler=GLES20.glGetUniformLocation(mProgram,"vMatrix")
        GLES20.glUniformMatrix4fv(mMatrixHandler,1,false,mMVPMatrix,0)

        mPositionHandler=GLES20.glGetAttribLocation(mProgram,"vPosition")
        GLES20.glEnableVertexAttribArray(mPositionHandler)
        GLES20.glVertexAttribPointer(mPositionHandler, COORDS_PER_VERTEX,
                GLES20.GL_FLOAT,false,
                vertexStride,vertexBuffer)

        mColorHanlder=GLES20.glGetUniformLocation(mProgram,"vColor")
        GLES20.glUniform4fv(mColorHanlder,1,color,0)
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_FAN,0,shapePos.size/3)
        GLES20.glDisableVertexAttribArray(mPositionHandler)
    }

    private val mProjectMatrix=FloatArray(16)

    private val mViewMatrix=FloatArray(16)

    val mMVPMatrix=FloatArray(16)

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        var ratio=width.toFloat()/height

        Matrix.frustumM(
                mProjectMatrix,0,-ratio,ratio,
                -1.0f,1.0f,3.0f,7.0f
        )
        Matrix.setLookAtM(mViewMatrix,0
        ,0.0f,0.0f,7.0f,
                0.0f,0.0f,0.0f,
                0.0f,1.0f,0.0f)
        Matrix.multiplyMM(mMVPMatrix,0,mProjectMatrix,0,mViewMatrix,0)
    }

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
    }


    companion object {
        private val vertexShaderCode=
                """
                    attribute vec4 vPosition;
                    uniform mat4 vMatrix;
                    void main(){
                        gl_Position=vMatrix*vPosition;
                    }
                """.trimIndent()
        private val fragmentShaderCode=
                """
                    precision mediump float;
                    uniform vec4 vColor;
                    void main(){
                        gl_FragColor=vColor;
                    }
                """.trimIndent()
        private val COORDS_PER_VERTEX=3

        private val color= floatArrayOf(1.0f,1.0f,1.0f,1.0f)

        private const val n=360

        private const val vertexStride=0

    }

}
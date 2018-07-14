package com.example.demo.openglapplication.render

import android.opengl.GLES20
import android.opengl.Matrix
import android.view.View
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

class TriangleColorFull(view:View):Shape(view){

    private var vertexBuffer: FloatBuffer

    private var colorBuffer: FloatBuffer

    private var mProgram: Int

    init {
        var bb=ByteBuffer.allocateDirect(triangleCoords.size*4)
        bb.order(ByteOrder.nativeOrder())

        vertexBuffer=bb.asFloatBuffer()
        vertexBuffer.put(triangleCoords)
        vertexBuffer.position(0)

        var dd=ByteBuffer.allocateDirect(color.size*4)
        dd.order(ByteOrder.nativeOrder())
        colorBuffer=dd.asFloatBuffer()
        colorBuffer.put(color)
        colorBuffer.position(0)

        var vertexShader=loadShader(GLES20.GL_VERTEX_SHADER, vertexShaderCode)
        var fragmentShader=loadShader(GLES20.GL_FRAGMENT_SHADER, fragmentShaderCode)

        mProgram=GLES20.glCreateProgram()
        GLES20.glAttachShader(mProgram,vertexShader)
        GLES20.glAttachShader(mProgram,fragmentShader)

        GLES20.glLinkProgram(mProgram)
    }

    private var mMatrixHandler: Int=0

    private var mPositionHandler: Int=0

    private var mColorHandler=0

    override fun onDrawFrame(gl: GL10?) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT or GLES20.GL_DEPTH_BUFFER_BIT)
        GLES20.glUseProgram(mProgram)
        mMatrixHandler=GLES20.glGetUniformLocation(mProgram,"vMatrix")
        GLES20.glUniformMatrix4fv(mMatrixHandler,1,false,mMVPMatrix,0)

        mPositionHandler=GLES20.glGetAttribLocation(mProgram,"vPosition")
        GLES20.glEnableVertexAttribArray(mPositionHandler)
        GLES20.glVertexAttribPointer(mPositionHandler, COORDS_PER_VERTEX,
                GLES20.GL_FLOAT,false,
                vertexStride,vertexBuffer)

        mColorHandler=GLES20.glGetAttribLocation(mProgram,"aColor")
        GLES20.glEnableVertexAttribArray(mColorHandler)
        GLES20.glVertexAttribPointer(mColorHandler,4,GLES20.GL_FLOAT,false,0,colorBuffer)

        GLES20.glDrawArrays(GLES20.GL_TRIANGLES,0, vertexCount)
        GLES20.glDisableVertexAttribArray(mPositionHandler)

    }

    private val mProjectMatrix=FloatArray(16)

    private val mViewMatrix=FloatArray(16)

    private val mMVPMatrix=FloatArray(16)

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        var ratio=width.toFloat()/height
        Matrix.frustumM(mProjectMatrix,0,-ratio,ratio,-1.0f,1.0f,3.0f,7.0f)
        Matrix.setLookAtM(mViewMatrix,0,0.0f,0.0f,7.0f,0.0f,0.0f,0.0f,0.0f,1.0f,0.0f)
        Matrix.multiplyMM(mMVPMatrix,0,mProjectMatrix,0,mViewMatrix,0)
    }

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
    }

    companion object {
        val triangleCoords= floatArrayOf(
                0.5f,0.5f,0.0f,
                -0.5f,-0.5f,0.0f,
                0.5f,-0.5f,0.0f
        )

        val color= floatArrayOf(
                0.0f,1.0f,0.0f,1.0f,
                1.0f,0.0f,0.0f,1.0f,
                0.0f,0.0f,1.0f,1.0f
        )

        const val COORDS_PER_VERTEX=3
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
                """
        val fragmentShaderCode=
                """
                    precision mediump float;
                    varying vec4 vColor;
                    void main(){
                      gl_FragColor=vColor;
                    }
                """
        val vertexCount= triangleCoords.size/ COORDS_PER_VERTEX
        val vertexStride= COORDS_PER_VERTEX*4
    }

}
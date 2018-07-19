package com.example.demo.openglapplication.render

import android.opengl.GLES20
import android.view.View
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

/**
 * 绘制三角形
 */
class Triangle(mView: View) : Shape(mView) {

    private var vertexBuffer: FloatBuffer

    private var mProgram: Int

    init {
        var bb = ByteBuffer.allocateDirect(triangleCoords.size*4)
        bb.order(ByteOrder.nativeOrder())

        vertexBuffer=bb.asFloatBuffer()
        vertexBuffer.put(triangleCoords)
        vertexBuffer.position(0)

        var vertexShader=loadShader(GLES20.GL_VERTEX_SHADER,vertexShaderCode)
        var fragmentShader=loadShader(GLES20.GL_FRAGMENT_SHADER,fragmentShaderCode)

        mProgram=GLES20.glCreateProgram();
        GLES20.glAttachShader(mProgram,vertexShader)
        GLES20.glAttachShader(mProgram,fragmentShader)

        GLES20.glLinkProgram(mProgram)



    }

    private var mPositionHandler: Int = 0

    private val vertextStride: Int = COORD_PER_VERTEX * 4

    private var mColorHandler: Int=0


    override fun onDrawFrame(gl: GL10) {

        GLES20.glUseProgram(mProgram)
        mPositionHandler=GLES20.glGetAttribLocation(mProgram,"vPosition")

        GLES20.glEnableVertexAttribArray(mPositionHandler)
        GLES20.glVertexAttribPointer(mPositionHandler, COORD_PER_VERTEX,
                GLES20.GL_FLOAT,false, vertextStride,vertexBuffer)

        mColorHandler=GLES20.glGetUniformLocation(mProgram,"vColor")
        GLES20.glUniform4fv(mColorHandler,1,color,0)
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES,0, vertextCount)
        GLES20.glDisableVertexAttribArray(mPositionHandler)
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
    }

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
    }


    companion object {
        val COORD_PER_VERTEX = 3

        //顶点坐标
        val triangleCoords = floatArrayOf(
                0.5f, 0.5f, 0.0f,
                -0.5f, -0.5f, 0.0f,
                0.5f, -0.5f, 0.0f
        )

        //颜色
        val color= floatArrayOf(1.0f,1.0f,1.0f,1.0f)

        private val vertextCount = triangleCoords.size / COORD_PER_VERTEX

        private val vertexShaderCode="attribute vec4 vPosition;"+
                "void main(){"+
                " gl_Position= vPosition;"+
                "}"
        private val fragmentShaderCode="precision mediump float;"+
                "uniform vec4 vColor;"+
                "void main(){"+
                " gl_FragColor=vColor;"+
                "}"
    }

}
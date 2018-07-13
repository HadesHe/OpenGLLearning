package com.example.demo.openglapplication.render

import android.opengl.GLES20
import android.opengl.Matrix
import android.view.View
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

class TriangleWithCamera(view: View) : Shape(view) {

    private var vertexBuffer: FloatBuffer

    private var mProgram: Int

    init {
        var bb=ByteBuffer.allocateDirect(triangleCoords.size*4)
        bb.order(ByteOrder.nativeOrder())

        vertexBuffer=bb.asFloatBuffer()
        vertexBuffer.put(triangleCoords)
        vertexBuffer.position(0)

        var vertexShader=loadShader(GLES20.GL_VERTEX_SHADER, vertexShaderCode)
        var fragmentShader=loadShader(GLES20.GL_FRAGMENT_SHADER, fragmentShaderCode)

        mProgram=GLES20.glCreateProgram()
        GLES20.glAttachShader(mProgram,vertexShader)
        GLES20.glAttachShader(mProgram,fragmentShader)
        GLES20.glLinkProgram(mProgram)

    }

    private var mMatrixHandler: Int=0

    private var mPositionHandle: Int=0

    private var mColorHandle: Int=0

    override fun onDrawFrame(gl: GL10?) {

        GLES20.glUseProgram(mProgram)
        mMatrixHandler=GLES20.glGetUniformLocation(mProgram,"vMatrix")

        GLES20.glUniformMatrix4fv(mMatrixHandler,1,false,mMVPMatrix,0)
        mPositionHandle=GLES20.glGetAttribLocation(mProgram,"vPosition")
        GLES20.glEnableVertexAttribArray(mPositionHandle)
        GLES20.glVertexAttribPointer(mPositionHandle,COORD_PER_VERTEX,GLES20.GL_FLOAT,false,
                vertexStride,vertexBuffer)
        mColorHandle=GLES20.glGetUniformLocation(mProgram,"vColor")

        GLES20.glUniform4fv(mColorHandle,1, color,0)
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES,0,vertexCount)
        GLES20.glDisableVertexAttribArray(mPositionHandle)
    }

    private val mProjectMatrix: FloatArray = FloatArray(16)

    private val mViewMatrix: FloatArray = FloatArray(16)

    private val mMVPMatrix= FloatArray(16)

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        //计算宽高比
        var ratio:Float = (width.toFloat()  / height)
        //设置透视投影
        Matrix.frustumM(mProjectMatrix, 0, -ratio, ratio, -1f, 1f, 3f, 7f)
        //设置相机位置
        Matrix.setLookAtM(mViewMatrix, 0, 0f, 0f, 7.0f, 0f, 0f, 0f, 0f, 1.0f, 0.0f)
        //计算变换矩阵
        Matrix.multiplyMM(mMVPMatrix, 0, mProjectMatrix, 0, mViewMatrix, 0)
    }

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
    }

    companion object {
        val vertexShaderCode=
                "attribute vec4 vPosition;"+
                        "uniform mat4 vMatrix;"+
                        "void main(){"+
                        " gl_Position = vMatrix*vPosition;"+
                        "}"
        val fragmentShaderCode =
                "precision mediump float;"+
                        "uniform vec4 vColor;"+
                        "void main() {"+
                        " gl_FragColor = vColor;"+
                        "}"
        val triangleCoords= floatArrayOf(
                0.5f,0.5f,0.0f,
                -0.5f,-0.5f,0.0f,
                0.5f,-0.5f,0.0f
        )

        val color= floatArrayOf(1.0f,1.0f,1.0f,1.0f)

        const val COORD_PER_VERTEX=3

        const val vertexStride= COORD_PER_VERTEX*4

        var vertexCount= triangleCoords.size/ COORD_PER_VERTEX
    }

}

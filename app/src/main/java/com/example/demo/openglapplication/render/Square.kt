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

class Square(view:View):Shape(view){

    private var vertexBuffer: FloatBuffer

    private var indexBuffer: ShortBuffer

    private var mProgram: Int

    init {
        var bb=ByteBuffer.allocateDirect(triangleCoords.size*4)
        bb.order(ByteOrder.nativeOrder())
        vertexBuffer=bb.asFloatBuffer()
        vertexBuffer.put(triangleCoords)
        vertexBuffer.position(0)

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

    override fun onDrawFrame(gl: GL10?) {

    }

    private val mProjectMatrix=FloatArray(16)

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        var ratio=width.toFloat()/height
        Matrix.frustumM(mProjectMatrix,0,-ratio,ratio,-1.0f,1.0f,3.0f,7.0f)
        Matrix.setLookAtM(mViewMatrix,0,
                0.0f,0.0f,7.0f,

                )
    }

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
    }


    companion object {
        private val vertexShaderCode =
                """
                    attribute vec4 vPosition;
                    uniform mat4 vMatrix;
                    void main(){
                        gl_Position=vMatrix*vPosition;
                    }
                """.trimIndent()
        private val fragmentShaderCode =
                """
                    precision mediump float;
                    uniform vec4 vColor;
                    void main(){
                        gl_FragColor=vColor;
                    }
                """.trimIndent()
        private val COORD_PER_VERTEX=3
        private val triangleCoords= floatArrayOf(
                -0.5f,0.5f,0.0f,
                -0.5f,-0.5f,0.0f,
                0.5f,-0.5f,0.0f,
                0.5f,0.5f,0.0f
        )

        private val index= shortArrayOf(
                0,1,2,0,2,3
        )

        private val vertexCount= triangleCoords.size/ COORD_PER_VERTEX
        private val vertexStride= COORD_PER_VERTEX*4
        private val color= floatArrayOf(1.0f,1.0f,1.0f,1.0f)
    }

}
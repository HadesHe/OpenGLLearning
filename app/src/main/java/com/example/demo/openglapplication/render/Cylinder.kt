package com.example.demo.openglapplication.render

import android.view.View
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10
import android.graphics.Shader
import android.opengl.GLES20
import android.opengl.Matrix
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer


class Cylinder(val view:View):Shape(view) {

    private var ovalBottom: Oval

    private val height=2.0f

    private var ovalTop: Oval

    private val n=360

    private var vSize: Int

    private var vertexBuffer: FloatBuffer

    private val radius=1.0f

    init {
        ovalBottom=Oval(view)
        ovalTop=Oval(view,height)
        val pos = ArrayList<Float>()
        val angDegSpan = 360f / n
        var i = 0f
        while (i < 360 + angDegSpan) {
            pos.add((radius * Math.sin(i * Math.PI / 180f)).toFloat())
            pos.add((radius * Math.cos(i * Math.PI / 180f)).toFloat())
            pos.add(height)
            pos.add((radius * Math.sin(i * Math.PI / 180f)).toFloat())
            pos.add((radius * Math.cos(i * Math.PI / 180f)).toFloat())
            pos.add(0.0f)
            i += angDegSpan
        }

        var d=FloatArray(pos.size)
        for (i in 0 until d.size) {
            d[i] = pos[i]
        }

        vSize=d.size/3
        var buffer=ByteBuffer.allocateDirect(d.size*4)
        buffer.order(ByteOrder.nativeOrder())
        vertexBuffer=buffer.asFloatBuffer()
        vertexBuffer.put(d)
        vertexBuffer.position(0)

    }
    override fun onDrawFrame(gl: GL10?) {
        GLES20.glUseProgram(mProgram)
        var mMatrix=GLES20.glGetUniformLocation(mProgram,"vMatrix")
        GLES20.glUniformMatrix4fv(mMatrix,1,false,mMVPMatrix,0)

        var mPositionHander=GLES20.glGetAttribLocation(mProgram,"vPosition")
        GLES20.glEnableVertexAttribArray(mPositionHander)
        GLES20.glVertexAttribPointer(mPositionHander,3,GLES20.GL_FLOAT,false
                ,0,vertexBuffer)
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP,0,vSize)
        GLES20.glDisableVertexAttribArray(mPositionHander)
        ovalBottom.mMVPMatrix=mMVPMatrix
        ovalBottom.onDrawFrame(gl)
        ovalTop.mMVPMatrix=mMVPMatrix
        ovalTop.onDrawFrame(gl)
    }

    private val mProjectMatrix=FloatArray(16)

    private var mViewMatrix=FloatArray(16)

    private val mMVPMatrix=FloatArray(16)

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        var ratio=width.toFloat()/height
        Matrix.frustumM(mProjectMatrix,0,-ratio,ratio,-1.0f,1.0f,3.0f,20.0f)
        Matrix.setLookAtM(mViewMatrix,0,
                1.0f,-10.0f,-4.0f,
                0.0f,0.0f,0.0f,
                0.0f,1.0f,0.0f)
        Matrix.multiplyMM(mMVPMatrix,0,
                mProjectMatrix,0,
                mViewMatrix,0)
    }

    private var mProgram: Int = 0

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        GLES20.glEnable(GLES20.GL_DEPTH_TEST)
        mProgram=ShaderEx.createProgram(view.resources,"vshader/Cone.sh","fshader/Cone.sh")
        ovalBottom.onSurfaceCreated(gl,config)
        ovalTop.onSurfaceCreated(gl,config)
    }
}
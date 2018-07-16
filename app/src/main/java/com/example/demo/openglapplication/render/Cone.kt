package com.example.demo.openglapplication.render

import android.opengl.GLES20
import android.opengl.Matrix
import android.util.Log
import android.view.View
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10



class Cone(view:View):Shape(view){

    private var oval: Oval

    private val height=1.0f
    private val radius=1.0f
    private val n=360

    private var vSize: Int

    private var vertexBuffer: FloatBuffer

    init {
        oval=Oval(view)

        val pos = ArrayList<Float>()
        pos.add(0.0f)
        pos.add(0.0f)
        pos.add(height)
        val angDegSpan = 360f / n
        run {
            var i = 0f
            while (i < 360 + angDegSpan) {
                pos.add((radius * Math.sin(i * Math.PI / 180f)).toFloat())
                pos.add((radius * Math.cos(i * Math.PI / 180f)).toFloat())
                pos.add(0.0f)
                i += angDegSpan
            }
        }
        val d = FloatArray(pos.size)
        for (i in d.indices) {
            d[i] = pos.get(i)
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
        Log.e(TAG,"mProgram:$mProgram")
        var mMatrix=GLES20.glGetUniformLocation(mProgram,"vMatrix")
        GLES20.glUniformMatrix4fv(mMatrix,1,false,mMVPMatrix,0)

        var mPositionHandle=GLES20.glGetAttribLocation(mProgram,"vPosition")
        Log.e(TAG,"Get position:$mPositionHandle")
        GLES20.glEnableVertexAttribArray(mPositionHandle)
        GLES20.glVertexAttribPointer(mPositionHandle,3,GLES20.GL_FLOAT,false,0,vertexBuffer)

        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_FAN,0,vSize)
        GLES20.glDisableVertexAttribArray(mPositionHandle)
        oval.mMVPMatrix=mMVPMatrix
        oval.onDrawFrame(gl)

    }

    private val mProjectMatrix=FloatArray(16)

    private val mViewMatrix=FloatArray(16)

    private val mMVPMatrix=FloatArray(16)

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        var ratio=width.toFloat()/height
        Matrix.frustumM(mProjectMatrix,0,-ratio,ratio,-1.0f,1.0f,3.0f,20.0f)
        Matrix.setLookAtM(mViewMatrix,0,
                1.0f,-10.0f,-4.0f,
                0.0f,0.0f,0.0f,
                0.0f,1.0f,0.0f)
        Matrix.multiplyMM(mMVPMatrix,0,mProjectMatrix,0,mViewMatrix,0)

    }


    private var mProgram: Int=0

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        GLES20.glEnable(GLES20.GL_DEPTH_TEST)
        mProgram=ShaderEx.createProgram(mView.resources,"vshader/Cone.sh","fshader/Cone.sh")
        oval.onSurfaceCreated(gl,config)
    }

    companion object {
        private val TAG=Cone::class.java.simpleName
    }


}
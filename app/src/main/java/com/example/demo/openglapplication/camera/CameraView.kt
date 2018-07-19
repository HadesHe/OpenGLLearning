package com.example.demo.openglapplication.camera

import android.content.Context
import android.opengl.GLSurfaceView
import android.util.AttributeSet
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

class CameraView(context: Context,attrs:AttributeSet?=null): GLSurfaceView(context,attrs),GLSurfaceView.Renderer{

    init {
        setEGLContextClientVersion(2)
        setRenderer(this)
        renderMode= RENDERMODE_WHEN_DIRTY

        mCamera2=KitkatCamera()
        mCameraDrawer=CameraDrawer(resources)
    }


    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onDrawFrame(gl: GL10?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}
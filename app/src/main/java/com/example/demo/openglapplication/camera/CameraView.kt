package com.example.demo.openglapplication.camera

import android.content.Context
import android.graphics.SurfaceTexture
import android.opengl.GLES20
import android.opengl.GLSurfaceView
import android.util.AttributeSet
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

class CameraView(context: Context,attrs:AttributeSet?=null): GLSurfaceView(context,attrs),GLSurfaceView.Renderer{

    private var mCamera2: KitkatCamera

    private var cameraId=1


    private  var mRunnable: Runnable?=null

    private var mCameraDrawer: CameraDrawer

    init {
        setEGLContextClientVersion(2)
        setRenderer(this)
        renderMode= RENDERMODE_WHEN_DIRTY

        mCamera2=KitkatCamera()
        mCameraDrawer=CameraDrawer(resources)
    }

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        mCameraDrawer.onSurfaceCreated(gl,config)
        if (mRunnable != null) {
            mRunnable!!.run()
            mRunnable = null
        }

        mCamera2.open(cameraId)
        mCameraDrawer.setCameraId(cameraId)
        val point=mCamera2.getPreviewSize()
        mCameraDrawer.setDataSize(point.x,point.y)
        mCamera2.setPreviewTexture(mCameraDrawer.getSurfaceTexture())
        mCameraDrawer.getSurfaceTexture().setOnFrameAvailableListener(object : SurfaceTexture.OnFrameAvailableListener {
            override fun onFrameAvailable(surfaceTexture: SurfaceTexture?) {
                requestRender()
            }
        })
        mCamera2.preview()
    }

    fun switchCamera(){
        mRunnable=object:Runnable{
            override fun run() {
                mCamera2.close()
                cameraId=if(cameraId==1)0 else 1
            }

        }
        onPause()
        onResume()
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        mCameraDrawer.setViewSize(width,height)
        GLES20.glViewport(0,0,width,height)
    }

    override fun onDrawFrame(gl: GL10?) {
        mCameraDrawer.onDrawFrame(gl)
    }

    override fun onPause() {
        super.onPause()
        mCamera2.close()
    }

    private var lastChange: Boolean = false

    fun changeMatrix() {
        mCameraDrawer.calculateMatrix(lastChange)
        lastChange=lastChange.not()
    }

}
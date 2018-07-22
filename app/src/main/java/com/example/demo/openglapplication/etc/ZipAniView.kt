package com.example.demo.openglapplication.etc

import android.content.Context
import android.graphics.PixelFormat
import android.opengl.GLSurfaceView
import android.util.AttributeSet
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

class ZipAniView(context: Context,attributeSet: AttributeSet?=null ):GLSurfaceView(context,attributeSet),GLSurfaceView.Renderer{

    init {
        setEGLContextClientVersion(2)
        setZOrderOnTop(true)
        holder.setFormat(PixelFormat.TRANSLUCENT)
        setEGLConfigChooser(8,8,8,8,16,0)
        setRenderer(this)
        renderMode= RENDERMODE_WHEN_DIRTY
        mDrawer=ZipMulDrawer(resources)
    }

    fun setScaleType(type:Int){
        mDrawer!!.setInt(ZipMulDrawer.TYPE,type)
    }

    fun setAnimation(path:String,timeStep:Int){
        mDrawer.setAnimation(this,path,timeStep)
    }

    fun start(){
        mDrawer.start()
    }

    fun stop(){
        mDrawer.stop()
    }

    fun isPlay():Boolean{
        return mDrawer.isPlay()
    }

    fun setStateChangeListener(listener:StateChangeListener){
        mDrawer.setStateChangeListener(listener)

    }
    override fun onDrawFrame(gl: GL10?) {
        mDrawer.draw()
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
       mDrawer.onSizeChanged(width,height)
    }

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        mDrawer.create()
    }

}
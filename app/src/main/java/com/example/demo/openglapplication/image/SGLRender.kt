package com.example.demo.openglapplication.image

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.opengl.GLSurfaceView
import android.view.View
import com.example.demo.openglapplication.filter.AFilter
import com.example.demo.openglapplication.filter.ColorFilter
import com.example.demo.openglapplication.filter.ContrastColorFilter
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

class SGLRender(val view: View) : GLSurfaceView.Renderer {
    var image: Bitmap=BitmapFactory.decodeStream(view.getResources().getAssets().open("texture/fengj.png"))
        set(value) {
        field=value
        mFilter.mBitmap=value
    }


    private var refreshFlag=false

    var mFilter: AFilter


    private lateinit var config: EGLConfig


    private var width=0

    private var height=0

    init {
        mFilter=ContrastColorFilter(view.context,ColorFilter.Filter.NONE)
    }


    fun setFilter(filter:AFilter){
            refreshFlag=true
            mFilter=filter
            if (image != null) {
                mFilter.mBitmap=image
            }
    }

    fun refresh(){
        refreshFlag=true
    }

    fun getFilter(): AFilter {
        return mFilter
    }

    fun setImageBuffer(buffer:IntArray,width: Int,height: Int){
        image= Bitmap.createBitmap(buffer,width,height,Bitmap.Config.RGB_565)
        mFilter.mBitmap=image
    }

    override fun onDrawFrame(gl: GL10?) {

        if(refreshFlag&&width != 0&& height != 0){
            mFilter.onSurfaceCreated(gl,config)
            mFilter.onSurfaceChanged(gl,width,height)
            refreshFlag=false
        }
        mFilter.onDrawFrame(gl)
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        this.width=width
        this.height=height
        mFilter.onSurfaceChanged(gl,width,height)
    }

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig) {
        this.config=config
        mFilter.onSurfaceCreated(gl,config)
    }
}
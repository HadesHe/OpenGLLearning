package com.example.demo.openglapplication.render

import android.content.Context
import android.opengl.GLSurfaceView
import android.util.AttributeSet

class FGLView(context:Context,attrs:AttributeSet?=null) :GLSurfaceView(context,attrs){

    private var renderer: FGLRender


    init {
        setEGLContextClientVersion(2)
        renderer=FGLRender(this)
        setRenderer(renderer)
        renderMode=GLSurfaceView.RENDERMODE_WHEN_DIRTY
    }

//    fun setShape(string:String){
//        renderer.setShape(string)
//    }

}
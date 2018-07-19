package com.example.demo.openglapplication.image

import android.content.Context
import android.graphics.BitmapFactory
import android.opengl.GLSurfaceView
import android.util.AttributeSet
import com.example.demo.openglapplication.filter.AFilter

class SGLView(context: Context,attributeSet: AttributeSet?=null):GLSurfaceView(context,attributeSet){

    val render: SGLRender

    init {
        setEGLContextClientVersion(2)
        render=SGLRender(this)
        setRenderer(render)
        renderMode= RENDERMODE_WHEN_DIRTY

        render.image=BitmapFactory.decodeStream(resources.assets.open("texture/fengj.png"))
        requestRender()
    }

    fun setFilter(filter:AFilter){
        render.setFilter(filter)
    }



}
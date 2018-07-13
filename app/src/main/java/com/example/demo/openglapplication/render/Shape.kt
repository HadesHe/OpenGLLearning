package com.example.demo.openglapplication.render

import android.opengl.GLES20
import android.opengl.GLSurfaceView
import android.view.View

abstract class Shape(val mView:View):GLSurfaceView.Renderer{

    fun loadShader(type:Int,shaderCode:String): Int {
        var shader=GLES20.glCreateShader(type)
        GLES20.glShaderSource(shader,shaderCode)
        GLES20.glCompileShader(shader)
        return shader
    }

}
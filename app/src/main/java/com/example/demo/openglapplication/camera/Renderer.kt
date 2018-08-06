package com.example.demo.openglapplication.camera

import android.opengl.GLSurfaceView

interface Renderer:GLSurfaceView.Renderer {

    fun onDestroy()
}

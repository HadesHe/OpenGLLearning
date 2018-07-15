package com.example.demo.openglapplication.render

import android.opengl.GLES20
import android.view.View
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

class FGLRender(val view:View):Shape(view) {
    override fun onDrawFrame(gl: GL10) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT or GLES20.GL_DEPTH_BUFFER_BIT)
        shape.onDrawFrame(gl)
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        GLES20.glViewport(0,0,width,height)
        shape.onSurfaceChanged(gl,width,height)
    }

    private lateinit var shape: Shape

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        GLES20.glClearColor(0.5f,0.5f,0.5f,0.5f)

        shape=Square(view)
        shape.onSurfaceCreated(gl,config)

    }

}

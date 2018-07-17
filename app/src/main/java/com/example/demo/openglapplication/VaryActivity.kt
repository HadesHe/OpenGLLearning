package com.example.demo.openglapplication

import android.opengl.GLSurfaceView
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.example.demo.openglapplication.vary.VaryRender
import kotlinx.android.synthetic.main.activity_vary.*

class VaryActivity:AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vary)

        initGL()
    }

    private fun initGL() {
        with(mGLView){
            setEGLContextClientVersion(2)
            setRenderer(VaryRender(resources))
            renderMode=GLSurfaceView.RENDERMODE_WHEN_DIRTY

        }
    }

}

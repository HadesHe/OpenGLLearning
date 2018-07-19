package com.example.demo.openglapplication

import android.opengl.GLSurfaceView
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.example.demo.openglapplication.vary.VaryRender
import kotlinx.android.synthetic.main.activity_vary.*

class VaryActivity:AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vary)

        initGL()
    }

    private lateinit var render: VaryRender

    private fun initGL() {
        with(mGLView){
            setEGLContextClientVersion(2)
            render=VaryRender(resources)
            setRenderer(render)
            renderMode=GLSurfaceView.RENDERMODE_WHEN_DIRTY

            mGLView.setOnClickListener {
                render.setX(3.0f)

                Log.d("x","x=====$x")
                mGLView.requestRender()
            }

        }
    }

}

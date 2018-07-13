package com.example.demo.openglapplication.render

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.example.demo.openglapplication.R
import kotlinx.android.synthetic.main.activity_fglview.*

class FGLViewActivity:AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fglview)
    }

    override fun onResume() {
        super.onResume()
        mGLView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mGLView.onPause()
    }


}

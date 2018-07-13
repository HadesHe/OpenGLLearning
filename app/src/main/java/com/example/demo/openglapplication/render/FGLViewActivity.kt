package com.example.demo.openglapplication.render

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.example.demo.openglapplication.R
import kotlinx.android.synthetic.main.activity_fglview.*

class FGLViewActivity:AppCompatActivity(), View.OnClickListener {
    override fun onClick(v: View) {
        when(v.id){
            R.id.btnTri->{

//                mGLView.setShape("三角形")

            }

            R.id.btnTriWithCamera->{
//                mGLView.setShape("正三角形")
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fglview)


        btnTri.setOnClickListener(this)
        btnTriWithCamera.setOnClickListener(this)
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

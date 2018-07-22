package com.example.demo.openglapplication.etc

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.example.demo.openglapplication.R
import com.example.demo.openglapplication.camera.Gl2Utils

class ZipActivity:AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_zip)


        mAnimView.scaleType=Gl2Utils.TYPE_CENTERINSIDE
        mAnimView.setOnClickListener{
            if(mAnimView.isPlay().not){
                mAnimView.setAnimation(nowMenu,50)
                mAnimView.start()
            }
        }

        TODO("stateChangeListener")
//        mAnimView.setStateChangeListener{
//
//        }
    }
}

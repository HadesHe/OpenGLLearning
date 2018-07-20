package com.example.demo.openglapplication.camera

import android.Manifest
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.example.demo.openglapplication.R
import kotlinx.android.synthetic.main.activity_camera.*

class CameraActivity:AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera)
    }


    override fun onResume() {
        super.onResume()
        mCameraView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mCameraView.onPause()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menu.add("切换摄像头").setTitle("切换摄像头").setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        val name=item?.title.toString()
        if (name.equals("切换摄像头")) {
            mCameraView.switchCamera()
        }
        return super.onOptionsItemSelected(item)
    }
}

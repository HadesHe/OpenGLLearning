package com.example.demo.openglapplication.camera

import android.Manifest
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.example.demo.openglapplication.R

class CameraActivity:AppCompatActivity() {

    private val initViewRunnable= object:Runnable{
        override fun run() {
            setContentView(R.layout.activity_camera)
        }

    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        PermissionEx.askPermission(this@CameraActivity, arrayOf(Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE),10,initViewRunnable)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        PermissionEx.onRequestPermissionsResult(requestCode==10,grantResults,initViewRunnable,
                object:Runnable{
                    override fun run() {
                        Toast.makeText(this@CameraActivity,"没有权限",Toast.LENGTH_SHORT).show()
                        finish()
                    }

                })
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

package com.example.demo.openglapplication.camera

import android.Manifest
import android.graphics.Camera
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.Toast
import com.example.demo.openglapplication.R
import kotlinx.android.synthetic.main.activity_camera.*

class CameraActivity:AppCompatActivity(), View.OnClickListener {
    override fun onClick(v: View) {
        when(v.id){
            R.id.btnChangeMatrix->{
                cameraView.changeMatrix()
            }

        }
    }

    private lateinit var cameraView: CameraView
    private lateinit var changeMatrix: Button
    private val initViewRunnable = Runnable {
        setContentView(R.layout.activity_camera)
        cameraView=findViewById(R.id.mCameraView)
        changeMatrix=findViewById<Button>(R.id.btnChangeMatrix)
        changeMatrix.setOnClickListener(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        PermissionEx.askPermission(this, arrayOf(Manifest.permission.CAMERA, Manifest
                .permission.WRITE_EXTERNAL_STORAGE), 10, initViewRunnable)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        PermissionEx.onRequestPermissionsResult(requestCode == 10, grantResults, initViewRunnable,
                Runnable {
                    Toast.makeText(this@CameraActivity, "没有获得必要的权限", Toast.LENGTH_SHORT).show()
                    finish()
                })

    }


    override fun onResume() {
        super.onResume()
        cameraView.onResume()
    }

    override fun onPause() {
        super.onPause()
        cameraView.onPause()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menu.add("切换摄像头").setTitle("切换摄像头").setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        val name=item?.title.toString()
        if (name.equals("切换摄像头")) {
            cameraView.switchCamera()
        }
        return super.onOptionsItemSelected(item)
    }
}

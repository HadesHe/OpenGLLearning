package com.example.demo.openglapplication

import android.content.Intent
import android.graphics.Camera
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import com.example.demo.openglapplication.camera.CameraActivity
import com.example.demo.openglapplication.etc.ZipActivity
import com.example.demo.openglapplication.render.FGLViewActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), View.OnClickListener {
    override fun onClick(v: View) {
        when(v.id){
            R.id.btnFGLView->{
                startActivity(Intent(this@MainActivity, FGLViewActivity::class.java))
            }
            R.id.btnVary -> {
                startActivity(Intent(this@MainActivity,VaryActivity::class.java))
            }

            R.id.btnSGL->{
                startActivity(Intent(this@MainActivity,SGLViewActivity::class.java))
            }
            R.id.btnCamera -> {
                startActivity(Intent(this@MainActivity, CameraActivity::class.java))
            }
            R.id.btnCamera2 ->{
                startActivity(Intent(this@MainActivity,Camera2Activity::class.java))
            }
            R.id.btnCamera3 ->{
                startActivity(Intent(this@MainActivity,Camera3Activity::class.java))
            }
            R.id.btnZip ->{
                startActivity(Intent(this@MainActivity, ZipActivity::class.java))
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        btnFGLView.setOnClickListener(this)
        btnVary.setOnClickListener(this)
        btnSGL.setOnClickListener(this)
        btnCamera.setOnClickListener(this)
        btnCamera2.setOnClickListener(this)
        btnCamera3.setOnClickListener(this)
        btnZip.setOnClickListener(this)

    }
}

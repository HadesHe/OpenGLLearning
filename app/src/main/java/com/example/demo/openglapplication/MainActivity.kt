package com.example.demo.openglapplication

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
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
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        btnFGLView.setOnClickListener(this)

    }
}

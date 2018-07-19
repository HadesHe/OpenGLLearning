package com.example.demo.openglapplication

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.ContextMenu
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.example.demo.openglapplication.filter.ColorFilter
import com.example.demo.openglapplication.filter.ContrastColorFilter
import kotlinx.android.synthetic.main.activity_picture.*

class SGLViewActivity:AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_picture)
    }

    override fun onResume() {
        super.onResume()
        glView.onResume()
    }

    override fun onPause() {
        super.onPause()
        glView.onPause()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_filter,menu)
        return super.onCreateOptionsMenu(menu)
    }

    private var isHalf=false

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.mDeal->{
                isHalf=isHalf.not()
                if(isHalf){
                    item.title="处理一半"
                }else{
                    item.title="全部处理"
                }

                glView.render.refresh()
            }
            R.id.mDefault->{
                glView.setFilter(ContrastColorFilter(this,ColorFilter.Filter.NONE))
            }
            R.id.mGray -> {
                glView.setFilter(ContrastColorFilter(this,ColorFilter.Filter.GRAY))
            }
            R.id.mCool ->{
                glView.setFilter(ContrastColorFilter(this,ColorFilter.Filter.COOL))
            }
            R.id.mWarm ->{
                glView.setFilter(ContrastColorFilter(this,ColorFilter.Filter.WARM))
            }
            R.id.mBlur ->{
                glView.setFilter(ContrastColorFilter(this,ColorFilter.Filter.BLUR))
            }
            R.id.mMagn ->{
                glView.setFilter(ContrastColorFilter(this,ColorFilter.Filter.MAGN))
            }


        }
        glView.render.getFilter().isHalf=isHalf
        glView.requestRender()
        return super.onOptionsItemSelected(item)
    }

}

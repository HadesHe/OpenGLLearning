package com.example.demo.openglapplication.render

import android.view.View
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

/**
 * 球体
 */
class Ball(view:View):Shape(view){
    override fun onDrawFrame(gl: GL10?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


    private val step=5f

    private fun createBallPos(): FloatArray {
        //球以(0,0,0)为中心，以R为半径，则球上任意一点的坐标为
        // ( R * cos(a) * sin(b),y0 = R * sin(a),R * cos(a) * cos(b))
        // 其中，a为圆心到点的线段与xz平面的夹角，b为圆心到点的线段在xz平面的投影与z轴的夹角
        val data = ArrayList<Float>()
        var r1: Float
        var r2: Float
        var h1: Float
        var h2: Float
        var sin: Float
        var cos: Float
        run {
            var i = -90f
            while (i < 90 + step) {
                r1 = Math.cos(i * Math.PI / 180.0).toFloat()
                r2 = Math.cos((i + step) * Math.PI / 180.0).toFloat()
                h1 = Math.sin(i * Math.PI / 180.0).toFloat()
                h2 = Math.sin((i + step) * Math.PI / 180.0).toFloat()
                // 固定纬度, 360 度旋转遍历一条纬线
                val step2 = step * 2
                var j = 0.0f
                while (j < 360.0f + step) {
                    cos = Math.cos(j * Math.PI / 180.0).toFloat()
                    sin = -Math.sin(j * Math.PI / 180.0).toFloat()

                    data.add(r2 * cos)
                    data.add(h2)
                    data.add(r2 * sin)
                    data.add(r1 * cos)
                    data.add(h1)
                    data.add(r1 * sin)
                    j += step2
                }
                i += step
            }
        }
        val f = FloatArray(data.size)
        for (i in f.indices) {
            f[i] = data.get(i)
        }
        return f
    }

}
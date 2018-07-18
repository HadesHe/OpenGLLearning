package com.example.demo.openglapplication.filter

import android.content.Context
import android.opengl.GLES20

class ColorFilter(context: Context,val filter:Filter):AFilter(context,"filter/default_vertex.sh","filter/color_fragment.sh"){


    override fun onDrawCreatedSet(mProgram: Int) {
        hChangeType=GLES20.glGetUniformLocation(mProgram,"vChangeType")
        hChangeColor=GLES20.glGetUniformLocation(mProgram,"vChangeColor")

    }

    private var hChangeType=0

    private var hChangeColor=0

    override fun onDrawSet() {
        GLES20.glUniform1i(hChangeType,filter.type)
        GLES20.glUniform3fv(hChangeColor,1,filter.data(),0)
    }


    enum class Filter  constructor(val type: Int, val data: FloatArray) {

        NONE(0, floatArrayOf(0.0f, 0.0f, 0.0f)),
        GRAY(1, floatArrayOf(0.299f, 0.587f, 0.114f)),
        COOL(2, floatArrayOf(0.0f, 0.0f, 0.1f)),
        WARM(2, floatArrayOf(0.1f, 0.1f, 0.0f)),
        BLUR(3, floatArrayOf(0.006f, 0.004f, 0.002f)),
        MAGN(4, floatArrayOf(0.0f, 0.0f, 0.4f));

        fun data(): FloatArray {
            return data
        }

    }

}
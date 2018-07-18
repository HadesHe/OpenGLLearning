package com.example.demo.openglapplication.filter

import android.content.Context
import android.opengl.GLES20

class ContrastColorFilter(context: Context,val filter: ColorFilter.Filter):AFilter(context,"filter/half_color_vertex.sh","filter/half_color_fragment.sh"){

    private var hChangeType=0

    private var hChangeColor=0

    override fun onDrawCreatedSet(mProgram: Int) {
        hChangeType=GLES20.glGetUniformLocation(mProgram,"vChangeType")
        hChangeColor=GLES20.glGetUniformLocation(mProgram,"vChangeColor")
    }

    override fun onDrawSet() {
        GLES20.glUniform1i(hChangeType,filter.type)
        GLES20.glUniform3fv(hChangeColor,1,filter.data,0)
    }


}
package com.example.demo.openglapplication.etc

import android.content.res.Resources
import android.opengl.GLES20
import com.example.demo.openglapplication.camera.AFilter

class ZipMulDrawer(resources: Resources): AFilter(resources) {
    private lateinit var texture: IntArray

    override fun onCreate() {
        createProgramByAssetsFile("shader/pkm_mul_vert","shader/pkm_mul_frag")
        texture=IntArray(2)
        createEtcTexture(texture)
        setTextureId(texture[0])

        mGlHAlpha=GLES20.glGetUniformLocation(mProgram,"vTextureAlpha")
    }

    override fun onSizeChanged(width: Int, height: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onClear(){
        GLES20.glClearColor(0.0f,0.0f,0.0f,0.0f)
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT or GLES20.GL_DEPTH_BUFFER_BIT)
    }

}

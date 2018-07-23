package com.example.demo.openglapplication.etc

import android.content.res.Resources
import android.opengl.ETC1
import android.opengl.ETC1Util
import android.opengl.GLES20
import com.example.demo.openglapplication.camera.AFilter
import java.nio.ByteBuffer

class ZipMulDrawer(resources: Resources): AFilter(resources) {
    private lateinit var texture: IntArray

    override fun onCreate() {
        createProgramByAssetsFile("shader/pkm_mul_vert","shader/pkm_mul_frag")
        texture=IntArray(2)
        createEtcTexture(texture)
        setTextureId(texture[0])

        mGlHAlpha=GLES20.glGetUniformLocation(mProgram,"vTextureAlpha")
    }

    private lateinit var emptyBuffer: ByteBuffer

    private var width: Int = 0

    private var height: Int=0

    override fun onSizeChanged(width: Int, height: Int) {

        emptyBuffer=ByteBuffer.allocateDirect(ETC1.getEncodedDataSize(width, height))
        this.width=width
        this.height=height
        GLES20.glEnable(GLES20.GL_BLEND)
        GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA,GLES20.GL_ONE_MINUS_SRC_ALPHA)

    }

    override fun onClear(){
        GLES20.glClearColor(0.0f,0.0f,0.0f,0.0f)
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT or GLES20.GL_DEPTH_BUFFER_BIT)
    }

    override fun onBindTexture() {
        ETC1Util.
    }



}

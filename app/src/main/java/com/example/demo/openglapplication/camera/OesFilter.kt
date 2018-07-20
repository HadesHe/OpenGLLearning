package com.example.demo.openglapplication.camera

import android.content.res.Resources
import android.opengl.GLES11
import android.opengl.GLES11Ext
import android.opengl.GLES20
import java.util.*

class OesFilter(resources: Resources): AFilter(resources) {
    var mHCoordMatrix: Int=0

    override fun onCreate() {
        createProgramByAssetsFile("shader/oes_base_vertex.sh","shader/oes_base_fragment.sh")
        mHCoordMatrix=GLES20.glGetUniformLocation(mProgram,"vCoordMatrix")
    }

    private val mCoordMatix= Arrays.copyOf(OM,16)

    override fun onSetExpandData(){
        super.onSetExpandData()
        GLES20.glUniformMatrix4fv(mHCoordMatrix,1,false,mCoordMatix,0)

    }

    override fun onBindTexture(){
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0+textureType)
        GLES20.glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES,textureId)
        GLES20.glUniform1i(mHTexture,textureType)
    }

    override fun onSizeChanged(width: Int, height: Int) {
    }


}

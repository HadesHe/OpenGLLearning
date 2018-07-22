package com.example.demo.openglapplication.camera

import android.content.res.Resources
import android.graphics.SurfaceTexture
import android.opengl.GLES11Ext
import android.opengl.GLES20
import android.opengl.GLSurfaceView
import android.opengl.Matrix
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

class CameraDrawer(resources: Resources):GLSurfaceView.Renderer {
    private var cameraId=1

    private var dataWidth: Int=0

    private var dataHeight: Int=0

    private var width: Int=0

    private var height=0

    private var mOesFilter: OesFilter

    private val matrix=FloatArray(16)

    init {
        mOesFilter= OesFilter(resources)
    }

    fun setDataSize(x: Int, y: Int) {
        this.dataWidth=x
        this.dataHeight=y
        calculateMatrix()
    }

    fun setViewSize(width: Int, height: Int) {
        this.width=width
        this.height=height
        calculateMatrix()
    }





    fun calculateMatrix(changeMatrix:Boolean=false){
        Gl2Utils.getShowMatrix(matrix,this.dataWidth,this.dataHeight,this.width,this.height)
        if(cameraId==1){
            Gl2Utils.flip(matrix,true,false)
            Gl2Utils.rotate(matrix,90.0f)

        }else{
            Gl2Utils.rotate(matrix,270.0f)
        }

        if(changeMatrix){
            Matrix.scaleM(matrix,0,0.5f,0.5f,1.0f)
        }
        mOesFilter.matrix=matrix
    }



    private lateinit var surfaceTexture: SurfaceTexture

    fun getSurfaceTexture(): SurfaceTexture {
        return surfaceTexture
    }

    fun setCameraId(id:Int){
        this.cameraId=id
        calculateMatrix()
    }

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        val texture=createTextureID()
        surfaceTexture=SurfaceTexture(texture)
        mOesFilter.create()
        mOesFilter.textureId=texture
    }

    private fun createTextureID(): Int {
        val texture=IntArray(1)
        GLES20.glGenTextures(1,texture,0)
        GLES20.glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, texture[0])
        GLES20.glTexParameterf(GLES11Ext.GL_TEXTURE_EXTERNAL_OES,
                GL10.GL_TEXTURE_MIN_FILTER,GL10.GL_LINEAR.toFloat())
        GLES20.glTexParameterf(GLES11Ext.GL_TEXTURE_EXTERNAL_OES,
                GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR.toFloat())
        GLES20.glTexParameteri(GLES11Ext.GL_TEXTURE_EXTERNAL_OES,
                GL10.GL_TEXTURE_WRAP_S, GL10.GL_CLAMP_TO_EDGE)
        GLES20.glTexParameteri(GLES11Ext.GL_TEXTURE_EXTERNAL_OES,
                GL10.GL_TEXTURE_WRAP_T, GL10.GL_CLAMP_TO_EDGE)
        return texture[0]

    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        setViewSize(width,height)
    }



    override fun onDrawFrame(gl: GL10?) {
        if(surfaceTexture!=null){
            surfaceTexture.updateTexImage()
        }
        mOesFilter.draw()
    }




}

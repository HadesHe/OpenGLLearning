package com.example.demo.openglapplication.filter

import android.content.Context
import android.graphics.Bitmap
import android.opengl.GLES20
import android.opengl.GLSurfaceView
import android.opengl.GLUtils
import android.opengl.Matrix
import android.support.v7.view.menu.MenuPopupHelper
import com.example.demo.openglapplication.render.ShaderEx
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

abstract class AFilter(val context: Context,val vertext:String,val fragment:String):GLSurfaceView.Renderer{
    private var bPos: FloatBuffer

    private var bCoord: FloatBuffer

    private lateinit var mBitmap:Bitmap

    init {
        val bb=ByteBuffer.allocateDirect(sPos.size*4)
        bb.order(ByteOrder.nativeOrder())
        bPos=bb.asFloatBuffer()
        bPos.put(sPos)
        bPos.position(0)

        val cc=ByteBuffer.allocateDirect(sCoord.size*4)
        cc.order(ByteOrder.nativeOrder())
        bCoord=cc.asFloatBuffer()
        bCoord.put(sCoord)
        bCoord.position(0)
    }


    private var mProgram: Int=0

    private var glHPosition: Int=0

    private var glHCoordinate=0

    private var glHTexture=0

    private var glHMatrix=0

    private var hIsHalf=0

    private var glHUxy=0

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        GLES20.glClearColor(1.0f,1.0f,1.0f,1.0f)
        GLES20.glEnable(GLES20.GL_TEXTURE_2D)
        mProgram=ShaderEx.createProgram(context.resources,vertext,fragment)
        glHPosition=GLES20.glGetAttribLocation(mProgram,"vPosition")
        glHCoordinate=GLES20.glGetAttribLocation(mProgram,"vCoordinate")
        glHTexture=GLES20.glGetUniformLocation(mProgram,"vTexture")
        glHMatrix=GLES20.glGetUniformLocation(mProgram,"vMatrix")
        hIsHalf=GLES20.glGetUniformLocation(mProgram,"vIsHalf")
        glHUxy=GLES20.glGetUniformLocation(mProgram,"uXY")
        onDrawCreatedSet(mProgram)


    }

    abstract fun onDrawCreatedSet(mProgram: Int)

    private var uXY=-0.0f

    private val mProjectMatrix=FloatArray(16)

    private val mViewMatrix=FloatArray(16)

    private val mMVPMatrix=FloatArray(16)

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        GLES20.glViewport(0,0,width,height)

        val w=mBitmap.width
        val h=mBitmap.height
        val sWH=w.toFloat()/h
        val sWidthHeight=width.toFloat()/height

        uXY=sWidthHeight
        if(width>height){
            //图片比例大于屏幕比例
            if(sWH>sWidthHeight){
                Matrix.orthoM(mProjectMatrix,0,-sWidthHeight*sWH,sWidthHeight*sWH,-1.0f,1.0f,3.0f,5.0f)
            }else{
                Matrix.orthoM(mProjectMatrix,0,-sWidthHeight/sWH,sWidthHeight/sWH,-1.0f,1.0f,3.0f,5.0f)
            }
        }else{
            if(sWH>sWidthHeight){
                Matrix.orthoM(mProjectMatrix,0,-1.0f,1.0f,-1/sWidthHeight*sWH,1/sWidthHeight*sWH,3.0f,5.0f)
            }else{
                Matrix.orthoM(mProjectMatrix,0,-1.0f,1.0f,-sWH/sWidthHeight,sWH/sWidthHeight,3.0f,5.0f)

            }
        }
        Matrix.setLookAtM(mViewMatrix,0,
                0.0f,0.0f,5.0f,
                0.0f,0.0f,0.0f,
                0.0f,1.0f,0.0f)
        Matrix.multiplyMM(mMVPMatrix,0,mProjectMatrix,0,mViewMatrix,0)
    }

    private val isHalf=false

    private var textureId=0

    override fun onDrawFrame(gl: GL10?) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT or  GLES20.GL_DEPTH_BUFFER_BIT)
        GLES20.glUseProgram(mProgram)
        onDrawSet()
        GLES20.glUniform1i(hIsHalf,if(isHalf)1 else 0)
        GLES20.glUniform1f(glHUxy,uXY)
        GLES20.glUniformMatrix4fv(glHMatrix,1,false,mMVPMatrix,0)
        GLES20.glEnableVertexAttribArray(glHPosition)
        GLES20.glEnableVertexAttribArray(glHCoordinate)
        GLES20.glUniform1i(glHTexture,0)
        textureId=createTexture()
        GLES20.glVertexAttribPointer(glHPosition,2,GLES20.GL_FLOAT,false,0,bPos)
        GLES20.glVertexAttribPointer(glHCoordinate,2,GLES20.GL_FLOAT,false,0,bCoord)
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP,0,4)

    }

    private fun createTexture(): Int {
        val texture=IntArray(1)
        if(mBitmap!=null&&mBitmap.isRecycled.not()){
            GLES20.glGenTextures(1,texture,0)
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D,texture[0])
            GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D,GLES20.GL_TEXTURE_MIN_FILTER,GLES20.GL_NEAREST.toFloat())
            GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D,GLES20.GL_TEXTURE_MAG_FILTER,GLES20.GL_LINEAR.toFloat());
            //设置环绕方向S，截取纹理坐标到[1/2n,1-1/2n]。将导致永远不会与border融合
            GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S,GLES20.GL_CLAMP_TO_EDGE.toFloat());
            //设置环绕方向T，截取纹理坐标到[1/2n,1-1/2n]。将导致永远不会与border融合
            GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T,GLES20.GL_CLAMP_TO_EDGE.toFloat());
            //根据以上指定的参数，生成一个2D纹理
            GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, mBitmap, 0);
            return texture[0]
        }
        return 0
    }

    abstract fun onDrawSet()

    companion object {
        val sPos= floatArrayOf(
                -1.0f,1.0f,
                -1.0f,-1.0f,
                1.0f,1.0f,
                1.0f,-1.0f
        )

        val sCoord= floatArrayOf(
                0.0f,0.0f,
                0.0f,1.0f,
                1.0f,0.0f,
                1.0f,1.0f
        )
    }

}
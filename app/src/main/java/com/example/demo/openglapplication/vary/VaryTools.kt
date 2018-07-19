package com.example.demo.openglapplication.vary

import android.opengl.Matrix
import java.util.*

class VaryTools {

    private var mMatrixCamera=FloatArray(16)

    private var mMatrixProjection=FloatArray(16)

    private var mMatrixCurrent= floatArrayOf(
            1.0f,0.0f,0.0f,0.0f,
            0.0f,1.0f,0.0f,0.0f,
            0.0f,0.0f,1.0f,0.0f,
            0.0f,0.0f,0.0f,1.0f
    )

    private var mStack: Stack<FloatArray>


    init {
        mStack= Stack<FloatArray>()
    }

    fun pushMatrix(){
        mStack.push(Arrays.copyOf(mMatrixCurrent,16))
    }

    fun popMatrix(){
        mMatrixCurrent=mStack.pop()
    }

    fun clear(){
        mStack.clear()
    }

    //平移
    fun translate(x:Float,y :Float,z:Float){
        Matrix.translateM(mMatrixCurrent,0,x,y,z)
    }

    //旋转
    fun rotate(angel:Float,x:Float,y:Float,z:Float){
        Matrix.rotateM(mMatrixCurrent,0,angel,x,y,z)
    }

    //缩放变换
    fun scale(x:Float,y:Float,z:Float){
        Matrix.scaleM(mMatrixCurrent,0,x,y,z)
    }

    fun setCamera(ex: Float, ey: Float, ez: Float, cx: Float, cy: Float, cz: Float, ux: Float, uy: Float, uz: Float ){
        Matrix.setLookAtM(mMatrixCamera,0,ex,ey,ez,cx,cy,cz,ux,uy,uz)
    }

    fun ortho(left: Float, right: Float, bottom: Float, top: Float, near: Float, far: Float) {
        Matrix.orthoM(mMatrixProjection,0,left,right,bottom,top,near,far)
    }

    fun getFinalMatrix(): FloatArray {
        var ans=FloatArray(16)
        Matrix.multiplyMM(ans,0,mMatrixCamera,0,mMatrixCurrent,0)
        Matrix.multiplyMM(ans,0,mMatrixProjection,0,ans,0)
        return ans
    }





}

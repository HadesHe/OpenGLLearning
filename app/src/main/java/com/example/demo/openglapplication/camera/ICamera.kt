package com.example.demo.openglapplication.camera

import android.graphics.Point
import android.graphics.SurfaceTexture

interface ICamera {

    fun open(cameraId:Int):Boolean

    fun setConfig(config: Config)

    fun preview():Boolean

    fun switchTo(cameraId: Int):Boolean

    fun takePhoto(callback:TakePhotoCallback)

    fun close():Boolean

    fun setPreviewTexture(texture:SurfaceTexture)

    fun getPreviewSize(): Point

    fun getPictureSize():Point

    fun setPreviewTexture(callback:PreviewFrameCallback)

    data class Config(val rate:Float,val minPreviewWidth:Int,val minPictureWidth:Int)

    interface TakePhotoCallback{
        fun onTakePhoto(bytes:ByteArray,width:Int,height:Int)

    }

    interface PreviewFrameCallback{
        fun onPreviewFrame(bytes: ByteArray,width: Int,height: Int)
    }



}

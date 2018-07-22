package com.example.demo.openglapplication.camera

import android.graphics.Point
import android.graphics.SurfaceTexture
import android.hardware.Camera
import android.util.Log
import java.util.*



class KitkatCamera:ICamera {


    private var mConfig: ICamera.Config


    private var sizeComparator: CameraSizeComparator

    init {
        this.mConfig=ICamera.Config(1.778f,720,720)
        sizeComparator=CameraSizeComparator()
    }

    private lateinit var mCamera: Camera

    private lateinit var mPicSize: Point

    private lateinit var mPreSize: Point

    private lateinit var picSize: Camera.Size

    private lateinit var preSize: Camera.Size

    override fun open(cameraId: Int): Boolean {
        mCamera=Camera.open(cameraId)
        if(mCamera != null) {
            val param = mCamera.parameters
            picSize = getPropPictureSize(param.supportedPictureSizes, mConfig.rate,
                    mConfig.minPictureWidth)
            preSize = getPropPreviewSize(param.supportedPreviewSizes, mConfig.rate, mConfig
                    .minPreviewWidth)
            param.setPictureSize(picSize.width, picSize.height)
            param.setPreviewSize(preSize.width, preSize.height)
            mCamera.parameters = param
            val pre = param.previewSize
            val pic = param.pictureSize
            mPicSize = Point(pic.height, pic.width)
            mPreSize = Point(pre.height, pre.width)
            Log.e("wuwang", "camera previewSize:" + mPreSize.x + "/" + mPreSize.y)
            return true
        }
        return false
    }


    override fun setConfig(config: ICamera.Config) {
        this.mConfig=config
    }



    override fun preview(): Boolean {
        if (mCamera != null) {
            mCamera.startPreview()
        }

        return false
    }

    override fun switchTo(cameraId: Int): Boolean {
        close()
        open(cameraId)
        return false
    }

    override fun takePhoto(callback: ICamera.TakePhotoCallback) {
    }

    override fun close(): Boolean {
        if(mCamera!=null) {
            mCamera.stopPreview()
            mCamera.release()
        }
        return false
    }

    override fun setPreviewTexture(texture: SurfaceTexture) {
        if(mCamera != null){
            mCamera.setPreviewTexture(texture)
        }
    }

    override fun getPreviewSize(): Point {
        return mPreSize
    }

    override fun getPictureSize(): Point {
        return mPicSize
    }

    override fun setOnPreviewFrameCallback(callback: ICamera.PreviewFrameCallback) {
        if(mCamera!= null){
            mCamera.setPreviewCallback(object:Camera.PreviewCallback{
                override fun onPreviewFrame(data: ByteArray, camera: Camera?) {
                    callback.onPreviewFrame(data,mPreSize.x,mPreSize.y)
                }
            })
        }
    }

    fun  addBuffer(buffer:ByteArray){
        if(mCamera!=null){
            mCamera.addCallbackBuffer(buffer)
        }
    }

    fun setOnPreviewFrameCallbackWithBuffer(callback:ICamera.PreviewFrameCallback){
        if(mCamera!=null){
            mCamera.setPreviewCallbackWithBuffer(object:Camera.PreviewCallback{
                override fun onPreviewFrame(data: ByteArray, camera: Camera?) {
                    callback.onPreviewFrame(data,mPreSize.x,mPreSize.y)
                }

            })
        }
    }

    private fun getPropPreviewSize(list: List<Camera.Size>, th: Float, minWidth: Int): Camera.Size {
        Collections.sort(list, sizeComparator)

        var i = 0
        for (s in list) {
            if (s.height >= minWidth && equalRate(s, th)) {
                break
            }
            i++
        }
        if (i == list.size) {
            i = 0
        }
        return list[i]
    }

    private fun getPropPictureSize(list: List<Camera.Size>, th: Float, minWidth: Int): Camera.Size {
        Collections.sort(list, sizeComparator)

        var i = 0
        for (s in list) {
            if (s.height >= minWidth && equalRate(s, th)) {
                break
            }
            i++
        }
        if (i == list.size) {
            i = 0
        }
        return list[i]
    }

    private fun equalRate(s: Camera.Size, rate: Float): Boolean {
        val r = s.width.toFloat() / s.height.toFloat()
        return if (Math.abs(r - rate) <= 0.03) {
            true
        } else {
            false
        }
    }

    private class CameraSizeComparator :Comparator<Camera.Size>{
        override fun compare(o1: Camera.Size, o2: Camera.Size): Int {
            if(o1.height==o2.height){
                return 0
            }else if(o1.height>o2.height){
                return 1
            }else{
                return -1
            }

        }

    }

}

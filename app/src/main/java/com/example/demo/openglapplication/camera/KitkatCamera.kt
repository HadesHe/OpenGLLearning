package com.example.demo.openglapplication.camera

import android.graphics.Point
import android.graphics.SurfaceTexture
import android.hardware.Camera

class KitkatCamera:ICamera {

    private var mConfig: ICamera.Config

    private var sizeComparator: CameraSizeComparator

    init {
        this.mConfig=ICamera.Config(1.778f,720,720)
        sizeComparator=CameraSizeComparator()
    }

    private lateinit var mCamera: Camera

    override fun open(cameraId: Int): Boolean {
        mCamera=Camera.open(cameraId)
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun setConfig(config: ICamera.Config) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun preview(): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun switchTo(cameraId: Int): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun takePhoto(callback: ICamera.TakePhotoCallback) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun close(): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun setPreviewTexture(texture: SurfaceTexture) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getPreviewSize(): Point {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getPictureSize(): Point {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun setPreviewTexture(callback: ICamera.PreviewFrameCallback) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
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

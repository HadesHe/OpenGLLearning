package com.example.demo.openglapplication

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.SurfaceTexture
import android.hardware.Camera
import android.hardware.camera2.CameraCharacteristics
import android.hardware.camera2.CameraDevice
import android.hardware.camera2.CameraManager
import android.opengl.GLSurfaceView
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.example.demo.openglapplication.camera.PermissionEx
import android.os.Build
import android.os.Handler
import android.os.HandlerThread
import android.util.Size
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.view.View
import android.widget.ImageButton
import android.widget.Toast
import com.example.demo.openglapplication.camera.Renderer
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10


class Camera2Activity:AppCompatActivity(), View.OnClickListener {



    override fun onClick(v: View) {
        when(v.id){
            R.id.mImageButton ->{
                mController.takePhoto()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        mController.onResume()
    }

    override fun onPause() {
        super.onPause()
        mController.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        if (mController != null) {
            mController.destroy();
        }
    }

    private lateinit var mShutter: ImageButton
    private val initViewRunnable: Runnable =object:Runnable, View.OnClickListener {
        override fun run() {
            if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP){
                mRenderer=Camera2Renderer()
            }else{
                mRenderer=Camera1Renderer()

            }

            setContentView(R.layout.activity_camera2)
            mSurfaceView=findViewById<SurfaceView>(R.id.mSurface)
            mShutter=findViewById<ImageButton>(R.id.mImageButton)
            mShutter.setOnClickListener(this@Camera2Activity)
            mController=TextureController(this@Camera2Activity)

            onFilterSet(mController)
            mController.setFrameCallback(720,1280,this@Camera2Activity)
            mSurfaceView.holder.addCallback(object:SurfaceHolder.Callback{
                override fun surfaceChanged(holder: SurfaceHolder?, format: Int, width: Int, height: Int) {
                    mController.surfaceChanged(width,height)
                }

                override fun surfaceDestroyed(holder: SurfaceHolder?) {
                    mController.surfaceDestroyed()
                }

                override fun surfaceCreated(holder: SurfaceHolder?) {
                    mController.surfaceCreated(holder)
                    mController.setRenderer(mRenderer)
                }
            })
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        PermissionEx.askPermission(this, arrayOf(Manifest.permission.CAMERA, Manifest
                .permission.WRITE_EXTERNAL_STORAGE), 10, initViewRunnable);
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        PermissionEx.onRequestPermissionsResult(requestCode==10,grantResults,initViewRunnable,object :Runnable{
            override fun run() {
                Toast.makeText(this@Camera2Activity,"没有获得必要的权限",Toast.LENGTH_SHORT).show()
                finish()
            }
        })
    }

    class Camera1Renderer: Renderer {
        private val cameraId: Int=1
        override fun onDestroy() {
            if(mCamera!=null){
                mCamera?.stopPreview()
                mCamera?.release()
                mCamera=null
            }
        }

        override fun onDrawFrame(gl: GL10?) {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        private var mCamera: Camera?=null

        override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
            if(mCamera!=null){
                mCamera?.stopPreview()
                mCamera?.release()
                mCamera=null
            }
            mCamera= Camera.open(cameraId)
            mController.setImageDirection(cameraId)
            val size=mCamera?.parameters?.previewSize
            mController.setDataSize(requireNotNull(size).height, requireNotNull(size).width)

            requireNotNull(mCamera).setPreviewTexture(mController.getTexture())
            mController.getTexture().setOnFrameAvailableListener(object:SurfaceTexture.OnFrameAvailableListener{
                override fun onFrameAvailable(surfaceTexture: SurfaceTexture?) {
                    mController.requestRender()
                }

            })
            requireNotNull(mCamera).startPreview()


        }

        override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        }
    }

    class Camera2Renderer(val context:Context):Renderer{

        private var mCameraManager: CameraManager

        private var mThread: HandlerThread

        private var mHandler: Handler

        init {
            mCameraManager = context.getSystemService(CAMERA_SERVICE) as CameraManager
            mThread = HandlerThread("camera2 ");
            mThread.start()
            mHandler = Handler(mThread.getLooper());
        }

        private var mDevice: CameraDevice? = null

        override fun onDestroy() {
            requireNotNull(mDevice).close()
            mDevice=null
        }

        override fun onDrawFrame(gl: GL10?) {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        private val cameraId: Int = 1

        private lateinit var mPreviewSize: Size

        @SuppressLint("MissingPermission")
        override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {

            requireNotNull(mDevice).close()
            mDevice==null

            val c=mCameraManager.getCameraCharacteristics(cameraId.toString())
            val map=c.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP)
            val sizes=map.getOutputSizes(SurfaceHolder::class.java)

            mPreviewSize=sizes[0]
            mController.setDataSize(mPreviewSize.height,mPreviewSize.width)

            mCameraManager.openCamera(cameraId.toString(),object: CameraDevice.StateCallback() {
                override fun onOpened(camera: CameraDevice?) {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }

                override fun onDisconnected(camera: CameraDevice?) {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }

                override fun onError(camera: CameraDevice?, error: Int) {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }

            },mHandler)


        }

    }

}



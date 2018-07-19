package com.example.demo.openglapplication.vary

import android.content.res.Resources
import android.opengl.GLES20
import android.opengl.GLSurfaceView
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

class VaryRender(val resources: Resources) : GLSurfaceView.Renderer {
    private var cube: Cube

    private var tools: VaryTools

    private var x=0.0f


    init{
        tools=VaryTools()
        cube= Cube(resources)
    }

    override fun onDrawFrame(gl: GL10?) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT or GLES20.GL_DEPTH_BUFFER_BIT)
        cube.matrix=tools.getFinalMatrix()
        cube.drawSelf()

        //y轴正方形平移
        tools.pushMatrix()
        tools.translate(x,3.0f,0.0f)
        tools.scale(0.5f,0.5f,0.5f)
        cube.matrix=tools.getFinalMatrix()
        cube.drawSelf()
        tools.popMatrix()

//        //y轴负方向平移，然后按xyz -> (0,0,0)到(1,1,1)旋转30度
        tools.pushMatrix()
        tools.translate(0.0f,-3.0f,0.0f)
        cube.matrix=tools.getFinalMatrix()
        cube.drawSelf()
        tools.popMatrix()
//
//
//        //x轴负方向平移，然后按xyz -> (0,0,0)到(1,-1,1)旋转120度，再放大0.5倍
        tools.pushMatrix()
        tools.translate(-3.0f,0.0f,0.0f)
        tools.scale(0.5f,0.5f,0.5f)

        //在以上变换的基础上再进行变换
        tools.pushMatrix()
        tools.translate(12.0f,0.0f,0.0f)
        tools.scale(1.0f,2.0f,1.0f)
        tools.rotate(30f,1.0f,2.0f,1.0f)
        cube.matrix=tools.getFinalMatrix()
        cube.drawSelf()
        tools.popMatrix()


        tools.rotate(30f,-1.0f,-1.0f,1.0f)
        cube.matrix=tools.getFinalMatrix()
        cube.drawSelf()
        tools.popMatrix()



    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        GLES20.glViewport(0,0,width,height)
        val rate=width.toFloat()/height
        tools.ortho(-rate*6,rate*6,-6.0f,6.0f,3.0f,20.0f)
        tools.setCamera(0.0f,0.0f,10.0f,0.0f,0.0f,0.0f,0.0f,1.0f,0.0f)
    }

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        GLES20.glClearColor(1.0f,1.0f,1.0f,1.0f)
        GLES20.glEnable(GLES20.GL_DEPTH_TEST)
        cube.create()
    }

    fun setX(x: Float) {
        this.x=x
    }

}

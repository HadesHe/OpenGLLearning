package com.example.demo.openglapplication.vary

import android.content.res.Resources
import android.opengl.GLES20
import com.example.demo.openglapplication.render.ShaderEx
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer
import java.nio.ShortBuffer

class Cube(val res:Resources){

    private var vertexBuf: FloatBuffer

    private var colorBuf: FloatBuffer

    private var indexBuf: ShortBuffer

    private var mProgram=0

    private var hVertex=0

    private var hColor=0

    private var hMatrix=0

    var matrix= FloatArray(16)

    init {
        val a=ByteBuffer.allocateDirect(cubePosition.size*4)
        a.order(ByteOrder.nativeOrder())
        vertexBuf=a.asFloatBuffer()
        vertexBuf.put(cubePosition)
        vertexBuf.position(0)

        val b=ByteBuffer.allocateDirect(color.size*4)
        b.order(ByteOrder.nativeOrder())
        colorBuf=b.asFloatBuffer()
        colorBuf.put(color)
        colorBuf.position(0)

        val c=ByteBuffer.allocateDirect(index.size*2)
        c.order(ByteOrder.nativeOrder())
        indexBuf=c.asShortBuffer()
        indexBuf.put(index)
        indexBuf.position(0)

        create()

    }

     fun create() {
        mProgram=ShaderEx.createProgram(res,"vary/vertex.sh","vary/fragment.sh")
        hVertex=GLES20.glGetAttribLocation(mProgram,"vPosition")
        hColor=GLES20.glGetAttribLocation(mProgram,"aColor")
        hMatrix=GLES20.glGetUniformLocation(mProgram,"vMatrix")
    }

    fun drawSelf(){
        GLES20.glUseProgram(mProgram)
        if(matrix!=null){
            GLES20.glUniformMatrix4fv(hMatrix,1,false,matrix,0)
        }

        GLES20.glEnableVertexAttribArray(hVertex)
        GLES20.glEnableVertexAttribArray(hColor)
        GLES20.glEnableVertexAttribArray(hColor)
        GLES20.glVertexAttribPointer(hVertex,3,GLES20.GL_FLOAT,false,0,vertexBuf)
        GLES20.glVertexAttribPointer(hColor,4,GLES20.GL_FLOAT,false,0, colorBuf)
        GLES20.glDrawElements(GLES20.GL_TRIANGLES, index.size,GLES20.GL_UNSIGNED_SHORT,indexBuf)
        GLES20.glDisableVertexAttribArray(hVertex)
        GLES20.glDisableVertexAttribArray(hColor)

    }


    companion object {
        private val cubePosition= floatArrayOf(
                -1.0f,1.0f,1.0f,    //正面左上0
                -1.0f,-1.0f,1.0f,   //正面左下1
                1.0f,-1.0f,1.0f,    //正面右下2
                1.0f,1.0f,1.0f,     //正面右上3
                -1.0f,1.0f,-1.0f,    //反面左上4
                -1.0f,-1.0f,-1.0f,   //反面左下5
                1.0f,-1.0f,-1.0f,    //反面右下6
                1.0f,1.0f,-1.0f     //反面右上7
        )

        private val index= shortArrayOf(
                6,7,4,6,4,5,    //后面
                6,3,7,6,2,3,    //右面
                6,5,1,6,1,2,    //下面
                0,3,2,0,2,1,    //正面
                0,1,5,0,5,4,    //左面
                0,7,3,0,4,7  //上面
        )

        private val color= floatArrayOf(
            0f,1f,0f,1f,
            0f,1f,0f,1f,
            0f,1f,0f,1f,
            0f,1f,0f,1f,
            1f,0f,0f,1f,
            1f,0f,0f,1f,
            1f,0f,0f,1f,
            1f,0f,0f,1f
        )
    }
}
package com.example.demo.openglapplication.camera

import android.content.res.Resources
import android.opengl.GLES20
import android.opengl.Matrix
import android.util.Log


object Gl2Utils {

    val TAG = "GLUtils"
    var DEBUG = true

    val TYPE_FITXY = 0
    val TYPE_CENTERCROP = 1
    val TYPE_CENTERINSIDE = 2
    val TYPE_FITSTART = 3
    val TYPE_FITEND = 4


    fun getShowMatrix(matrix: FloatArray, imgWidth: Int, imgHeight: Int, viewWidth: Int, viewHeight: Int) {
        if (imgHeight > 0 && imgWidth > 0 && viewWidth > 0 && viewHeight > 0) {
            val sWhView = viewWidth.toFloat() / viewHeight
            val sWhImg = imgWidth.toFloat() / imgHeight
            val projection = FloatArray(16)
            val camera = FloatArray(16)
            if (sWhImg > sWhView) {
                Matrix.orthoM(projection, 0, -sWhView / sWhImg, sWhView / sWhImg, -1.0f, 1.0f, 1.0f, 3.0f)
            } else {
                Matrix.orthoM(projection, 0, -1.0f, 1.0f, -sWhImg / sWhView, sWhImg / sWhView, 1.0f, 3.0f)
            }
            Matrix.setLookAtM(camera, 0, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f)
            Matrix.multiplyMM(matrix, 0, projection, 0, camera, 0)
        }
    }

    fun getMatrix(matrix: FloatArray, type: Int, imgWidth: Int, imgHeight: Int, viewWidth: Int,
                  viewHeight: Int) {
        if (imgHeight > 0 && imgWidth > 0 && viewWidth > 0 && viewHeight > 0) {
            val projection = FloatArray(16)
            val camera = FloatArray(16)
            if (type == TYPE_FITXY) {
                Matrix.orthoM(projection, 0, -1.0f, 1.0f, -1.0f, 1.0f, 1.0f, 3.0f)
                Matrix.setLookAtM(camera, 0, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f)
                Matrix.multiplyMM(matrix, 0, projection, 0, camera, 0)
            }
            val sWhView = viewWidth.toFloat() / viewHeight
            val sWhImg = imgWidth.toFloat() / imgHeight
            if (sWhImg > sWhView) {
                when (type) {
                    TYPE_CENTERCROP -> Matrix.orthoM(projection, 0, -sWhView / sWhImg, sWhView / sWhImg, -1.0f, 1.0f, 1.0f, 3.0f)
                    TYPE_CENTERINSIDE -> Matrix.orthoM(projection, 0, -1.0f, 1.0f, -sWhImg / sWhView, sWhImg / sWhView, 1.0f, 3.0f)
                    TYPE_FITSTART -> Matrix.orthoM(projection, 0, -1.0f, 1.0f, 1 - 2 * sWhImg / sWhView, 1.0f, 1.0f, 3.0f)
                    TYPE_FITEND -> Matrix.orthoM(projection, 0, -1.0f, 1.0f, -1.0f, 2 * sWhImg / sWhView - 1, 1.0f, 3.0f)
                }
            } else {
                when (type) {
                    TYPE_CENTERCROP -> Matrix.orthoM(projection, 0, -1.0f, 1.0f, -sWhImg / sWhView, sWhImg / sWhView, 1.0f, 3.0f)
                    TYPE_CENTERINSIDE -> Matrix.orthoM(projection, 0, -sWhView / sWhImg, sWhView / sWhImg, -1.0f, 1.0f, 1.0f, 3.0f)
                    TYPE_FITSTART -> Matrix.orthoM(projection, 0, -1.0f, 2 * sWhView / sWhImg - 1, -1.0f, 1.0f, 1.0f, 3.0f)
                    TYPE_FITEND -> Matrix.orthoM(projection, 0, 1 - 2 * sWhView / sWhImg, 1.0f, -1.0f, 1.0f, 1.0f, 3.0f)
                }
            }
            Matrix.setLookAtM(camera, 0, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f)
            Matrix.multiplyMM(matrix, 0, projection, 0, camera, 0)
        }
    }

    fun getCenterInsideMatrix(matrix: FloatArray, imgWidth: Int, imgHeight: Int, viewWidth: Int, viewHeight: Int) {
        if (imgHeight > 0 && imgWidth > 0 && viewWidth > 0 && viewHeight > 0) {
            val sWhView = viewWidth.toFloat() / viewHeight
            val sWhImg = imgWidth.toFloat() / imgHeight
            val projection = FloatArray(16)
            val camera = FloatArray(16)
            if (sWhImg > sWhView) {
                Matrix.orthoM(projection, 0, -1.0f, 1.0f, -sWhImg / sWhView, sWhImg / sWhView, 1.0f, 3.0f)
            } else {
                Matrix.orthoM(projection, 0, -sWhView / sWhImg, sWhView / sWhImg, -1.0f, 1.0f, 1.0f, 3.0f)
            }
            Matrix.setLookAtM(camera, 0, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f)
            Matrix.multiplyMM(matrix, 0, projection, 0, camera, 0)
        }
    }

    fun rotate(m: FloatArray, angle: Float): FloatArray {
        Matrix.rotateM(m, 0, angle, 0.0f, 0.0f, 1.0f)
        return m
    }

    fun flip(m: FloatArray, x: Boolean, y: Boolean): FloatArray {
        if (x || y) {
            Matrix.scaleM(m, 0, if (x) -1.0f else 1.0f, if (y) -1.0f else 1.0f, 1.0f)
        }
        return m
    }

    fun getOriginalMatrix(): FloatArray {
        return floatArrayOf(1f, 0f, 0f, 0f, 0f, 1f, 0f, 0f, 0f, 0f, 1f, 0f, 0f, 0f, 0f, 1f)
    }

    //通过路径加载Assets中的文本内容
    fun uRes(mRes: Resources, path: String): String? {
        var result = mRes.assets.open(path).bufferedReader().use {
            it.readText()
        }

        return result.toString().replace("\\r\\n".toRegex(), "\n")
    }

    fun createGlProgramByRes(res: Resources, vert: String, frag: String): Int {
        return createGlProgram(uRes(res, vert), uRes(res, frag))
    }

    //创建GL程序
    fun createGlProgram(vertexSource: String?, fragmentSource: String?): Int {
        val vertex = loadShader(GLES20.GL_VERTEX_SHADER, vertexSource)
        if (vertex == 0) return 0
        val fragment = loadShader(GLES20.GL_FRAGMENT_SHADER, fragmentSource)
        if (fragment == 0) return 0
        var program = GLES20.glCreateProgram()
        if (program != 0) {
            GLES20.glAttachShader(program, vertex)
            GLES20.glAttachShader(program, fragment)
            GLES20.glLinkProgram(program)
            val linkStatus = IntArray(1)
            GLES20.glGetProgramiv(program, GLES20.GL_LINK_STATUS, linkStatus, 0)
            if (linkStatus[0] != GLES20.GL_TRUE) {
                glError(1, "Could not link program:" + GLES20.glGetProgramInfoLog(program))
                GLES20.glDeleteProgram(program)
                program = 0
            }
        }
        return program
    }

    //加载shader
    fun loadShader(shaderType: Int, source: String?): Int {
        var shader = GLES20.glCreateShader(shaderType)
        if (0 != shader) {
            GLES20.glShaderSource(shader, source)
            GLES20.glCompileShader(shader)
            val compiled = IntArray(1)
            GLES20.glGetShaderiv(shader, GLES20.GL_COMPILE_STATUS, compiled, 0)
            if (compiled[0] == 0) {
                glError(1, "Could not compile shader:$shaderType")
                glError(1, "GLES20 Error:" + GLES20.glGetShaderInfoLog(shader))
                GLES20.glDeleteShader(shader)
                shader = 0
            }
        }
        return shader
    }

    fun glError(code: Int, index: Any) {
        if (DEBUG && code != 0) {
            Log.e(TAG, "glError:$code---$index")
        }
    }
}

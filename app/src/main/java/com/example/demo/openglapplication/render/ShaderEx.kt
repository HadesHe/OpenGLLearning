package com.example.demo.openglapplication.render

import android.app.DialogFragment
import android.content.res.Resources
import android.opengl.GLES20
import android.util.Log

object ShaderEx {

    private val TAG = ShaderEx::class.java.simpleName

    fun createProgram(res: Resources, vertexRes: String, fragmentRes: String): Int {
        return createProgram(loadFromAssetsFile(vertexRes, res), loadFromAssetsFile(fragmentRes, res))

    }

    fun loadShader(shaderType: Int, source: String?): Int {
        var shader = GLES20.glCreateShader(shaderType)
        if (0 != shader) {
            GLES20.glShaderSource(shader, source)
            GLES20.glCompileShader(shader)
            var compiled = IntArray(1)

            GLES20.glGetShaderiv(shader, GLES20.GL_COMPILE_STATUS, compiled, 0)

            if (0 == compiled[0]) {
                Log.e(TAG, "Could not compile shader:$shaderType")
                Log.e(TAG, "GLES20 Error:${GLES20.glGetShaderInfoLog(shader)}")
                GLES20.glDeleteShader(shader)
                shader = 0
            }
        }
        return shader
    }

    fun createProgram(vertexRes: String?, fragmentRes: String?): Int {
        var vertex = loadShader(GLES20.GL_VERTEX_SHADER, vertexRes)
        if (vertex == 0) return 0
        var fragment = loadShader(GLES20.GL_FRAGMENT_SHADER, fragmentRes)
        if (fragment == 0) return 0

        var program = GLES20.glCreateProgram()
        if (program != 0) {
            GLES20.glAttachShader(program, vertex)
            GLES20.glAttachShader(program, fragment)
            GLES20.glLinkProgram(program)
            var linkStatus = IntArray(1)
            GLES20.glGetProgramiv(program, GLES20.GL_LINK_STATUS, linkStatus, 0)
            if (linkStatus[0] != GLES20.GL_TRUE) {
                Log.e(TAG, "Could not link program:${GLES20.glGetProgramInfoLog(program)}")
                GLES20.glDeleteProgram(program)
                program = 0
            }
        }
        return program
    }

    fun loadFromAssetsFile(fname: String, res: Resources): String? {

        var result = res.assets.open(fname).bufferedReader().use {
            it.readText()
        }

        return result.toString().replace("\\r\\n".toRegex(), "\n")

    }


}
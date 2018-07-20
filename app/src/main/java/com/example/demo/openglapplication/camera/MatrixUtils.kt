package com.example.demo.openglapplication.camera

import android.opengl.Matrix



object MatrixUtils {

    val TYPE_FITXY = 0
    val TYPE_CENTERCROP = 1
    val TYPE_CENTERINSIDE = 2
    val TYPE_FITSTART = 3
    val TYPE_FITEND = 4



    /**
     * use [.getMatrix] instead
     */
    @Deprecated("")
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

    fun scale(m: FloatArray, x: Float, y: Float): FloatArray {
        Matrix.scaleM(m, 0, x, y, 1.0f)
        return m
    }

    fun getOriginalMatrix(): FloatArray {
        return floatArrayOf(1f, 0f, 0f, 0f, 0f, 1f, 0f, 0f, 0f, 0f, 1f, 0f, 0f, 0f, 0f, 1f)
    }
}

package com.example.demo.openglapplication.camera

import android.Manifest
import android.content.pm.PackageManager
import android.support.v4.app.ActivityCompat
import android.os.Build
import android.app.Activity



object PermissionEx {

    fun askPermission(context: Activity, permissions: Array<String>, req: Int, runnable: Runnable) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val result = ActivityCompat.checkSelfPermission(context, permissions[0])
            if (result == PackageManager.PERMISSION_GRANTED) {
                runnable.run()
            } else {
                ActivityCompat.requestPermissions(context, arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE), req)
            }
        } else {
            runnable.run()
        }
    }

    fun onRequestPermissionsResult(isReq: Boolean, grantResults: IntArray, okRun: Runnable, deniRun: Runnable) {
        if (isReq) {
            if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                okRun.run()
            } else {
                deniRun.run()
            }
        }
    }
}

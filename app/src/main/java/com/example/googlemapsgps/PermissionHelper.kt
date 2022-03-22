package com.example.googlemapsgps

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

object PermissionHelper {

    fun askForLocationPermission(activity: Activity) {
        ActivityCompat.requestPermissions(activity, PERMISSIONS, REQUEST_CODE_FOR_PERMISSION)
    }

    fun arePermissionsGranted(context: Context) =
        PERMISSIONS.all {
            ContextCompat.checkSelfPermission(context, it)==PackageManager.PERMISSION_GRANTED
        }

    const val REQUEST_CODE_FOR_PERMISSION = 0x1001

    private val PERMISSIONS = arrayOf(
        android.Manifest.permission.ACCESS_COARSE_LOCATION,
        android.Manifest.permission.ACCESS_FINE_LOCATION)

}
package com.cyrillrx.android.device

import android.app.ActivityManager
import android.app.UiModeManager
import android.content.Context
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.os.Build
import android.util.Log
import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader
import java.util.*
import kotlin.math.pow

/**
 * @author Cyril Leroux
 *          Created on 08/06/2018.
 */
object HardwareUtils {

    private val TAG = HardwareUtils::class.java.simpleName

    private const val KEY_PLATFORM = "device_platform"
    private const val KEY_DEVICE_TYPE = "device_type"
    private const val KEY_DEVICE_ROOTED = "device_rooted"
    private const val KEY_DEVICE_RAM = "device_ram"

    private const val VALUE_PLATFORM = "Android"

    private const val TYPE_TABLET = "tablet"
    private const val TYPE_PHONE = "phone"
    private const val TYPE_TV = "tv"

    enum class Type { PHONE, TABLET, TV }

    fun getDeviceProperties(context: Context): Map<String, String> = mapOf(
        KEY_PLATFORM to VALUE_PLATFORM,
        KEY_DEVICE_TYPE to getDeviceTypeLabel(context),
        KEY_DEVICE_ROOTED to (if (isDeviceRooted()) "yes" else "no"),
        KEY_DEVICE_RAM to getDeviceMemoryString(context)
    )

    fun getDeviceMemoryString(context: Context): String =
        "%.1f".format(Locale.US, getDeviceMemoryGb(context))

    /** @return The RAM size of the device in gigabytes. */
    private fun getDeviceMemoryGb(context: Context): Double {

        // Add RAM in Go if possible
        return try {
            val actManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
            val memInfo = ActivityManager.MemoryInfo()
            actManager.getMemoryInfo(memInfo)

            val bytesInGb = 1024.0.pow(3.0)
            memInfo.totalMem / bytesInGb

        } catch (e: Exception) {
            Log.e(TAG, "Error while getting device memory.", e)
            0.0
        }
    }

    private fun getDeviceType(context: Context): Type {

        val uiModeManager = context.getSystemService(Context.UI_MODE_SERVICE) as UiModeManager

        val currentModeType = uiModeManager.currentModeType
        if (currentModeType == Configuration.UI_MODE_TYPE_TELEVISION) {
            return Type.TV
        }

        val packageManager = context.packageManager
        if (packageManager.hasSystemFeature(PackageManager.FEATURE_TELEVISION) || packageManager.hasSystemFeature(
                PackageManager.FEATURE_LEANBACK
            )
        ) {
            return Type.TV
        }

        return if (context.resources.getBoolean(R.bool.is_tablet)) Type.TABLET else Type.PHONE
    }

    fun getDeviceTypeLabel(context: Context): String =
        when (getDeviceType(context)) {
            Type.TABLET -> TYPE_TABLET
            Type.TV -> TYPE_TV
            Type.PHONE -> TYPE_PHONE
        }

    fun isDeviceRooted(): Boolean =
        try {
            checkRootMethod1() || checkRootMethod2() || checkRootMethod3()
        } catch (e: Exception) {
            Log.e(TAG, "Error while checking root access.", e)
            false
        }

    private fun checkRootMethod1(): Boolean = Build.TAGS?.contains("test-keys") == true

    private fun checkRootMethod2(): Boolean {
        val paths = arrayOf(
            "/system/app/Superuser.apk",
            "/sbin/su",
            "/system/bin/su",
            "/system/xbin/su",
            "/data/local/xbin/su",
            "/data/local/bin/su",
            "/system/sd/xbin/su",
            "/system/bin/failsafe/su",
            "/data/local/su",
            "/su/bin/su"
        )
        return paths.any { File(it).exists() }
    }

    private fun checkRootMethod3(): Boolean {

        var process: Process? = null
        return try {
            process = Runtime.getRuntime().exec(arrayOf("/system/xbin/which", "su"))
            val input = BufferedReader(InputStreamReader(process.inputStream))
            input.readLine() != null

        } catch (t: Throwable) {
            false

        } finally {
            process?.destroy()
        }
    }
}
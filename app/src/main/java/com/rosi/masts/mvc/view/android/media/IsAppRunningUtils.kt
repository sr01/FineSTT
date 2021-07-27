package com.rosi.masts.mvc.view.android.media

import android.app.ActivityManager
import android.content.Context
import android.os.Build
import com.rosi.masts.mvc.view.android.service.MediaNotificationListenerService
import com.rosi.masts.utils.Logger
import com.rosi.masts.utils.TerminalUtils

class IsAppRunningUtils(private val context: Context, private val logger: Logger) {

    private val tag = "IsAppRunningUtils"

    fun isRunning(packageName: String): Boolean {
        //return isRunningByApp(packageName)
        return isRunningByNotification(packageName)
    }

    private fun isRunningByApp(packageName: String): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            isRunningForAPI26AndAboveUsingRoot(packageName)
        } else {
            isRunningForAPI25AndBelowByActivityManager(packageName)
        }
    }

    private fun isRunningByNotification(packageName: String): Boolean {
        return MediaNotificationListenerService.runningMediaApplications.contains(packageName)
    }

    private fun isRunningForAPI25AndBelowByActivityManager(packageName: String): Boolean {
        val am = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager

        @Suppress("DEPRECATION")
        val pid = am.getRunningServices(200).firstOrNull() { pid -> pid.service.className.contains(packageName) }
        return pid != null
    }

    private fun isRunningForAPI26AndAboveUsingRoot(packageName: String): Boolean {
        val running = TerminalUtils.isProcessExist(packageName)

        return if (running != null) {
            running
        } else {
            logger.i(tag, "can't determine if $packageName is running or not (device is probably un-rooted), assume it is running")
            true
        }
    }
}
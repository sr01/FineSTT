package com.rosi.masts.mvc.view.android.service

import android.content.Context
import android.content.Intent
import android.media.session.PlaybackState
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import com.rosi.masts.di.dependencyProvider
import com.rosi.masts.mvc.model.settings.Settings
import com.rosi.masts.utils.Logger
import com.rosi.masts.utils.android.getMediaController
import com.rosi.masts.utils.android.stateName


class MediaNotificationListenerService : NotificationListenerService() {
    private val tag = "MediaNotificationListenerService"
    private lateinit var logger: Logger
    private lateinit var settings: Settings

    override fun onCreate() {
        super.onCreate()
        logger = application.dependencyProvider.logger
        settings = application.dependencyProvider.settings
        logger.i(tag, "service created")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val packages = settings.getMediaApplications().map { it.packageName }
        runningMediaApplications = activeNotifications.filter { packages.contains(it.packageName) }.map { it.packageName }.toSet()
        logger.testPrint(tag, "onStartCommand, runningMediaApplications: $runningMediaApplications")
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        logger.d(tag, "onDestroy, ")
        super.onDestroy()
        logger.i(tag, "service terminated")
    }

    override fun onNotificationRemoved(notification: StatusBarNotification?) {
        val packages = settings.getMediaApplications().map { it.packageName }
        if (packages.contains(notification?.packageName)) {
            runningMediaApplications = activeNotifications.filter { packages.contains(it.packageName) }.map { it.packageName }.toSet()
            logger.testPrint(tag, "onNotificationRemoved, runningMediaApplications: $runningMediaApplications")
        }
    }

    override fun onNotificationPosted(notification: StatusBarNotification?) {
        val packages = settings.getMediaApplications().map { it.packageName }
        if (packages.contains(notification?.packageName)) {
            runningMediaApplications = activeNotifications.filter { packages.contains(it.packageName) }.map { it.packageName }.toSet()
            logger.testPrint(tag, "onNotificationPosted, runningMediaApplications: $runningMediaApplications")
        }
    }

    private fun test(notification: StatusBarNotification) {
        val mediaController = notification.getMediaController(applicationContext)
        mediaController?.let {
            logger.testPrint(tag, "test, mediaController.playbackState: ${mediaController.playbackState?.state?.stateName}")
            if (mediaController.playbackState?.state == PlaybackState.STATE_PAUSED) {
                mediaController.transportControls.play()
            }
        }
    }

    companion object {
        fun getStartIntent(context: Context): Intent {
            return Intent(context, MediaNotificationListenerService::class.java)
        }

        var runningMediaApplications: Set<String> = emptySet()
            private set
    }
}


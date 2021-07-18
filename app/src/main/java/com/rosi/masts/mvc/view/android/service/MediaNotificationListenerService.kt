package com.rosi.masts.mvc.view.android.service

import android.content.Context
import android.content.Intent
import android.media.session.PlaybackState
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import com.rosi.masts.di.dependencyProvider
import com.rosi.masts.utils.Logger
import com.rosi.masts.utils.android.getMediaController
import com.rosi.masts.utils.android.stateName


class MediaNotificationListenerService : NotificationListenerService() {
    private val tag = "MediaNotificationListenerService"
    private lateinit var logger: Logger


    override fun onCreate() {
        super.onCreate()
        logger = application.dependencyProvider.logger
        logger.i(tag, "service created")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val notification = activeNotifications.firstOrNull { it.packageName.contains("com.spotify.music") }
        isSpotifyRunning = notification != null
        logger.testPrint(tag, "onStartCommand, isSpotifyRunning: $isSpotifyRunning")
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        logger.d(tag, "onDestroy, ")
        super.onDestroy()
        logger.i(tag, "service terminated")
    }

    override fun onNotificationRemoved(notification: StatusBarNotification?) {
        if (notification?.packageName == "com.spotify.music") {
//            logger.i(tag, "Notification removed: \n ${notification.toLogString()}")

            val notification = activeNotifications.firstOrNull { it.packageName.contains("com.spotify.music") }
            isSpotifyRunning = notification != null
            logger.testPrint(tag, "onNotificationRemoved, isSpotifyRunning: $isSpotifyRunning")
        }
    }

    override fun onNotificationPosted(notification: StatusBarNotification?) {
        if (notification?.packageName == "com.spotify.music") {
//            logger.i(tag, "Notification posted: \n ${notification.toLogString()}")

            isSpotifyRunning = true
            logger.testPrint(tag, "onNotificationPosted, isSpotifyRunning: $isSpotifyRunning")
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

        var isSpotifyRunning = false
            private set
    }
}


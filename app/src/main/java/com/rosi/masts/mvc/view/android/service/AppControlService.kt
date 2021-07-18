package com.rosi.masts.mvc.view.android.service

import android.app.Notification
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.os.Process
import androidx.core.app.NotificationCompat
import com.rosi.masts.di.controller
import com.rosi.masts.di.dependencyProvider
import com.rosi.masts.utils.Consts
import com.rosi.masts.utils.Logger
import com.rosi.masts.utils.android.toPrettyString
import com.rosi.masts.mvc.view.android.notifications.MASNotification

class AppControlService : Service() {
    private val tag = "AppControlService"
    private lateinit var logger: Logger
    private lateinit var actor: AppControlServiceActor
    private var notificationBuilder: NotificationCompat.Builder? = null

    override fun onCreate() {
        Process.setThreadPriority(-16)
        Process.setThreadPriority(Process.myTid(), -16)

        super.onCreate()

        logger = application.dependencyProvider.logger
        actor = AppControlServiceActor(
            application.controller,
            application.controller.viewManager,
            "view-manager/app-control-service",
            logger,
            application.dependencyProvider.generalScope)

        logger.i(tag, "service created")

        actor.start()

        startService(MediaNotificationListenerService.getStartIntent(applicationContext))
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        logger.d(tag, "onStartCommand, intent: ${intent.toPrettyString()}")

        super.onStartCommand(intent, flags, startId)

        startForeground(MASNotification.NOTIFICATION_ID, createNotification())

        return START_STICKY
    }

    override fun onDestroy() {
        logger.d(tag, "onDestroy, ")

        actor.stop()

        stopService(MediaNotificationListenerService.getStartIntent(applicationContext))

        super.onDestroy()
        logger.i(tag, "service terminated")
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    private fun createNotification(content: String = ""): Notification? {
        return MASNotification.createNotification(this.applicationContext, "${Consts.APP_NAME} is Active", content).let { builder ->
            notificationBuilder = builder
            builder.build()
        }
    }

    companion object {
        fun getStartIntent(context: Context): Intent {
            return Intent(context, AppControlService::class.java)
        }
    }
}
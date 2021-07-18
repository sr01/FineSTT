package com.rosi.masts.mvc.view.android.notifications

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.graphics.Color
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.rosi.masts.R
import com.rosi.masts.mvc.view.android.activity.main.MainActivity
import com.rosi.masts.utils.Consts
import com.rosi.masts.utils.android.notificationManager
import java.util.*

object MASNotification {
    const val NOTIFICATION_ID = 2
    private const val channelId = Consts.NOTIFICATION_CHANNEL_ID
    private const val channelName = Consts.NOTIFICATION_CHANNEL_NAME

    fun createNotification(context: Context, title: String, content: String = ""): NotificationCompat.Builder {
        return createNotificationBuilder(title, content, context)
    }

    fun updateNotification(context: Context, builder: NotificationCompat.Builder, content: List<String>) {
        when {
            content.size > 1 -> {
                val style = content.foldRight(NotificationCompat.InboxStyle(), { nextLine, style -> style.addLine(nextLine) })
                builder.setStyle(style)
                builder.setContentText("")
            }
            else -> {
                builder.setStyle(null)
                content.firstOrNull()?.let {
                    builder.setContentText(it)
                }
            }
        }
        context.notificationManager.notify(NOTIFICATION_ID, builder.build())
    }

    private fun createNotificationBuilder(title: String, content: String, context: Context, highPriority: Boolean = false, ongoing: Boolean = false): NotificationCompat.Builder {

        val openIntent = createPendingIntent(context)

        val builder: NotificationCompat.Builder = NotificationCompat.Builder(context.applicationContext, channelId)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            setupNotificationForApi26(context, highPriority)
        } else {
            @Suppress("DEPRECATION")
            builder.priority = if (highPriority) Notification.PRIORITY_HIGH else Notification.PRIORITY_LOW
        }

        if (Build.VERSION.SDK_INT >= 21 && highPriority) {
            builder.setVibrate(kotlin.LongArray(0))
        }

        builder.setAutoCancel(false)
            .setSmallIcon(R.drawable.ic_notification)
            .setOngoing(ongoing)
            .setContentIntent(openIntent)
            .setTicker(title)
            .setContentTitle(title)
            .setContentText(content)
            .setDefaults(if (highPriority) Notification.DEFAULT_VIBRATE else 0)
            .setWhen(Calendar.getInstance().timeInMillis)
            .color = ContextCompat.getColor(context, R.color.colorAccent)

        return builder
    }

    private fun createPendingIntent(context: Context): PendingIntent? {
        return PendingIntent.getActivity(context, 0,
            MainActivity.createOpenIntent(context),
            PendingIntent.FLAG_UPDATE_CURRENT)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setupNotificationForApi26(context: Context, highPriority: Boolean) {
        val importance = if (highPriority) NotificationManager.IMPORTANCE_HIGH else NotificationManager.IMPORTANCE_LOW
        val mChannel = NotificationChannel(channelId, channelName, importance)
        if (highPriority) {
            mChannel.enableLights(true)
            mChannel.enableVibration(true)
            mChannel.lightColor = Color.RED
        }
        mChannel.lockscreenVisibility = Notification.VISIBILITY_PUBLIC
        context.notificationManager.createNotificationChannel(mChannel)
    }
}
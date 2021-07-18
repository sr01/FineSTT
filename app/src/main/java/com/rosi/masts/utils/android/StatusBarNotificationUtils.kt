package com.rosi.masts.utils.android

import android.content.Context
import android.media.session.MediaController
import android.media.session.MediaSession
import android.service.notification.StatusBarNotification

fun StatusBarNotification?.toLogString() =
    "id: ${this?.id}\n" +
            "groupKey:${this?.groupKey}\n" +
            "isOngoing:${this?.isOngoing}\n" +
            "key:${this?.key}\n" +
            "packageName:${this?.packageName}\n" +
            "postTime:${this?.postTime}\n" +
            "tag:${this?.tag}\n" +
            "user:${this?.user}\n" +
            "tickerText: ${this?.notification?.tickerText}\n" +
            "extras: ${this?.notification?.extras.getAll()}\n"

fun StatusBarNotification?.getMediaController(context: Context): MediaController? {
    return (this?.notification?.extras?.get("android.mediaSession") as MediaSession.Token?)?.let { MediaController(context, it) }
}

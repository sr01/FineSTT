package com.rosi.masts.utils.android

import com.rosi.masts.utils.DateTimeProvider
import java.util.*

object AndroidDateTimeProvider : DateTimeProvider {

    override fun currentDateTimeForFilename(): String {
        return android.text.format.DateFormat.format("yyyyMMdd_hhmmss", Date()).toString()
    }
}
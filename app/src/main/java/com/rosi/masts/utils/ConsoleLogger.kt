package com.rosi.masts.utils

import com.rosi.masts.BuildConfig
import com.rosi.masts.base.Logger
import com.rosi.masts.base.LoggerListener
import com.rosi.masts.utils.android.AndroidLogger
import java.text.SimpleDateFormat
import java.util.*

object ConsoleLogger : Logger {
    private val dateTimeFormat = SimpleDateFormat("dd/M/yyyy hh:mm:ss")
    private fun now() = dateTimeFormat.format(Calendar.getInstance().time)

    override fun e(tag: String, msg: String, e: Throwable?) {
        if (e != null)
            println("${now()} ERROR [${AndroidLogger.rootTag}.$tag] $msg $e")
        else
            println("${now()} ERROR [${AndroidLogger.rootTag}.$tag] $msg")
    }

    override fun w(tag: String, msg: String, e: Throwable?) {
        if (e != null)
            println("${now()} WARN [${AndroidLogger.rootTag}.$tag] $msg $e")
        else
            println("${now()} WARN [${AndroidLogger.rootTag}.$tag] $msg")
    }

    override fun i(tag: String, msg: String, e: Throwable?) {
        if (e != null)
            println("${now()} INFO [${AndroidLogger.rootTag}.$tag] $msg $e")
        else
            println("${now()} INFO [${AndroidLogger.rootTag}.$tag] $msg")
    }

    override fun d(tag: String, msg: String, e: Throwable?) {
        if (e != null)
            println("${now()} DEBUG [${AndroidLogger.rootTag}.$tag] $msg $e")
        else
            println("${now()} DEBUG [${AndroidLogger.rootTag}.$tag] $msg")
    }

    override fun testPrint(tag: String, message: Any) {
        if (BuildConfig.DEBUG) {
            println("${now()} [**** TEST ****] [${Thread.currentThread().id}:${Thread.currentThread().name}] [${AndroidLogger.rootTag}.$tag]  message: $message")
        }
    }

    override fun loadHistory(): List<String>? = emptyList()

    override fun addListener(listener: LoggerListener) {
    }

    override fun removeListener(listener: LoggerListener) {
    }
}
package com.rosi.masts.utils.android

import android.util.Log
import com.rosi.masts.BuildConfig
import com.rosi.masts.utils.Logger
import com.rosi.masts.utils.LoggerListener

object AndroidLogger : Logger {

    var rootTag: String = "SR"

    override fun e(tag: String, msg: String, e: Throwable?) {
        if (e != null)
            Log.e("$rootTag.$tag", msg, e)
        else
            Log.e("$rootTag.$tag", msg)
    }

    override fun w(tag: String, msg: String, e: Throwable?) {
        if (e != null)
            Log.w("$rootTag.$tag", msg, e)
        else
            Log.w("$rootTag.$tag", msg)
    }

    override fun i(tag: String, msg: String, e: Throwable?) {
        if (e != null)
            Log.i("$rootTag.$tag", msg, e)
        else
            Log.i("$rootTag.$tag", msg)
    }

    override fun d(tag: String, msg: String, e: Throwable?) {
        if (e != null)
            Log.d("$rootTag.$tag", msg, e)
        else
            Log.d("$rootTag.$tag", msg)
    }

    override fun testPrint(tag: String, message: Any) {
        if (BuildConfig.DEBUG) {
            println("[**** TEST ****] [${Thread.currentThread().id}:${Thread.currentThread().name}] [$rootTag.$tag]  message: $message")
        }
    }

    override fun loadHistory(): List<String>? = null
    override fun addListener(listener: LoggerListener) {
    }

    override fun removeListener(listener: LoggerListener) {
    }
}


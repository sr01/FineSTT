package com.rosi.masts.utils.android

import com.rosi.masts.utils.Logger
import com.rosi.masts.utils.LoggerListener

class LogCollection(private val loggers: List<Logger>) : Logger {

    override fun e(tag: String, msg: String, e: Throwable?) {
        loggers.forEach { it.e(tag, msg, e) }
    }

    override fun w(tag: String, msg: String, e: Throwable?) {
        loggers.forEach { it.w(tag, msg, e) }
    }

    override fun i(tag: String, msg: String, e: Throwable?) {
        loggers.forEach { it.i(tag, msg, e) }
    }

    override fun d(tag: String, msg: String, e: Throwable?) {
        loggers.forEach { it.d(tag, msg, e) }
    }

    override fun testPrint(tag: String, message: Any) {
        loggers.forEach { it.testPrint(tag, message) }
    }

    override fun loadHistory(): List<String>? {
        var history: List<String>? = null
        for (it in loggers) {
            history = it.loadHistory()
            if (history != null) {
                continue
            }
        }
        return history
    }

    override fun addListener(listener: LoggerListener) {
        loggers.forEach { it.addListener(listener) }
    }

    override fun removeListener(listener: LoggerListener) {
        loggers.forEach { it.removeListener(listener) }
    }
}
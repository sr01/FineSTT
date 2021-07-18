package com.rosi.masts.utils.android

import android.content.Context
import com.rosi.masts.BuildConfig
import com.rosi.masts.utils.Logger
import com.rosi.masts.utils.LoggerListener
import java.io.File
import java.io.FileReader
import java.io.FileWriter
import java.text.DateFormat
import java.util.*

class AndroidFileLogger(private val context: Context) : Logger {
    private val dateTimeFormat = DateFormat.getTimeInstance()
    private val file = File(getWorkingFolder(), "app.log")
    private val listeners = mutableListOf<LoggerListener>()

    override fun e(tag: String, msg: String, e: Throwable?) {
        if (e != null)
            write("ERROR [${AndroidLogger.rootTag}.$tag] $msg $e")
        else
            write("ERROR [${AndroidLogger.rootTag}.$tag] $msg")
    }

    override fun w(tag: String, msg: String, e: Throwable?) {
        if (e != null)
            write("WARN [${AndroidLogger.rootTag}.$tag] $msg $e")
        else
            write("WARN [${AndroidLogger.rootTag}.$tag] $msg")
    }

    override fun i(tag: String, msg: String, e: Throwable?) {
        if (e != null)
            write("INFO [${AndroidLogger.rootTag}.$tag] $msg $e")
        else
            write("INFO [${AndroidLogger.rootTag}.$tag] $msg")
    }

    override fun d(tag: String, msg: String, e: Throwable?) {
        if (e != null)
            write("DEBUG [${AndroidLogger.rootTag}.$tag] $msg $e")
        else
            write("DEBUG [${AndroidLogger.rootTag}.$tag] $msg")
    }

    override fun testPrint(tag: String, message: Any) {
        if (BuildConfig.DEBUG) {
            write("[**** TEST ****] [${Thread.currentThread().id}:${Thread.currentThread().name}] [${AndroidLogger.rootTag}.$tag]  message: $message")
        }
    }

    private fun write(msg: String) {
        val time = dateTimeFormat.format(Date())
        val fixedMessage = "$time $msg \r\n"
        var writer: FileWriter? = null

        listeners.forEach { it.onMessage(fixedMessage) }

        try {
            writer = FileWriter(file, true)
            writer.append(fixedMessage)
            writer.flush()
        } finally {
            writer?.close()
        }

        checkFileSizeLimit()
    }

    private fun checkFileSizeLimit() {
        if (file.length() > MAX_FILE_SIZE_BYTES) {
            file.delete()
        }
    }

    private fun getWorkingFolder(): File {
        val workingFolder = File(context.filesDir, "/logs")
        if (!workingFolder.exists()) {
            workingFolder.mkdirs()
        }
        return workingFolder
    }

    override fun loadHistory(): List<String> {
        return if (file.exists()) {
            val reader = FileReader(file)
            val text = reader.readLines()
            reader.close()
            text
        } else {
            emptyList()
        }
    }

    override fun addListener(listener: LoggerListener) {
        listeners.add(listener)
    }

    override fun removeListener(listener: LoggerListener) {
        listeners.remove(listener)
    }


    companion object {
        private const val MAX_FILE_SIZE_BYTES = 4000000
    }
}

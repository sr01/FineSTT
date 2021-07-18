package com.rosi.masts.utils

import com.rosi.masts.mvc.model.settings.LogLevel

object LogParser {

    fun parse(textLines: List<String>): List<Line> {
        return textLines.map {
            parse(it)
        }
    }

    fun parse(textLine: String): Line {
        return if(textLine.startWithDigit()) {
            val fields = textLine.split(delimiters = arrayOf(" "), ignoreCase = true, limit = 4)
            Line(fields[0], fields[1].toLogLevel(), fields[2], fields[3], textLine)
        }else{
            Line("", null, "", "", textLine)
        }
    }
}

private val digits = arrayListOf('0','1','2','3','4','5','6','7','8','9')
private fun String.startWithDigit(): Boolean {
    return digits.contains(this[0])
}

private fun String.toLogLevel(): LogLevel? {
    return when (this.lowercase()) {
        "info" -> LogLevel.Info
        "error" -> LogLevel.Error
        "debug" -> LogLevel.Debug
        else -> null
    }
}

data class Line(val time: String, val level: LogLevel?, val tag: String, val message: String, val fulltext: String)
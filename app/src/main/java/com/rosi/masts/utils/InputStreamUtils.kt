package com.rosi.masts.utils

import java.io.InputStream


fun InputStream.readTextWithTimeout(timeoutMillis: Long = 2000): String {
    val buffer = ByteArray(100000)
    val count = this.readWithTimeout(buffer, timeoutMillis)
    return String(buffer, 0, count)
}

fun InputStream.readWithTimeout(b: ByteArray, timeoutMillis: Long = 2000): Int {
    var bufferOffset = 0
    val maxTimeMillis = System.currentTimeMillis() + timeoutMillis
    while (System.currentTimeMillis() < maxTimeMillis && bufferOffset < b.size) {
        val readLength = Math.min(this.available(), b.size - bufferOffset)
        val readResult: Int = this.read(b, bufferOffset, readLength)
        if (readResult == -1) break
        bufferOffset += readResult
    }
    return bufferOffset
}
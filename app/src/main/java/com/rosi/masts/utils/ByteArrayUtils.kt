package com.rosi.masts.utils

import kotlin.math.min

fun ByteArray?.toHexArrayString(limit: Int = -1): String {
    return if (this == null) {
        "null"
    } else {
        take(if (limit > -1) limit else size).toByteArray().asUByteArray().joinToString(
            limit = limit,
            prefix = "[",
            postfix = "]",
            separator = ", ",
            transform = {
                "0x" + it.toString(16).padStart(2, '0').uppercase()
            })
    }
}

object ByteArrayUtils {
    fun areEquals(x: ByteArray?, xLength: Int, y: ByteArray?, yLength: Int): Boolean {

        if ((x == null && y != null) || (x != null && y == null)) {
            return false
        }

        if (x == null && y == null) {
            return true
        }


        val xMax = min(x!!.size, xLength)
        val yMax = min(y!!.size, yLength)

        println("x: ${x.toHexArrayString()}")
        println("y: ${y.toHexArrayString()}")

        if (xMax != yMax) {
            return false
        }

        for (i in 0 until xMax) {
            if (x[i] != y[i]) {
                return false
            }
        }

        return true
    }
}
package com.rosi.masts.utils.android

import android.content.Intent
import android.os.Bundle

fun Intent?.toPrettyString(): String {
    return if (this != null && this.action != null)
        "action=$action, extras=${extras.getAll()}"
    else {
        "null"
    }
}

fun Bundle?.getAll(): String {
    return this?.keySet()?.joinToString("\n") { key -> "($key -> " + if (this.get(key) != null) this.get(key) else "NULL" + ")" } ?: ""
}

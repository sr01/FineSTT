package com.rosi.masts.utils.android

import android.os.Build

object BuildUtils {
    val isAtLeast24Api: Boolean
        get() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.N
    
    val isAtLeast17Api: Boolean
        get() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1
}
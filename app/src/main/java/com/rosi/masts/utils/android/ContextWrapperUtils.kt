package com.rosi.masts.utils.android

import android.content.Context
import android.content.ContextWrapper
import android.os.LocaleList
import com.rosi.masts.utils.android.BuildUtils.isAtLeast17Api
import com.rosi.masts.utils.android.BuildUtils.isAtLeast24Api
import java.util.*

object ContextWrapperUtils {
    fun wrap(context: Context, newLocale: Locale?): ContextWrapper {
        var context = context
        val res = context.resources
        val configuration = res.configuration
        when {
            isAtLeast24Api -> {
                configuration.setLocale(newLocale)
                val localeList = LocaleList(newLocale)
                LocaleList.setDefault(localeList)
                configuration.setLocales(localeList)
                context = context.createConfigurationContext(configuration)
            }
            isAtLeast17Api -> {
                configuration.setLocale(newLocale)
                context = context.createConfigurationContext(configuration)
            }
            else -> {
                configuration.locale = newLocale
                res.updateConfiguration(configuration, res.displayMetrics)
            }
        }
        return ContextWrapper(context)
    }
}
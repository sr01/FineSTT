package com.rosi.masts.utils

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import com.rosi.masts.di.dependencyProvider
import com.rosi.masts.utils.android.ContextWrapperUtils
import java.util.*

abstract class LocaleBaseActivity : AppCompatActivity() {

    override fun attachBaseContext(newBase: Context?) {
        if (newBase != null) {
            val language = newBase.applicationContext.dependencyProvider.settings.getUILanguage()
            val context = ContextWrapperUtils.wrap(newBase, Locale(language))
            super.attachBaseContext(context)
        } else {
            super.attachBaseContext(newBase)
        }
    }
}
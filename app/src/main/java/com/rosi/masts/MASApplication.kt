package com.rosi.masts

import android.app.Application
import com.rosi.masts.di.AndroidDependencyProvider
import com.rosi.masts.di.DependencyProvider
import com.rosi.masts.mvc.control.Controller


class MASApplication : Application() {

    private val tag = "MASApplication"

    val dependencyProvider: DependencyProvider by lazy {
        AndroidDependencyProvider(this.applicationContext)
    }

    override fun onCreate() {
        super.onCreate()

        dependencyProvider.logger.d(tag, "onCreate")
    }
}
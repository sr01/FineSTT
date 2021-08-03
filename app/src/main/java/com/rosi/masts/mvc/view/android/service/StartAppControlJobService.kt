package com.rosi.masts.mvc.view.android.service

import android.app.job.JobParameters
import android.app.job.JobService
import android.content.Context
import android.os.Build
import com.rosi.masts.di.DependencyProvider
import com.rosi.masts.di.dependencyProvider
import com.rosi.masts.base.Logger

class StartAppControlJobService : JobService() {
    private lateinit var deps: DependencyProvider
    private lateinit var logger : Logger
    private val tag = "StartAppControlJobService"

    override fun onCreate() {
        super.onCreate()
        deps = application.dependencyProvider
        logger = deps.logger
    }

    override fun onStartJob(params: JobParameters?): Boolean {
        logger.d(tag, "onStartJob, service enabled: ${deps.settings.isServiceStartOnBootEnabled()}")

        if(deps.settings.isServiceStartOnBootEnabled()) {
            startAppControlService(this)
        }
        return false
    }

    override fun onStopJob(params: JobParameters?): Boolean {
        return false
    }

    private fun startAppControlService(context: Context) {

        val serviceIntent = AppControlService.getStartIntent(context)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            logger.d(tag, "startAppControlService, calling startForegroundService()")
            context.startForegroundService(serviceIntent)
        } else {
            logger.d(tag, "startAppControlService, calling startService()")
            context.startService(serviceIntent)
        }
    }
}
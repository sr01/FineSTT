package com.rosi.masts.mvc.view.android.service

import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.BroadcastReceiver
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import com.rosi.masts.MASApplication
import com.rosi.masts.di.dependencyProvider
import com.rosi.masts.utils.Logger
import com.rosi.masts.utils.android.toPrettyString

class BootBroadcastReceiver : BroadcastReceiver() {
    private val tag = "BootBroadcastReceiver"
    private val DELAY_START_JOB_ID = 1
    private val MINIMUM_DELAY_START_MILLIS = 10000L
    private val DEFAULT_DELAY_START_MILLIS = 30000L
    private var logger: Logger? = null

    override fun onReceive(context: Context, intent: Intent) {
        init(context)

        logger?.d(tag, "onReceive, intent: ${intent.toPrettyString()}")

        val delay = MINIMUM_DELAY_START_MILLIS  //TODO: move to settings
        scheduleJob(context, delay)
    }

    private fun init(context: Context) {
        if (context.applicationContext is MASApplication) {
            logger =  context.dependencyProvider.logger
        }
    }

    private fun scheduleJob(context: Context, delayMillis: Long) {
        val serviceComponent = ComponentName(context, StartAppControlJobService::class.java)
        val builder = JobInfo.Builder(DELAY_START_JOB_ID, serviceComponent)
        builder.setMinimumLatency(delayMillis)                              // wait at least
        builder.setOverrideDeadline(delayMillis * 2)                        // maximum delay
        val jobInfo: JobInfo = builder.build()
        val jobScheduler = context.getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler
        val schedule = jobScheduler.schedule(jobInfo)
        logger?.d(tag, "scheduled a job of StartAppControlJobService, result: $schedule")
    }
}

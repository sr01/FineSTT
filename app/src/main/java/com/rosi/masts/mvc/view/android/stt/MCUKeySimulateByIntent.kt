package com.rosi.masts.mvc.view.android.stt

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import com.rosi.masts.mvc.view.stt.JniToJavaActor
import com.rosi.masts.mvc.view.stt.JniToJava
import com.rosi.masts.base.Logger
import com.rosi.masts.utils.android.toPrettyString

/**
 * adb command example:
 *   adb shell am broadcast -a com.rosi.masts.test.SIMULATE_MCU_KEY_INPUT --eia key -63,1,2,3,4,5
 */
class MCUKeySimulateByIntent(private val context: Context) : JniToJava {
    private val tag = "MCUKeySimulateByIntent"
    private lateinit var logger: Logger
    private lateinit var actor: JniToJavaActor
    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            logger.testPrint(tag, "onReceive, intent: ${intent.toPrettyString()}")
            intent?.let { intent ->
                intent.extras?.let {
                    it.getIntArray("key")?.let { key ->
                        val bytes = key.map { it.toByte() }.toByteArray()
                        actor.onMcuData(bytes, bytes.size)
                    }
                }
            }
        }
    }

    override fun init(actor: JniToJavaActor, logger: Logger) {
        this.actor = actor
        this.logger = logger

        val filter = IntentFilter(SIMULATE_MCU_KEY_INPUT_ACTION)
        context.registerReceiver(receiver, filter)
    }

    fun dispose() {
        context.unregisterReceiver(receiver)
    }

    companion object {
        const val SIMULATE_MCU_KEY_INPUT_ACTION = "com.rosi.masts.test.SIMULATE_MCU_KEY_INPUT"
    }
}
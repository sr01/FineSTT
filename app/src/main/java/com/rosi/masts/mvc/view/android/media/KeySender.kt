package com.rosi.masts.mvc.view.android.media

import android.content.Context
import android.content.Intent
import android.os.SystemClock
import android.view.KeyEvent
import com.rosi.masts.base.Logger

class KeySender(private val context: Context, private val logger: Logger) {
    private val tag = "KeySender"

    fun sendKey(keycode: Int, packageName: String, className: String) {
        logger.d(tag, "sendKey, keycode: $keycode")

        val eventTime = SystemClock.uptimeMillis()

        val keyDownIntent: Intent = createMediaKeyIntent(eventTime, keycode, KeyEvent.ACTION_DOWN, packageName, className)
        keyDownIntent.addFlags(Intent.FLAG_RECEIVER_REPLACE_PENDING)
        context.sendBroadcast(keyDownIntent)

        val keyUpIntent: Intent = createMediaKeyIntent(eventTime, keycode, KeyEvent.ACTION_UP, packageName, className)
        context.sendBroadcast(keyUpIntent)

        //TODO: check if wait of 400ms is required
        val blankIntent = Intent(Intent.ACTION_MEDIA_BUTTON, null)
        val event = KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_UNKNOWN)
        blankIntent.putExtra(Intent.EXTRA_KEY_EVENT, event)
        blankIntent.addFlags(Intent.FLAG_RECEIVER_REPLACE_PENDING)
        context.sendBroadcast(blankIntent)
    }

    private fun createMediaKeyIntent(eventTime: Long, keyCode: Int, action: Int, packageName: String, className: String): Intent {
        val intent = Intent(Intent.ACTION_MEDIA_BUTTON, null)
        intent.setClassName(packageName, className)
        val event = KeyEvent(eventTime, eventTime, action, keyCode, 0)
        intent.putExtra(Intent.EXTRA_KEY_EVENT, event)
        return intent
    }
}
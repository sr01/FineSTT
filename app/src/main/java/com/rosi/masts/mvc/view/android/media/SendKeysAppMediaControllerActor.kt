package com.rosi.masts.mvc.view.android.media

import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.SystemClock
import android.view.KeyEvent
import com.rosi.masts.base.actor.Actor
import com.rosi.masts.base.actor.Message
import com.rosi.masts.base.actor.printUnknownMessage
import com.rosi.masts.mvc.InputKeyMessage
import com.rosi.masts.mvc.model.ActionTypes
import com.rosi.masts.mvc.model.media.MediaController
import com.rosi.masts.mvc.model.settings.Settings
import com.rosi.masts.mvc.view.android.service.MediaNotificationListenerService
import com.rosi.masts.mvc.view.resources.StringsProvider
import com.rosi.masts.utils.Logger
import com.rosi.masts.utils.TerminalUtils
import com.rosi.masts.utils.android.toast
import kotlinx.coroutines.CoroutineScope

abstract class SendKeysAppMediaControllerActor(private val context: Context,
                                               private val className: String,
                                               private val packageName: String,
                                               private val settings: Settings,
                                               private val stringsProvider: StringsProvider,
                                               name: String,
                                               logger: Logger,
                                               scope: CoroutineScope) : Actor(name, logger, scope), MediaController {

    private val tag = "SendKeysAppMediaControllerActor"

    override suspend fun receive(message: Message) {
        super.receive(message)

        when (message) {

            is InputKeyMessage -> if (message.action != null) onKeyAction(message.action)

            else -> printUnknownMessage(message)
        }
    }

    private suspend fun onKeyAction(action: ActionTypes) {
        logger.i(tag, "receive key action: $action")

        if (isRunning()) {
            performAction(action)
        }
    }

    private fun performAction(action: ActionTypes) {
        val keyCode = when (action) {
            ActionTypes.Next -> KeyEvent.KEYCODE_MEDIA_NEXT
            ActionTypes.Prev -> KeyEvent.KEYCODE_MEDIA_PREVIOUS
            ActionTypes.Pause -> KeyEvent.KEYCODE_MEDIA_PAUSE
            ActionTypes.Play -> KeyEvent.KEYCODE_MEDIA_PLAY
            ActionTypes.PlayPause -> KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE
        }

        if (settings.showToastMessagesForMediaActionsEnabled()) {
            val actionText = stringsProvider.getDisplayNameForKeyActionType(action)
            context.toast(actionText)
        }

        sendKey(keyCode)
    }

    private fun isRunning(): Boolean {
        return if (settings.checkIfAppIsRunningEnabled()) {
    //        val isRunning = isRunningByApp()
            val isRunning = isRunningByNotification()
            logger.testPrint(tag, "$name isRunning: $isRunning")
            return isRunning
        } else {
            logger.i(tag, "check if app is running disabled")
            true
        }
    }

    private fun isRunningByApp(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            isRunningForAPI26AndAboveUsingRoot()
        } else {
            isRunningForAPI25AndBelowByActivityManager()
        }
    }

    private fun isRunningByNotification(): Boolean {
        return MediaNotificationListenerService.isSpotifyRunning
    }

    private fun isRunningForAPI25AndBelowByActivityManager(): Boolean {
        val am = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager

        @Suppress("DEPRECATION")
        val pid = am.getRunningServices(200).firstOrNull() { pid -> pid.service.className.contains(packageName) }
        return pid != null
    }

    private fun isRunningForAPI26AndAboveUsingRoot(): Boolean {
        val running = TerminalUtils.isProcessExist(packageName)

        return if (running != null) {
            running
        } else {
            logger.i(tag, "can't determine if $name is running or not (device is probably un-rooted), assume it is running")
            true
        }
    }

    private fun sendKey(keycode: Int) {
        logger.d(tag, "sendKey, keycode: $keycode")

        val eventTime = SystemClock.uptimeMillis()

        val keyDownIntent: Intent = createMediaKeyIntent(eventTime, keycode, KeyEvent.ACTION_DOWN)
        keyDownIntent.addFlags(Intent.FLAG_RECEIVER_REPLACE_PENDING)
        context.sendBroadcast(keyDownIntent)

        val keyUpIntent: Intent = createMediaKeyIntent(eventTime, keycode, KeyEvent.ACTION_UP)
        context.sendBroadcast(keyUpIntent)

        //TODO: check if wait of 400ms is required
        val blankIntent = Intent(Intent.ACTION_MEDIA_BUTTON, null)
        val event = KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_UNKNOWN)
        blankIntent.putExtra(Intent.EXTRA_KEY_EVENT, event)
        blankIntent.addFlags(Intent.FLAG_RECEIVER_REPLACE_PENDING)
        context.sendBroadcast(blankIntent)
    }

    private fun createMediaKeyIntent(eventTime: Long, keyCode: Int, action: Int): Intent {
        val intent = Intent(Intent.ACTION_MEDIA_BUTTON, null)
        intent.setClassName(packageName, className)
        val event = KeyEvent(eventTime, eventTime, action, keyCode, 0)
        intent.putExtra(Intent.EXTRA_KEY_EVENT, event)
        return intent
    }


}
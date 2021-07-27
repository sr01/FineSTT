package com.rosi.masts.mvc.view.android.media

import android.content.Context
import android.view.KeyEvent
import com.rosi.masts.base.actor.Actor
import com.rosi.masts.base.actor.Message
import com.rosi.masts.base.actor.printUnknownMessage
import com.rosi.masts.mvc.InputKeyMessage
import com.rosi.masts.mvc.model.ActionTypes
import com.rosi.masts.mvc.model.media.MediaController
import com.rosi.masts.mvc.model.settings.Settings
import com.rosi.masts.mvc.view.resources.StringsProvider
import com.rosi.masts.utils.Logger
import com.rosi.masts.utils.VolumeControl
import com.rosi.masts.utils.android.toast
import kotlinx.coroutines.CoroutineScope

class MediaControlManager(private val context: Context,
                          private val volumeControl: VolumeControl,
                          private val settings: Settings,
                          private val stringsProvider: StringsProvider,
                          logger: Logger,
                          scope: CoroutineScope) : Actor("media-control-manager", logger, scope), MediaController {

    private val keySender = KeySender(context, logger)
    private val isAppRunningUtils = IsAppRunningUtils(context, logger)

    override suspend fun receive(message: Message) {
        super.receive(message)

        when (message) {

            is InputKeyMessage -> if (message.action != null) inputKeyMessage(message.action)

            else -> printUnknownMessage(message)
        }
    }

    private fun inputKeyMessage(action: ActionTypes) {
        notifyKeySent(action)

        if (action.isVolumeAction()) {
            performVolumeAction(action)
        } else {
            performMediaAction(action)
        }
    }

    private fun performMediaAction(action: ActionTypes) {
        val keyCode = getKeyCodeForAction(action)
        settings.getMediaApplications().forEach { app ->
            if (isRunning(app.packageName)) {
                keySender.sendKey(keyCode, app.packageName, app.className)
            }
        }
    }

    private fun performVolumeAction(action: ActionTypes) {
        when (action) {
            ActionTypes.VolumeUp -> volumeControl.volumeUp()
            ActionTypes.VolumeDown -> volumeControl.volumeDown()
            ActionTypes.Mute -> volumeControl.mute()
        }
    }

    private fun ActionTypes.isVolumeAction(): Boolean = when (this) {
        ActionTypes.Mute, ActionTypes.VolumeUp, ActionTypes.VolumeDown -> true
        else -> false
    }

    private fun notifyKeySent(action: ActionTypes) {
        val actionText = stringsProvider.getDisplayNameForKeyActionType(action)

        if (settings.showToastMessagesForMediaActionsEnabled()) {
            context.toast(actionText)
        }
    }

    private fun getKeyCodeForAction(action: ActionTypes): Int {
        return when (action) {
            ActionTypes.Next -> KeyEvent.KEYCODE_MEDIA_NEXT
            ActionTypes.Prev -> KeyEvent.KEYCODE_MEDIA_PREVIOUS
            ActionTypes.Pause -> KeyEvent.KEYCODE_MEDIA_PAUSE
            ActionTypes.Play -> KeyEvent.KEYCODE_MEDIA_PLAY
            ActionTypes.PlayPause -> KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE
            ActionTypes.VolumeUp -> KeyEvent.KEYCODE_VOLUME_UP
            ActionTypes.VolumeDown -> KeyEvent.KEYCODE_VOLUME_DOWN
            ActionTypes.Mute -> KeyEvent.KEYCODE_VOLUME_MUTE
        }
    }

    private fun isRunning(packageName: String): Boolean {
        return if (settings.checkIfAppIsRunningEnabled()) {
            isAppRunningUtils.isRunning(packageName)
        } else {
            true
        }
    }
}
package com.rosi.masts.mvc.view.android.activity.keybinding

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.rosi.masts.mvc.model.ActionTypes
import com.rosi.masts.mvc.view.KeyBindingActivityActor
import com.rosi.masts.base.Logger

class KeyBindingViewModel(private val actor: KeyBindingActivityActor, private val logger: Logger) : ViewModel(),
    KeyBindingActivityActor.Listener {

    private val tag = "KeyBindingViewModel"
    val notifications = MutableLiveData<ViewNotification>()
    val availableActions = MutableLiveData<Collection<ActionTypes>>()
    val detectedKey = MutableLiveData<DetectedKey>()

    init {
        logger.testPrint(tag, "init, ")
        actor.addListener(this)
    }

    override fun onCleared() {
        super.onCleared()
        actor.stopBinding()
        actor.removeListener(this)
    }

    override fun onSelectAction(availableActions: Collection<ActionTypes>) {
        logger.testPrint(tag, "onSelectAction, ")
        notifications.postValue(ViewNotification.ShowSelectAction)
    }

    override fun onSelectKey() {
        logger.testPrint(tag, "onSelectKey, ")
        notifications.postValue(ViewNotification.ShowSelectKey)
    }

    override fun onKeyDetected(key: String, keyDetectedCount: Int) {
        logger.testPrint(tag, "onKeyDetected, ")
        detectedKey.postValue(DetectedKey(key, keyDetectedCount))
    }

    override fun onBindSuccess() {
        logger.testPrint(tag, "onBindSuccess, ")
    }

    override fun onBindComplete() {
        logger.testPrint(tag, "onBindComplete, ")
        notifications.postValue(ViewNotification.Close)
    }

    override fun onAvailableActions(actions: Collection<ActionTypes>) {
        logger.testPrint(tag, "onAvailableActions, ")
        availableActions.postValue(actions)
    }

    fun startBinding() {
        logger.testPrint(tag, "startBinding, ")
        actor.startBinding()
    }

    fun startBindingForBindingID(bindingID: String) {
        logger.testPrint(tag, "startBindingForBindingID, ")
        actor.startBindingForBindingID(bindingID)
    }

}

sealed class ViewNotification {
    object ShowSelectAction : ViewNotification()
    object ShowSelectKey : ViewNotification()
    object Close  : ViewNotification()
}

data class DetectedKey(val key: String, val keyDetectedCount: Int)
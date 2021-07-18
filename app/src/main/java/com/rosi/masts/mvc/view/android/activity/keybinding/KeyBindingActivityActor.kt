package com.rosi.masts.mvc.view.android.activity.keybinding

import com.rosi.masts.base.actor.*
import com.rosi.masts.mvc.*
import com.rosi.masts.mvc.control.Controller
import com.rosi.masts.mvc.model.ActionTypes
import com.rosi.masts.mvc.model.mcu.MCUInputKey
import com.rosi.masts.utils.Logger
import kotlinx.coroutines.CoroutineScope


class KeyBindingActivityActor(private val controller: Controller, name: String, logger: Logger, scope: CoroutineScope) : Actor(name, logger, scope) {

    private var listeners = mutableListOf<Listener>()
    private var availableActions: Collection<ActionTypes>? = null
    private var keyDetectedCount = 0

    override suspend fun receive(message: Message) {
        super.receive(message)

        when (message) {
            is AddListenerMessage<*> -> if (message.listener is Listener) listeners.add(message.listener)
            is RemoveListenerMessage<*> -> if (message.listener is Listener) listeners.remove(message.listener)
            is SelectActionMessage -> if (message.availableActions != null) selectActionMessage(message.availableActions)
            is SelectKeyMessage -> selectKeyMessage()
            is KeyDetectedMessage -> if (message.key != null) keyDetectedMessage(message.key)
            is BindSuccessMessage -> bindSuccessMessage()
            is GetAvailableActionsMessage -> if (message.actions != null) getAvailableActionsMessage(message.actions)
            else -> printUnknownMessage(message)
        }
    }

    private fun selectKeyMessage() {
        keyDetectedCount = 0
        listeners.forEach { it.onSelectKey() }
    }

    private fun selectActionMessage(availableActions: Collection<ActionTypes>) {
        this.availableActions = availableActions
        listeners.forEach { it.onSelectAction(availableActions) }
    }

    private fun bindSuccessMessage() {
        listeners.forEach { it.onBindSuccess() }

        listeners.forEach { it.onBindComplete() }
    }

    private fun keyDetectedMessage(key: MCUInputKey) {
        keyDetectedCount++
        listeners.forEach { it.onKeyDetected(key.toString(), keyDetectedCount) }
    }

    private fun getAvailableActionsMessage(actions: Collection<ActionTypes>) {
        listeners.forEach { it.onAvailableActions(actions) }
    }

    fun addListener(listener: Listener) {
        this send AddListenerMessage(listener = listener) to this
    }

    fun removeListener(listener: Listener) {
        this send RemoveListenerMessage(listener = listener) to this
    }

    fun startBinding() {
        this send StartKeyBindingMessage() to controller
    }

    fun startBindingForBindingID(bindingID: String) {
        this send StartKeyBindingForActionMessage(bindingID = bindingID) to controller
    }

    fun actionSelected(action: ActionTypes) {
        this send ActionSelectedMessage(action = action) to controller
    }

    fun keySelected() {
        this send KeySelectedMessage() to controller
    }

    fun stopBinding() {
        this send StopKeyBindingMessage() to controller
    }

    fun getAvailableActions() {
        this send GetAvailableActionsMessage(recipient = this) to controller
    }

    interface Listener {
        fun onSelectAction(availableActions: Collection<ActionTypes>)
        fun onSelectKey()
        fun onKeyDetected(key: String, keyDetectedCount: Int)
        fun onBindSuccess()
        fun onBindComplete()
        fun onAvailableActions(actions: Collection<ActionTypes>)
    }
}


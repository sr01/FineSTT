package com.rosi.masts.mvc.view.android.activity.main

import com.rosi.masts.base.actor.Actor
import com.rosi.masts.base.actor.Message
import com.rosi.masts.base.actor.printUnknownMessage
import com.rosi.masts.base.actor.send
import com.rosi.masts.mvc.*
import com.rosi.masts.mvc.control.Controller
import com.rosi.masts.mvc.model.keybinding.KeyActionBinding
import com.rosi.masts.mvc.view.resources.StringsProvider
import com.rosi.masts.mvc.view.ViewManager
import com.rosi.masts.mvc.view.android.activity.keybinding.ActionViewData
import com.rosi.masts.mvc.view.android.activity.keybinding.ActionWithMultipleKeysViewData
import com.rosi.masts.utils.Logger
import kotlinx.coroutines.CoroutineScope

class MainActivityActor(
    private val controller: Controller,
    private val viewManager: ViewManager,
    private val stringsProvider: StringsProvider,
    name: String,
    logger: Logger,
    scope: CoroutineScope) : Actor(name, logger, scope) {

    private var listeners = mutableListOf<Listener>()

    override suspend fun receive(message: Message) {
        super.receive(message)

        when (message) {
            is GetServiceStatus -> if (message.isRunning != null) getServiceStatus(message.isRunning)
            is GetKeyActionBindingsMessage -> if (message.bindings != null) {
                getKeyActionBindingsMessage(message.bindings)
            } else {
                logger.testPrint(tag, "receive, GetKeyActionBindingsMessage with no bindings")
            }
            is ServiceStatusChanged -> serviceStatusChanged(message.isRunning)
            is AddListenerMessage<*> -> if (message.listener is Listener) listeners.add(message.listener)
            is RemoveListenerMessage<*> -> if (message.listener is Listener) listeners.remove(message.listener)
            is RemoveKeyActionBindingMessage -> if (message.removedBindings != null) removeKeyActionBindingMessage(message, message.removedBindings)
            else -> printUnknownMessage(message)
        }
    }

    fun addListener(listener: Listener) {
        this send AddListenerMessage(listener = listener) to this
        this send GetKeyActionBindingsMessage(recipient = this) to controller
        this send GetServiceStatus(recipient = this) to viewManager
    }

    fun removeListener(listener: Listener) {
        this send RemoveListenerMessage(listener = listener) to this
    }

    fun unbindAction(actionViewData: ActionViewData) {
        actionViewData.bindingID?.let { bindingID ->
            this send RemoveKeyActionBindingMessage(bindingID = bindingID) to controller
        }
    }

    private val tag = "MainActivityActor"
    private fun getServiceStatus(isRunning: Boolean) {
        logger.testPrint(tag, "getServiceStatus, listeners: ${listeners.size}")
        listeners.forEach { it.onServiceStatusChanged(isRunning) }
    }

    private fun serviceStatusChanged(isRunning: Boolean) {
        logger.testPrint(tag, "serviceStatusChanged, listeners: ${listeners.size}")
        listeners.forEach { it.onServiceStatusChanged(isRunning) }
    }

    private fun getKeyActionBindingsMessage(bindings: Collection<KeyActionBinding>) {

        val keyActionViewDataList = bindings.map { binding ->
            ActionViewData(
                bindingID = binding.id,
                displayName = stringsProvider.getDisplayNameForKeyActionType(binding.actionType),
                action = binding.actionType,
                isSelected = false,
                boundKeyName = binding.key.displayName)
        }

        val actionViewMultipleKeysData = keyActionViewDataList.groupBy { it.action }
            .map {
                ActionWithMultipleKeysViewData(
                    action = it.key,
                    displayName = stringsProvider.getDisplayNameForKeyActionType(it.key),
                    keys = it.value.toMutableList())
            }

        listeners.forEach { it.onShowActions(actionViewMultipleKeysData) }
    }

    private fun removeKeyActionBindingMessage(message: RemoveKeyActionBindingMessage, removedBindings: Collection<KeyActionBinding>) {
        listeners.forEach { listener ->
            val removedBindingIDs = removedBindings.map { it.id }
            listener.onActionsRemoved(removedBindingIDs)
        }
    }

    interface Listener {
        fun onServiceStatusChanged(isRunning: Boolean)
        fun onShowActions(actions: Collection<ActionWithMultipleKeysViewData>)
        fun onActionsRemoved(bindingsIDs: Collection<String>)
    }
}
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
import com.rosi.masts.utils.Logger
import kotlinx.coroutines.CoroutineScope
import java.io.File

class MainActivityActor(private val controller: Controller, private val viewManager: ViewManager, name: String, logger: Logger, scope: CoroutineScope) : Actor(name, logger, scope) {

    private val tag = "MainActivityActor"
    private var listeners = mutableListOf<Listener>()

    override suspend fun receive(message: Message) {
        super.receive(message)

        when (message) {
            is GetServiceStatus -> if (message.isRunning != null) getServiceStatus(message.isRunning)
            is GetKeyActionBindingsMessage -> if (message.bindings != null) showActions(message.bindings)
            is ServiceStatusChanged -> serviceStatusChanged(message.isRunning)
            is AddListenerMessage<*> -> if (message.listener is Listener) listeners.add(message.listener)
            is RemoveListenerMessage<*> -> if (message.listener is Listener) listeners.remove(message.listener)
            is RemoveKeyActionBindingMessage -> if (message.removedBindings != null) removeKeyActionBindingMessage(message, message.removedBindings)
            is ImportKeyBindingsMessage -> if (message.bindings != null) showActions(message.bindings)
            is ShareKeyBindingsMessage -> if (message.bindingsJson != null) shareKeyBindingsMessage(message.bindingsJson)
            is BindSuccessMessage -> bindSuccessMessage(message.keyActionBinding)
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

    fun shareBindings() {
        this send ShareKeyBindingsMessage() to controller
    }

    fun exportBindings() {
        this send ExportKeyBindingsMessage() to controller
    }

    fun importBindings(file: File) {
        this send ImportKeyBindingsMessage(file = file) to controller
    }

    private fun getServiceStatus(isRunning: Boolean) {
        logger.testPrint(tag, "getServiceStatus, listeners: ${listeners.size}")
        listeners.forEach { it.onServiceStatusChanged(isRunning) }
    }

    private fun serviceStatusChanged(isRunning: Boolean) {
        logger.testPrint(tag, "serviceStatusChanged, listeners: ${listeners.size}")
        listeners.forEach { it.onServiceStatusChanged(isRunning) }
    }

    private fun showActions(bindings: Collection<KeyActionBinding>) {
        listeners.forEach { it.onShowBindings(bindings) }
    }

    private fun removeKeyActionBindingMessage(message: RemoveKeyActionBindingMessage, removedBindings: Collection<KeyActionBinding>) {
        listeners.forEach { listener ->
            val removedBindingIDs = removedBindings.map { it.id }
            listener.onBindingsRemoved(removedBindingIDs)
        }
    }

    private fun shareKeyBindingsMessage(bindingsJson: String) {
        listeners.forEach { it.onShareBindings(bindingsJson) }
    }

    private fun bindSuccessMessage(keyActionBinding: KeyActionBinding) {
        listeners.forEach { it.onBindingUpdated(keyActionBinding) }
    }

    interface Listener {
        fun onServiceStatusChanged(isRunning: Boolean)
        fun onShowBindings(bindings: Collection<KeyActionBinding>)
        fun onBindingsRemoved(bindingsIDs: Collection<String>)
        fun onBindingUpdated(binding: KeyActionBinding)
        fun onShareBindings(bindingsJson: String)
    }
}
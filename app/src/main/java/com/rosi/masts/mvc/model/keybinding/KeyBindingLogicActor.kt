package com.rosi.masts.mvc.model.keybinding

import com.rosi.masts.base.actor.*
import com.rosi.masts.mvc.*
import com.rosi.masts.mvc.control.Controller
import com.rosi.masts.mvc.model.*
import com.rosi.masts.mvc.model.ActionTypes
import com.rosi.masts.mvc.model.mcu.MCUInputKey
import com.rosi.masts.base.Logger
import com.rosi.masts.utils.ifNotNull
import kotlinx.coroutines.CoroutineScope

class KeyBindingLogicActor(private val controller: Controller,
                           private val modelManager: ModelManager,
                           name: String,
                           logger: Logger,
                           scope: CoroutineScope) : Actor(name, logger, scope) {

    private var selectedBindingId: String? = null
    private var state: States = States.Done
    private var selectedAction: ActionTypes? = null
    private var selectedKeyActionBinding: KeyActionBinding? = null
    private var selectedKey: MCUInputKey? = null

    override suspend fun receive(message: Message) {
        super.receive(message)

        when (message) {
            is StartKeyBindingMessage -> if (state == States.Done) startKeyBindingMessage()
            is StartKeyBindingForActionMessage -> if (state == States.Done && message.keyActionBinding != null) startKeyBindingForActionMessage(message.keyActionBinding)
            is ActionSelectedMessage -> if (state == States.SelectAction) actionSelectedMessage(message.action)
            is KeySelectedMessage -> if (state == States.SelectKey && message.key != null) keySelectedMessage(message.key)
            is StopKeyBindingMessage -> stopKeyBindingMessage()
        }
    }

    private suspend fun startKeyBindingMessage() {
        state = States.SelectAction
        selectedAction = null
        selectedKey = null

        this send SelectActionMessage() to modelManager
    }

    private fun startKeyBindingForActionMessage(keyActionBinding: KeyActionBinding) {
        state = States.SelectKey
        selectedAction = keyActionBinding.actionType
        selectedBindingId = keyActionBinding.id
        selectedKey = null
        this send SelectKeyMessage() to controller
    }

    private fun actionSelectedMessage(action: ActionTypes) {
        state = States.SelectKey
        selectedAction = action
        this send SelectKeyMessage() to controller
    }

    private fun keySelectedMessage(key: MCUInputKey) {
        state = States.Binding
        selectedKey = key

        ifNotNull(selectedKey, selectedAction) { key, action ->
            this send AddOrUpdateKeyActionBindingMessage(key = key, action =  action, bindingID = selectedBindingId) to modelManager
            state = States.Done
            selectedBindingId = null
        }
    }

    private fun stopKeyBindingMessage() {
        state = States.Done
    }

    enum class States {
        SelectAction,
        SelectKey,
        Binding,
        Done
    }
}


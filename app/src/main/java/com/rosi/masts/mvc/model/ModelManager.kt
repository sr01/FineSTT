package com.rosi.masts.mvc.model

import com.rosi.masts.base.actor.Actor
import com.rosi.masts.base.actor.Message
import com.rosi.masts.base.actor.printUnknownMessage
import com.rosi.masts.base.actor.send
import com.rosi.masts.di.DependencyProvider
import com.rosi.masts.mvc.*
import com.rosi.masts.mvc.control.Controller
import com.rosi.masts.mvc.model.keybinding.*
import com.rosi.masts.mvc.model.mcu.InputKeyLogicActor

class ModelManager(controller: Controller, override val name: String, deps: DependencyProvider) : Actor(name, deps.logger, deps.generalScope) {

    private val keyBindingStorageActor = KeyBindingStorageActor(controller, this, deps.settings, deps.textFileReadWrite, deps.dateTimeProvider, "model-manager/key-binding-storage", deps.logger, deps.ioScope)
    val keyBindingLogicActor = KeyBindingLogicActor(controller, this, "model-manager/key-binding-actor", logger, deps.generalScope)
    private val inputKeyLogicActor = InputKeyLogicActor(controller, this, "model-manager/input-key-logic", logger, deps.generalScope)

    override suspend fun receive(message: Message) {
        super.receive(message)

        when (message) {

            is StartInputKeyListening -> this send message to inputKeyLogicActor
            is StopInputKeyListening -> this send message to inputKeyLogicActor
            is StartKeyBindingMessage -> {
                this send message to inputKeyLogicActor
                this send message to keyBindingLogicActor
            }
            is StartKeyBindingForActionMessage -> when (message.keyActionBinding) {
                null -> {
                    this send message to inputKeyLogicActor
                    this send message to keyBindingStorageActor
                }
                else -> this send message to keyBindingLogicActor
            }
            is ActionSelectedMessage -> this send message to keyBindingLogicActor
            is KeySelectedMessage -> when (message.key) {
                null -> this send message to inputKeyLogicActor
                else -> this send message to keyBindingLogicActor
            }
            is BindSuccessMessage -> this send message to inputKeyLogicActor
            is StopKeyBindingMessage -> {
                this send message to inputKeyLogicActor
                this send message to keyBindingLogicActor
            }
            is InputKeyMessage -> when (message.key) {
                null -> this send message to inputKeyLogicActor
                else -> this send message to keyBindingStorageActor
            }
            is GetKeyActionBindingsMessage -> this send message to keyBindingStorageActor
            is RemoveKeyActionBindingMessage -> this send message to keyBindingStorageActor
            is GetAvailableActionsMessage -> this send message to keyBindingStorageActor
            is SelectActionMessage -> this send message to keyBindingStorageActor
            is AddOrUpdateKeyActionBindingMessage -> this send message to keyBindingStorageActor

            is ExportKeyBindingsMessage -> this send message to keyBindingStorageActor
            is ImportKeyBindingsMessage -> this send message to keyBindingStorageActor
            is ShareKeyBindingsMessage -> this send message to keyBindingStorageActor

            else -> printUnknownMessage(message)
        }
    }
}


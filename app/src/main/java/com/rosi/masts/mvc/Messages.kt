package com.rosi.masts.mvc

import com.rosi.masts.base.actor.DefaultSender
import com.rosi.masts.base.actor.ISender
import com.rosi.masts.base.actor.Message
import com.rosi.masts.mvc.model.keybinding.KeyActionBinding
import com.rosi.masts.mvc.model.ActionTypes
import com.rosi.masts.mvc.model.mcu.MCUInputKey
import java.io.File

data class InputKeyMessage(override val sender: ISender = DefaultSender,
                           val data: ByteArray,
                           val length: Int,
                           val key: MCUInputKey? = null,
                           val action: ActionTypes? = null) : Message {
    override fun withSender(sender: ISender) = this.copy(sender = sender)
    fun withKey(key: MCUInputKey) = this.copy(key = key)
    fun withAction(action: ActionTypes?) = this.copy(action = action)
}

data class StartInputKeyListening(override val sender: ISender = DefaultSender) : Message {
    override fun withSender(sender: ISender) = this.copy(sender = sender)
}

data class StopInputKeyListening(override val sender: ISender = DefaultSender) : Message {
    override fun withSender(sender: ISender) = this.copy(sender = sender)
}

data class StartKeyBindingMessage(override val sender: ISender = DefaultSender) : Message {
    override fun withSender(sender: ISender) = this.copy(sender = sender)
}

data class StartKeyBindingForActionMessage(override val sender: ISender = DefaultSender, val bindingID: String, val keyActionBinding: KeyActionBinding? = null) : Message {
    override fun withSender(sender: ISender) = this.copy(sender = sender)
    fun withKeyActionBinding(keyActionBinding: KeyActionBinding) = this.copy(keyActionBinding = keyActionBinding)
}

data class StopKeyBindingMessage(override val sender: ISender = DefaultSender) : Message {
    override fun withSender(sender: ISender) = this.copy(sender = sender)
}

data class KeyDetectedMessage(override val sender: ISender = DefaultSender, val key: MCUInputKey) : Message {
    override fun withSender(sender: ISender) = this.copy(sender = sender)
}

data class AddListenerMessage<T>(override val sender: ISender = DefaultSender, val listener: T) : Message {
    override fun withSender(sender: ISender) = this.copy(sender = sender)
}

data class RemoveListenerMessage<T>(override val sender: ISender = DefaultSender, val listener: T) : Message {
    override fun withSender(sender: ISender) = this.copy(sender = sender)
}

data class ServiceStatusChanged(override val sender: ISender = DefaultSender, val isRunning: Boolean) : Message {
    override fun withSender(sender: ISender) = this.copy(sender = sender)
}

data class AddOrUpdateKeyActionBindingMessage(override val sender: ISender = DefaultSender, val key: MCUInputKey, val action: ActionTypes, val bindingID: String? = null) : Message {
    override fun withSender(sender: ISender) = this.copy(sender = sender)
}

data class RemoveKeyActionBindingMessage(override val sender: ISender = DefaultSender, val bindingID: String, val removedBindings: Collection<KeyActionBinding>? = null) : Message {
    override fun withSender(sender: ISender) = this.copy(sender = sender)
    fun withRemovedBindings(removedBindings: Collection<KeyActionBinding>) = this.copy(removedBindings = removedBindings)
}

data class GetKeyActionBindingsMessage(override val sender: ISender = DefaultSender, val bindings: Collection<KeyActionBinding>? = null, val recipient: ISender) : Message {
    override fun withSender(sender: ISender) = this.copy(sender = sender)
    fun withActions(bindings: Collection<KeyActionBinding>) = this.copy(bindings = bindings)
}

data class GetAvailableActionsMessage(override val sender: ISender = DefaultSender, val actions: Collection<ActionTypes>? = null, val recipient: ISender) : Message {
    override fun withSender(sender: ISender) = this.copy(sender = sender)
    fun withAvailableActions(actions: Collection<ActionTypes>) = this.copy(actions = actions)
}

data class GetServiceStatus(override val sender: ISender = DefaultSender, val isRunning: Boolean? = null, val recipient: ISender) : Message {
    override fun withSender(sender: ISender) = this.copy(sender = sender)
    fun withServiceStatus(isRunning: Boolean) = this.copy(isRunning = isRunning)
}

data class SelectActionMessage(override val sender: ISender = DefaultSender, val availableActions: Collection<ActionTypes>? = null) : Message {
    override fun withSender(sender: ISender) = this.copy(sender = sender)
    fun withAvailableActions(availableActions: Collection<ActionTypes>) = this.copy(availableActions = availableActions)
}

data class SelectKeyMessage(override val sender: ISender = DefaultSender) : Message {
    override fun withSender(sender: ISender) = this.copy(sender = sender)
}

data class ActionSelectedMessage(override val sender: ISender = DefaultSender, val action: ActionTypes) : Message {
    override fun withSender(sender: ISender) = this.copy(sender = sender)
}

data class KeySelectedMessage(override val sender: ISender = DefaultSender, val key: MCUInputKey? = null) : Message {
    override fun withSender(sender: ISender) = this.copy(sender = sender)
    fun withKey(key: MCUInputKey) = this.copy(key = key)
}

data class BindSuccessMessage(override val sender: ISender = DefaultSender, val keyActionBinding: KeyActionBinding) : Message {
    override fun withSender(sender: ISender) = this.copy(sender = sender)
}

data class ExportKeyBindingsMessage(override val sender: ISender = DefaultSender) : Message {
    override fun withSender(sender: ISender) = this.copy(sender = sender)
}

data class ImportKeyBindingsMessage(override val sender: ISender = DefaultSender, val file: File, val bindings: Collection<KeyActionBinding>? = null) : Message {
    override fun withSender(sender: ISender) = this.copy(sender = sender)
    fun withActions(bindings: Collection<KeyActionBinding>) = this.copy(bindings = bindings)
}

data class ShareKeyBindingsMessage(override val sender: ISender = DefaultSender, val bindingsJson: String? = null) : Message {
    override fun withSender(sender: ISender) = this.copy(sender = sender)
    fun withBindingsJson(bindingsJson: String) = this.copy(bindingsJson = bindingsJson)
}

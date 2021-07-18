package com.rosi.masts.mvc.model.mcu

import com.rosi.masts.base.actor.*
import com.rosi.masts.mvc.*
import com.rosi.masts.mvc.control.Controller
import com.rosi.masts.mvc.model.ModelManager
import com.rosi.masts.utils.Logger
import com.rosi.masts.utils.toHexArrayString
import kotlinx.coroutines.CoroutineScope

class InputKeyLogicActor(
    private val controller: Controller,
    private val modelManager: ModelManager,
    name: String,
    logger: Logger,
    scope: CoroutineScope) : Actor(name, logger, scope) {

    private var state: State = State.Off
        set(value) {
            logger.d(tag, "state change from $field to $value")
            field = value
        }

    private var lastKey: MCUInputKey? = null
    private val tag = "InputKeyLogicActor"

    override suspend fun receive(message: Message) {
        super.receive(message)

        when (message) {
            is StartInputKeyListening -> startInputKeyListening()
            is StopInputKeyListening -> stopInputKeyListening()
            is StartKeyBindingMessage -> startKeyBindingMessage()
            is StartKeyBindingForActionMessage -> startKeyBindingMessage()
            is InputKeyMessage -> inputKeyMessage(message, message.data, message.length)
            is KeySelectedMessage -> keySelectedMessage(message)
            is BindSuccessMessage -> bindCompleteMessage(message)
            is StopKeyBindingMessage -> stopKeyBindingMessage(message)
            else -> printUnknownMessage(message)
        }
    }

    private fun startInputKeyListening() {
        if (state is State.Off) {
            state = State.Listening(state)
        }
    }

    private fun stopInputKeyListening() {
        if (state is State.Listening) {
            state = State.Off
        }
    }

    private fun startKeyBindingMessage() {
        state = State.Binding(state)
    }

    private fun stopKeyBindingMessage(message: StopKeyBindingMessage) {
        restorePreviousState(message)
    }

    private fun inputKeyMessage(message: InputKeyMessage, data: ByteArray, length: Int) {
        logger.testPrint(tag, "inputKeyMessage, data: ${data.toHexArrayString()}, length: $length")
        val key = MCUInputKey.fromBytes(data, length)
        logger.testPrint(tag, "inputKeyMessage, state: $state, key: $key")

        when (state) {
            is State.Off -> {
                //do nothing
            }
            is State.Listening -> {
                this send message.withKey(key) to modelManager
            }
            is State.Binding -> {
                this send KeyDetectedMessage(key = key) to controller
            }
        }

        lastKey = key

    }

    private fun keySelectedMessage(message: KeySelectedMessage) {
        lastKey?.let { lastKey ->
            this send message.withKey(lastKey) to modelManager
        }
    }

    private fun bindCompleteMessage(message: BindSuccessMessage) {
        restorePreviousState(message)

        this send message to controller
    }

    private fun restorePreviousState(message: Message) {
        state.let { currentState ->
            if (currentState is State.Binding) {

                when (currentState.previousState) {
                    is State.Binding -> {
                        logger.e(tag, "invalid state, current: $currentState, previous: ${currentState.previousState}, message: $message")
                        this.state = State.Off
                    }
                    is State.Listening -> {
                        this.state = currentState.previousState
                    }
                    is State.Off -> {
                        this.state = currentState.previousState
                    }
                }
            }
        }
    }

    sealed class State {
        object Off : State() {
            override fun toString(): String = "Off"
        }

        data class Listening(val previousState: State) : State()
        data class Binding(val previousState: State) : State()
    }
}


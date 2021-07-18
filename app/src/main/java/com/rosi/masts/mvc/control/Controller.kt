package com.rosi.masts.mvc.control

import com.rosi.masts.base.actor.Actor
import com.rosi.masts.base.actor.Message
import com.rosi.masts.base.actor.printUnknownMessage
import com.rosi.masts.base.actor.send
import com.rosi.masts.di.DependencyProvider
import com.rosi.masts.mvc.model.ModelManager
import com.rosi.masts.mvc.view.ViewManager

class Controller(deps: DependencyProvider) : Actor("controller", deps.logger, deps.generalScope) {

    private val tag = "Controller"
    private val viewManagerName = "view-manager"
    private val modelManagerName = "model-manager"
    val modelManager = ModelManager(this, modelManagerName, deps)
    val viewManager = ViewManager(this, viewManagerName, deps)

    init {
        logger.d(tag, "init")
    }

    override suspend fun receive(message: Message) {
        super.receive(message)

        when {
            message.sender.name.contains(modelManagerName) -> {
                messageFromModel(message)
            }
            message.sender.name.contains(viewManagerName) -> {
                messageFromView(message)
            }
            else -> {
                printUnknownMessage(message)
            }
        }
    }

    private suspend fun messageFromView(message: Message) {
        this send message to modelManager
    }

    private suspend fun messageFromModel(message: Message) {
        this send message to viewManager
    }
}
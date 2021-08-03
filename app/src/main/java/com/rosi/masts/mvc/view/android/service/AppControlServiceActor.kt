package com.rosi.masts.mvc.view.android.service

import com.rosi.masts.base.actor.Actor
import com.rosi.masts.base.actor.Message
import com.rosi.masts.base.actor.printUnknownMessage
import com.rosi.masts.base.actor.send
import com.rosi.masts.mvc.ServiceStatusChanged
import com.rosi.masts.mvc.StartInputKeyListening
import com.rosi.masts.mvc.StopInputKeyListening
import com.rosi.masts.mvc.control.Controller
import com.rosi.masts.mvc.view.ViewManager
import com.rosi.masts.base.Logger
import kotlinx.coroutines.CoroutineScope

class AppControlServiceActor(
    private val controller: Controller,
    private val viewManager: ViewManager,
    name: String,
    logger: Logger,
    scope: CoroutineScope) : Actor(name, logger, scope) {

    override suspend fun receive(message: Message) {
        super.receive(message)

        when (message) {
            else -> printUnknownMessage(message)
        }
    }

    fun start() {
        this send ServiceStatusChanged(isRunning = true) to viewManager

        this send StartInputKeyListening() to controller
    }

    fun stop() {
        this send ServiceStatusChanged(isRunning = false) to viewManager

        this send StopInputKeyListening() to controller
    }
}
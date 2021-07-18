package com.rosi.masts.test

import com.rosi.masts.base.actor.Actor
import com.rosi.masts.base.actor.Message
import com.rosi.masts.utils.ConsoleLogger
import kotlinx.coroutines.test.TestCoroutineScope
import java.util.concurrent.LinkedBlockingQueue

class TestActor : Actor("test-actor", ConsoleLogger, TestCoroutineScope()) {
    val messages = LinkedBlockingQueue<Message>()

    override suspend fun receive(message: Message) {
        super.receive(message)

        messages.put(message)

        when (message) {
            else -> logger.d(name, "RECEIVE MESSAGE: $message")
        }
    }
}
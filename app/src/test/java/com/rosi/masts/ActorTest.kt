package com.rosi.masts

import com.rosi.masts.base.actor.*
import com.rosi.masts.utils.ConsoleLogger
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Test

class ActorTest {

    @Test
    fun `cancel scheduled message`() = runBlockingTest {
        val receivedMessages = mutableListOf<Message>()

        val x = object : Actor("x", ConsoleLogger, this) {
            override suspend fun receive(message: Message) {
                super.receive(message)

                when (message) {

                }
            }
        }

        val y = object : Actor("y", ConsoleLogger, this) {
            override suspend fun receive(message: Message) {
                super.receive(message)

                when (message) {
                    is PingMessage -> receivedMessages.add(message)
                }
            }
        }

        x.scheduleMessage(PingMessage(), y, 3000)
        x.cancelScheduledMessage(PingMessage())

        this.advanceTimeBy(5000)

        assertEquals(0, receivedMessages.size)

        x.actor.close()
        y.actor.close()
    }

    @Test
    fun `schedule the same message twice`() = runBlockingTest {

        val receivedMessages = mutableListOf<Message>()

        val x = object : Actor("x", ConsoleLogger, this) {
            override suspend fun receive(message: Message) {
                super.receive(message)

                when (message) {

                }
            }
        }

        val y = object : Actor("y", ConsoleLogger, this) {
            override suspend fun receive(message: Message) {
                super.receive(message)

                when (message) {
                    is PingMessage -> receivedMessages.add(message)
                }
            }
        }

        x.scheduleMessage(PingMessage(), y, 3000)
        advanceTimeBy(1000)

        x.scheduleMessage(PingMessage(), y, 3000)
        advanceTimeBy(5000)

        assertEquals(1, receivedMessages.size)

        x.actor.close()
        y.actor.close()
    }

    @Test
    fun `schedule the same message twice and one other message`() = runBlockingTest {

        val receivedMessages = mutableListOf<Message>()

        val x = object : Actor("x", ConsoleLogger, this) {
            override suspend fun receive(message: Message) {
                super.receive(message)

                when (message) {

                }
            }
        }

        val y = object : Actor("y", ConsoleLogger, this) {
            override suspend fun receive(message: Message) {
                super.receive(message)

                receivedMessages.add(message)
            }
        }

        x.scheduleMessage(PongMessage(), y, 1000)
        x.scheduleMessage(PingMessage(), y, 1000)
        advanceTimeBy(500)

        x.scheduleMessage(PingMessage(), y, 1000)
        advanceTimeBy(1000)

        assertEquals(2, receivedMessages.size)
        assertEquals(PongMessage::class.java, receivedMessages[0].javaClass)
        assertEquals(PingMessage::class.java, receivedMessages[1].javaClass)

        x.actor.close()
        y.actor.close()
    }

    data class PingMessage(override val sender: ISender = DefaultSender) : Message {
        override fun withSender(sender: ISender) = this.copy(sender = sender)
    }

    data class PongMessage(override val sender: ISender = DefaultSender) : Message {
        override fun withSender(sender: ISender) = this.copy(sender = sender)
    }
}
package com.rosi.masts.mvc.view

import com.rosi.masts.base.actor.*
import com.rosi.masts.di.DependencyProvider
import com.rosi.masts.mvc.*
import com.rosi.masts.mvc.control.Controller
import com.rosi.masts.mvc.model.media.MediaController
import com.rosi.masts.mvc.view.android.activity.keybinding.KeyBindingActivityActor
import com.rosi.masts.mvc.view.android.activity.main.MainActivityActor
import com.rosi.masts.utils.Logger
import com.rosi.masts.mvc.view.stt.JavaToJni
import com.rosi.masts.mvc.view.stt.JniToJava
import com.rosi.masts.mvc.view.stt.JniToJavaActor

class ViewManager(controller: Controller, override val name: String, deps: DependencyProvider) : Actor(name, deps.logger, deps.generalScope) {
    private val tag = "ViewManager"
    override val logger: Logger = deps.logger

    private var isServiceRunning = false
    val jniToJavaActor = JniToJavaActor(controller, deps.settings, "view-manager/jni-to-java-actor", logger, deps.generalScope)
    private val appController: MediaController = deps.mediaController
    private val jniToJava: JniToJava = deps.jniToJava
    private val javaToJni: JavaToJni = deps.javaToJni
    private val rootChecker = deps.rootChecker

    val keyBindingActivityActor = KeyBindingActivityActor(controller, "view-manager/key-binding-activity-actor", logger, deps.mainScope)
    val mainActivityActor = MainActivityActor(controller, this, deps.stringsProvider, "view-manager/main-activity-actor", logger, deps.mainScope)

    init {
        initJavaToJni()
    }

    override suspend fun receive(message: Message) {
        super.receive(message)

        when (message) {

            is GetServiceStatus -> {
                logger.testPrint(tag, "receive, GetServiceStatus")
                this send message.withServiceStatus(isServiceRunning) to message.recipient
            }
            is ServiceStatusChanged -> {
                this.isServiceRunning = message.isRunning
                this send message to mainActivityActor
            }

            is KeyDetectedMessage -> this send message to keyBindingActivityActor

            is InputKeyMessage -> this send message to appController

            is SelectActionMessage -> this send message to keyBindingActivityActor

            is SelectKeyMessage -> this send message to keyBindingActivityActor

            is BindSuccessMessage -> this send message to keyBindingActivityActor

            is GetAvailableActionsMessage -> this send message to keyBindingActivityActor

            is RemoveKeyActionBindingMessage -> this send message to mainActivityActor

            is ImportKeyBindingsMessage -> this send message to mainActivityActor

            else -> printUnknownMessage(message)
        }
    }

    private fun initJavaToJni() {
        try {
            println("[SR.Test] =================== JavaToJniInit ===================")
            jniToJava.init(jniToJavaActor, logger)
            javaToJni.JavaToJniInit()
            javaToJni.InitArmMcuCom()
            println("[SR.Test] JniControl loaded successfully")
            logger.i(tag, "JniControl.isArmMcuComOpen: " + javaToJni.isArmMcuComOpen())
        } catch (t: Throwable) {
            println("[SR.Test] Failed to load JniControl, $t")
        }
    }
}


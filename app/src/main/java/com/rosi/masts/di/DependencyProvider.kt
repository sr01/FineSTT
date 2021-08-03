package com.rosi.masts.di

import com.rosi.masts.base.Logger
import com.rosi.masts.mvc.control.Controller
import com.rosi.masts.mvc.model.media.MediaController
import com.rosi.masts.mvc.model.settings.Settings
import com.rosi.masts.mvc.view.resources.StringsProvider
import com.rosi.masts.mvc.view.stt.JavaToJni
import com.rosi.masts.mvc.view.stt.JniToJava
import com.rosi.masts.utils.*
import kotlinx.coroutines.CoroutineScope


interface DependencyProvider {

    val settings: Settings

    val logger: Logger

    val mediaController: MediaController

    val jniToJava: JniToJava

    val javaToJni: JavaToJni

    val stringsProvider: StringsProvider

    val mainScope: CoroutineScope

    val ioScope: CoroutineScope

    val generalScope: CoroutineScope

    val rootChecker : RootChecker

    val textFileReadWrite : TextFileReadWrite

    val dateTimeProvider : DateTimeProvider

    val volumeControl : VolumeControl

    val controller: Controller
}

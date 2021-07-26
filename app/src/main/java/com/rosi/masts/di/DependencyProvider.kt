package com.rosi.masts.di

import com.rosi.masts.mvc.model.media.MediaController
import com.rosi.masts.mvc.model.settings.Settings
import com.rosi.masts.mvc.view.resources.StringsProvider
import com.rosi.masts.mvc.view.stt.JavaToJni
import com.rosi.masts.mvc.view.stt.JniToJava
import com.rosi.masts.utils.DateTimeProvider
import com.rosi.masts.utils.Logger
import com.rosi.masts.utils.RootChecker
import com.rosi.masts.utils.TextFileReadWrite
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
}

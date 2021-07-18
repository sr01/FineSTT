package com.rosi.masts.mvc

import com.rosi.masts.di.DependencyProvider
import com.rosi.masts.mvc.control.Controller
import com.rosi.masts.mvc.model.ActionTypes
import com.rosi.masts.mvc.model.media.MediaController
import com.rosi.masts.mvc.model.settings.Settings
import com.rosi.masts.mvc.view.android.service.AppControlServiceActor
import com.rosi.masts.mvc.view.resources.StringsProvider
import com.rosi.masts.mvc.view.stt.JavaToJni
import com.rosi.masts.mvc.view.stt.JniToJava
import com.rosi.masts.test.TestActor
import com.rosi.masts.utils.ConsoleLogger
import com.rosi.masts.utils.Logger
import com.rosi.masts.utils.RootChecker
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.test.TestCoroutineScope
import org.mockito.kotlin.*

open class ControllerTestBase {
    val testScope = TestCoroutineScope()
    val dependencyProvider = object : DependencyProvider {
        override val settings: Settings = mock()
        override val logger: Logger = ConsoleLogger
        override val mediaController: MediaController = mock()
        override val jniToJava: JniToJava = mock()
        override val javaToJni: JavaToJni = mock()
        override val stringsProvider: StringsProvider = mock() {
            val argumentCaptor = argumentCaptor<ActionTypes>()
            on {
                getDisplayNameForKeyActionType(argumentCaptor.capture())
            } doAnswer  {
                argumentCaptor.firstValue.name.uppercase()
            }
        }
        override val mainScope: CoroutineScope = testScope
        override val ioScope: CoroutineScope = testScope
        override val generalScope: CoroutineScope = testScope
        override val rootChecker: RootChecker = mock()
    }
    val controller: Controller = Controller(dependencyProvider)
    val testActor = TestActor()
    val serviceActor = AppControlServiceActor(controller, controller.viewManager, "test/app-control-service", dependencyProvider.logger, dependencyProvider.generalScope)
}
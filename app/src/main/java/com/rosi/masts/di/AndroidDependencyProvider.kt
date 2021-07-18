package com.rosi.masts.di

import android.content.Context
import com.rosi.masts.mvc.model.media.MediaController
import com.rosi.masts.mvc.model.settings.Settings
import com.rosi.masts.mvc.view.resources.StringsProvider
import com.rosi.masts.mvc.view.android.resources.AndroidStringsProvider
import com.rosi.masts.utils.Logger
import com.rosi.masts.utils.android.*
import com.rosi.masts.mvc.view.android.settings.SharedPreferencesSettings
import com.rosi.masts.mvc.view.android.media.SpotifySendKeysAppControllerActor
import com.rosi.masts.mvc.view.android.stt.*
import com.rosi.masts.mvc.view.stt.JavaToJni
import com.rosi.masts.mvc.view.stt.JniToJava
import com.rosi.masts.mvc.view.stt.SttJavaToJni
import com.rosi.masts.mvc.view.stt.SttJniToJave
import com.rosi.masts.utils.RootChecker
import com.rosi.masts.utils.TerminalUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope

class AndroidDependencyProvider(context: Context) : DependencyProvider {

    private val fileLogger = AndroidFileLogger(context)

    override val settings: Settings by lazy {
        SharedPreferencesSettings(context.resources, context.defaultSharedPreferences)
    }

    override val logger: Logger by lazy {
        LogCollection(listOf(AndroidLogger, fileLogger))
    }

    override val mediaController: MediaController by lazy {
        SpotifySendKeysAppControllerActor(context, settings, stringsProvider, logger, mainScope)
    }

    override val jniToJava: JniToJava by lazy {
        if (settings.isSimulateKeyInputEnabled()) {
            MCUKeySimulateByIntent(context)
        } else {
            SttJniToJave
        }
    }

    override val javaToJni: JavaToJni by lazy {
        SttJavaToJni
    }

    override val stringsProvider: StringsProvider by lazy {
        AndroidStringsProvider(context, settings)
    }

    override val mainScope: CoroutineScope by lazy {
        MainScope()
    }

    override val ioScope: CoroutineScope by lazy {
        CoroutineScope(Dispatchers.IO + Job())
    }

    override val generalScope: CoroutineScope by lazy {
        CoroutineScope(Dispatchers.Default + Job())
    }

    override val rootChecker: RootChecker by lazy {
        TerminalUtils
    }
}
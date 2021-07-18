package com.rosi.masts.mvc.view.android.media

import android.content.Context
import com.rosi.masts.mvc.model.settings.Settings
import com.rosi.masts.mvc.view.resources.StringsProvider
import com.rosi.masts.utils.Logger
import kotlinx.coroutines.CoroutineScope

class SpotifySendKeysAppControllerActor(context: Context, settings: Settings, private val stringsProvider: StringsProvider, logger: Logger, scope: CoroutineScope) : SendKeysAppMediaControllerActor(
    context = context,
    className = "com.spotify.music.internal.receiver.MediaButtonReceiver",
    packageName = "com.spotify.music",
    settings,
    stringsProvider,
    name = "Spotify",
    logger = logger,
    scope = scope)

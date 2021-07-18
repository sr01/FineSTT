package com.rosi.masts.mvc.view.android.resources

import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
import com.rosi.masts.R
import com.rosi.masts.mvc.model.ActionTypes
import com.rosi.masts.mvc.model.settings.Settings
import com.rosi.masts.mvc.view.resources.StringsProvider
import java.util.*

class AndroidStringsProvider(private val context: Context, private val settings: Settings) : StringsProvider {

    override fun getDisplayNameForKeyActionType(actionType: ActionTypes): String {
        val resources = getLocalizedResources(context, Locale(settings.getUILanguage()))
        return when (actionType) {
            ActionTypes.Next -> resources.getString(R.string.key_action_types_next)
            ActionTypes.Prev -> resources.getString(R.string.key_action_types_prev)
            ActionTypes.Pause -> resources.getString(R.string.key_action_types_pause)
            ActionTypes.Play -> resources.getString(R.string.key_action_types_play)
            ActionTypes.PlayPause -> resources.getString(R.string.key_action_types_playpause)
        }
    }

    fun getLocalizedResources(context: Context, locale: Locale): Resources {
        var conf = Configuration(context.resources.configuration)
        conf.setLocale(locale)
        val localizedContext = context.createConfigurationContext(conf)
        return localizedContext.resources
    }
}
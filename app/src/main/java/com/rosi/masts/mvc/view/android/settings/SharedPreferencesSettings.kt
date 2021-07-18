package com.rosi.masts.mvc.view.android.settings

import android.content.SharedPreferences
import android.content.res.Resources
import com.rosi.masts.R
import com.rosi.masts.mvc.model.keybinding.KeyBindingCompareMethod
import com.rosi.masts.mvc.model.settings.LogLevel
import com.rosi.masts.mvc.model.settings.Settings
import java.util.*

class SharedPreferencesSettings(private val resources: Resources, private val prefs: SharedPreferences) : Settings {

    override fun isServiceStartOnBootEnabled(): Boolean {
        return prefs.getBoolean(resources.getString(R.string.settings_key_start_on_boot_enabled), false)
    }

    override fun setServiceStartOnBootEnabled(enabled: Boolean) {
        prefs.edit()
            .putBoolean(resources.getString(R.string.settings_key_start_on_boot_enabled), enabled)
            .apply()
    }

    override fun getDisplayLogLevel(): LogLevel {
        return LogLevel.values()[prefs.getInt(resources.getString(R.string.settings_key_display_log_level), LogLevel.Info.ordinal)]
    }

    override fun setDisplayLogLevel(level: LogLevel) {
        prefs.edit()
            .putInt(resources.getString(R.string.settings_key_display_log_level), level.ordinal)
            .apply()
    }

    override fun setInputKeyDebounceMillis(millis: Long) {
        prefs.edit()
            .putString(resources.getString(R.string.settings_key_input_key_debounce_millis), millis.toString())
            .apply()
    }

    override fun getInputKeyDebounceMillis(): Long {
        return prefs.getString(resources.getString(R.string.settings_key_input_key_debounce_millis), "150")!!.toLong()
    }

    override fun getDisplayKeyInputEventsOnly(): Boolean {
        return prefs.getBoolean(resources.getString(R.string.settings_key_display_input_event_only), false)
    }

    override fun setDisplayKeyInputEventsOnly(value: Boolean) {
        prefs.edit()
            .putBoolean(resources.getString(R.string.settings_key_display_input_event_only), value)
            .apply()
    }

    override fun getKeyBindingsJSON(): String {
        return prefs.getString(resources.getString(R.string.settings_key_bindings_json), "{}")!!
    }

    override fun setKeyBindingsJSON(json: String) {
        prefs.edit()
            .putString(resources.getString(R.string.settings_key_bindings_json), json)
            .apply()
    }

    override fun getKeyBindingCompareMethod(): KeyBindingCompareMethod {
        val index = prefs.getString(resources.getString(R.string.settings_key_binding_compare_method), KeyBindingCompareMethod.b345.ordinal.toString())!!.toInt()
        return KeyBindingCompareMethod.values()[index]
    }

    override fun setKeyBindingCompareMethod(method: KeyBindingCompareMethod) {
        prefs.edit()
            .putString(resources.getString(R.string.settings_key_binding_compare_method), method.ordinal.toString())
            .apply()
    }

    override fun isSimulateKeyInputEnabled(): Boolean {
        return prefs.getBoolean(resources.getString(R.string.settings_key_simulate_key_input), false)
    }

    override fun checkIfAppIsRunningEnabled(): Boolean {
        return prefs.getBoolean(resources.getString(R.string.settings_key_check_if_app_is_running), true)
    }

    override fun setCheckIfAppIsRunningEnabled(enabled: Boolean) {
        prefs.edit()
            .putBoolean(resources.getString(R.string.settings_key_check_if_app_is_running), enabled)
            .apply()
    }

    override fun showToastMessagesForMediaActionsEnabled(): Boolean {
        return prefs.getBoolean(resources.getString(R.string.settings_key_show_toast_messages_for_media_actions), true)
    }

    override fun setShowToastMessagesForMediaActionsEnabled(enabled: Boolean) {
        prefs.edit()
            .putBoolean(resources.getString(R.string.settings_key_show_toast_messages_for_media_actions), enabled)
            .apply()
    }

    override fun getUILanguage(): String {
        return prefs.getString(resources.getString(R.string.settings_key_ui_language), Locale.getDefault().language)!!
    }

    override fun isMultipleKeysPerActionAllowed(): Boolean {
        return prefs.getBoolean(resources.getString(R.string.settings_key_multiple_keys_per_action_allowed), false)
    }

    override fun setMultipleKeysPerActionAllowed(allowed: Boolean) {
        prefs.edit()
            .putBoolean(resources.getString(R.string.settings_key_multiple_keys_per_action_allowed), allowed)
            .apply()
    }
}
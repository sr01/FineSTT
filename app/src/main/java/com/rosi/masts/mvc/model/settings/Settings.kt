package com.rosi.masts.mvc.model.settings

import com.rosi.masts.mvc.model.keybinding.KeyBindingCompareMethod

interface Settings {

    fun isServiceStartOnBootEnabled(): Boolean

    fun setServiceStartOnBootEnabled(enabled: Boolean)

    fun getDisplayLogLevel(): LogLevel

    fun setDisplayLogLevel(level: LogLevel)

    fun setInputKeyDebounceMillis(millis: Long)

    fun getInputKeyDebounceMillis(): Long

    fun getDisplayKeyInputEventsOnly(): Boolean

    fun setDisplayKeyInputEventsOnly(value: Boolean)

    fun getKeyBindingsJSON(): String

    fun setKeyBindingsJSON(json: String)

    fun getKeyBindingCompareMethod(): KeyBindingCompareMethod

    fun setKeyBindingCompareMethod(method: KeyBindingCompareMethod)

    fun isSimulateKeyInputEnabled(): Boolean

    fun checkIfAppIsRunningEnabled(): Boolean

    fun setCheckIfAppIsRunningEnabled(enabled: Boolean)

    fun showToastMessagesForMediaActionsEnabled(): Boolean

    fun setShowToastMessagesForMediaActionsEnabled(enabled: Boolean)

    fun getUILanguage(): String

    fun isMultipleKeysPerActionAllowed(): Boolean

    fun setMultipleKeysPerActionAllowed(allowed: Boolean)
}

enum class LogLevel {
    Error,
    Info,
    Debug
}
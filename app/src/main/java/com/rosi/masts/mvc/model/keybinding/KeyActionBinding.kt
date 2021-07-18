package com.rosi.masts.mvc.model.keybinding

import com.rosi.masts.mvc.model.ActionTypes
import com.rosi.masts.mvc.model.mcu.MCUInputKey
import kotlinx.serialization.Serializable

@Serializable
data class KeyActionBinding(val id: String, val key: MCUInputKey, val actionType: ActionTypes)
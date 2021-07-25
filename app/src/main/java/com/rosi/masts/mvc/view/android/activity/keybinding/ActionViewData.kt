package com.rosi.masts.mvc.view.android.activity.keybinding

import com.rosi.masts.mvc.model.ActionTypes
import com.rosi.masts.mvc.view.resources.StringsProvider


data class ActionViewData(val displayName: String, val action: ActionTypes, var isSelected: Boolean = false, val boundKeyName: String? = null, val bindingID: String? = null)

data class ActionWithMultipleKeysViewData(val displayName: String, val action: ActionTypes, val keys: MutableList<ActionViewData>)

fun Collection<ActionTypes>.toListOfActionViewData(stringsProvider: StringsProvider): List<ActionViewData> = this.map {
    ActionViewData(stringsProvider.getDisplayNameForKeyActionType(it), it)
}


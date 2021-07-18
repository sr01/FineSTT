package com.rosi.masts.mvc.view.resources

import com.rosi.masts.mvc.model.ActionTypes

interface StringsProvider {
    fun getDisplayNameForKeyActionType(actionType: ActionTypes): String
}
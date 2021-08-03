package com.rosi.masts.mvc.view.android.activity.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rosi.masts.mvc.model.keybinding.KeyActionBinding
import com.rosi.masts.mvc.view.MainActivityActor
import com.rosi.masts.mvc.view.android.activity.keybinding.ActionViewData
import com.rosi.masts.mvc.view.android.activity.keybinding.ActionWithMultipleKeysViewData
import com.rosi.masts.mvc.view.resources.StringsProvider
import com.rosi.masts.base.Logger
import kotlinx.coroutines.launch
import java.io.File

class MainViewModel(private val actor: MainActivityActor, private val stringsProvider: StringsProvider, private val logger: Logger) : ViewModel(), MainActivityActor.Listener {
    private val tag = "MainViewModel"
    val isServiceRunning = MutableLiveData<Boolean>(false)
    val actions = MutableLiveData<Collection<ActionWithMultipleKeysViewData>>()
    val shareNotification = MutableLiveData<String>()

    init {
        logger.testPrint(tag, "init, ")
        actor.addListener(this)
    }

    override fun onCleared() {
        super.onCleared()
        logger.testPrint(tag, "onCleared, ")
        actor.removeListener(this)
    }

    override fun onServiceStatusChanged(isRunning: Boolean) {
        isServiceRunning.postValue(isRunning)
    }

    override fun onShowBindings(bindings: Collection<KeyActionBinding>) {
        viewModelScope.launch {
            val keyActionViewDataList = bindings.map { binding ->
                ActionViewData(
                    bindingID = binding.id,
                    displayName = stringsProvider.getDisplayNameForKeyActionType(binding.actionType),
                    action = binding.actionType,
                    isSelected = false,
                    boundKeyName = binding.key.displayName)
            }

            val actionViewMultipleKeysData = keyActionViewDataList.groupBy { it.action }
                .map {
                    ActionWithMultipleKeysViewData(
                        action = it.key,
                        displayName = stringsProvider.getDisplayNameForKeyActionType(it.key),
                        keys = it.value.toMutableList())
                }

            actions.value = actionViewMultipleKeysData
        }
    }

    override fun onBindingsRemoved(removedBindingsIDs: Collection<String>) {

        viewModelScope.launch {
            actions.value?.let { actions ->
                actions.forEachIndexed { index, action ->
                    val removedKeys = action.keys.filter { action -> removedBindingsIDs.contains(action.bindingID) }
                    if (removedKeys.isNotEmpty()) {
                        action.keys.removeAll(removedKeys)
                    }
                }

                val actionsWithKeys = actions.filter { it.keys.isNotEmpty() }
                this@MainViewModel.actions.value = actionsWithKeys
            }
        }
    }

    override fun onBindingUpdated(binding: KeyActionBinding) {
        viewModelScope.launch {

            val actionViewData = ActionViewData(
                bindingID = binding.id,
                displayName = stringsProvider.getDisplayNameForKeyActionType(binding.actionType),
                action = binding.actionType,
                isSelected = false,
                boundKeyName = binding.key.displayName)

            val newActions = mutableListOf<ActionWithMultipleKeysViewData>()
            actions.value?.let { newActions.addAll(it) }

            val action = newActions.firstOrNull { it.action == binding.actionType }

            if (action != null) {
                action.keys.add(actionViewData)
            } else {
                newActions.add(ActionWithMultipleKeysViewData(
                    action = actionViewData.action,
                    displayName = stringsProvider.getDisplayNameForKeyActionType(actionViewData.action),
                    keys = mutableListOf(actionViewData)
                ))
            }

            actions.value = newActions
        }
    }

    override fun onShareBindings(bindingsJson: String) {
        shareNotification.postValue(bindingsJson)
    }

    fun shareBindings() {
        actor.shareBindings()
    }

    fun importBindings(file: File) {
        actor.importBindings(file)
    }

    fun exportBindings() {
        actor.exportBindings()
    }

    fun unbindAction(actionViewData: ActionViewData) {
        actor.unbindAction(actionViewData)
    }
}
package com.rosi.masts.mvc.model.keybinding

import com.rosi.masts.mvc.model.ActionTypes
import com.rosi.masts.mvc.model.mcu.MCUInputKey
import kotlinx.serialization.Serializable
import java.util.*

typealias BindingID = String

@Serializable
class KeyBindingStorage {
    private var bindings = mutableMapOf<BindingID, KeyActionBinding>()

    fun add(binding: KeyActionBinding) {
        bindings[binding.id] = binding
    }

    fun add(key: MCUInputKey, actionType: ActionTypes): KeyActionBinding {
        val binding = KeyActionBinding(UUID.randomUUID().toString(), key, actionType)
        add(binding)
        return binding
    }

    fun getByID(bindingID: BindingID): KeyActionBinding? {
        return bindings[bindingID]
    }

    fun getByKey(key: MCUInputKey, compareMethod: KeyBindingCompareMethod = KeyBindingCompareMethod.b345): KeyActionBinding? {
        val comparator = when (compareMethod) {
            KeyBindingCompareMethod.b3 -> MCUInputKey3Comparator
            KeyBindingCompareMethod.b34 -> MCUInputKey34Comparator

            KeyBindingCompareMethod.b345 -> MCUInputKey345Comparator
        }

        return bindings.values.firstOrNull { binding ->
            comparator.areEquals(binding.key, key)
        }
    }

    fun getAll() = bindings.values.toList()

    fun addOrUpdateKey(bindingID: BindingID?, key: MCUInputKey, actionType: ActionTypes, allowMultipleKeysPerAction: Boolean): KeyActionBinding {
        if (!allowMultipleKeysPerAction) {
            removeAllByAction(actionType, bindingID)
        }

        return if (bindingID != null) {
            val binding = bindings[bindingID]?.copy(key = key)
            if (binding != null) {
                bindings[bindingID] = binding
                binding
            } else {
                add(key, actionType)
            }
        } else {
            add(key, actionType)
        }
    }

    fun removeByID(bindingID: BindingID): KeyActionBinding? {
        return bindings.remove(bindingID)
    }

    fun replaceAll(storage: KeyBindingStorage) {
        this.bindings.clear()
        this.bindings.putAll(storage.bindings)
    }

    fun addAll(storage: KeyBindingStorage) {
        this.bindings.putAll(storage.bindings)
    }

    private fun removeAllByAction(actionType: ActionTypes, excludeBindingID: BindingID? = null): Collection<KeyActionBinding> {
        val removeBindings = bindings.values.filter {
            (excludeBindingID != null && it.id != excludeBindingID) && it.actionType == actionType
        }
        removeBindings.forEach { bindings.remove(it.id) }
        return removeBindings
    }
}
package com.rosi.masts.mvc.view.android.adapters

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.rosi.masts.R
import com.rosi.masts.mvc.view.android.activity.OnItemSelectedListener
import com.rosi.masts.mvc.view.android.activity.keybinding.ActionViewData
import com.rosi.masts.mvc.view.android.activity.keybinding.ActionWithMultipleKeysViewHolder
import com.rosi.masts.mvc.view.android.activity.keybinding.ActionWithMultipleKeysViewData
import com.rosi.masts.utils.android.layoutInflater

class ActionWithMultipleKeysAdapter(private val onItemSelectedListener: OnItemSelectedListener<ActionViewData>)
    : RecyclerView.Adapter<ActionWithMultipleKeysViewHolder>() {

    val actions = mutableListOf<ActionWithMultipleKeysViewData>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActionWithMultipleKeysViewHolder {
        val view = parent.context.layoutInflater.inflate(R.layout.item_action_multiple_keys, parent, false)
        return ActionWithMultipleKeysViewHolder(view, onItemSelectedListener)
    }

    override fun onBindViewHolder(holder: ActionWithMultipleKeysViewHolder, position: Int) {
        val action = actions[position]
        holder.bind(action)
    }

    override fun getItemCount(): Int = actions.size

    fun removeKeyBindings(bindingsIDs: Collection<String>) {
        actions.forEachIndexed { index, action ->
            val keys = action.keys.filter { bindingsIDs.contains(it.bindingID) }
            if (keys.isNotEmpty()) {
                action.keys.removeAll(keys)
                notifyItemChanged(index)
            }
        }

        val actionsWithNoKey = actions.filter { it.keys.isEmpty() }
        actions.removeAll(actionsWithNoKey)
        notifyDataSetChanged()
    }

//    fun getSelectedItem(): ActionViewData? {
//        return actions.firstOrNull { it.isSelected }
//    }
//
//    fun clearSelection() {
//        actions.forEach { it.isSelected = false }
//        notifyDataSetChanged()
//    }
}
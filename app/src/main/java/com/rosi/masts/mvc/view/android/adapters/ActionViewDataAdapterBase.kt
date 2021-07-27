package com.rosi.masts.mvc.view.android.adapters

import androidx.recyclerview.widget.RecyclerView
import com.rosi.masts.mvc.view.android.activity.OnItemSelectedListener
import com.rosi.masts.mvc.view.android.activity.keybinding.ActionViewData

abstract class ActionViewDataAdapterBase<VD, VH : RecyclerView.ViewHolder?>(private val onItemSelectedListener: OnItemSelectedListener<VD>) : RecyclerView.Adapter<VH>() {
    protected val actions = mutableListOf<ActionViewData>()

    override fun getItemCount(): Int = actions.size

    fun toggleItemSelection(item: ActionViewData) {
        actions.forEach { it.isSelected = false }
        item.isSelected = !item.isSelected
    }

    fun getSelectedItem(): ActionViewData? {
        return actions.firstOrNull { it.isSelected }
    }

    fun clearSelection() {
        actions.forEach { it.isSelected = false }
        notifyDataSetChanged()
    }

    fun addItems(actionViewData: List<ActionViewData>) {
        actions.clear()
        actions.addAll(actionViewData)
        notifyDataSetChanged()
    }

    fun removeItemsByBindingID(bindingsIDs: Collection<String>) {
        val items = actions.filter {
            bindingsIDs.contains(it.bindingID)
        }
        actions.removeAll(items)
        notifyDataSetChanged()
    }
}
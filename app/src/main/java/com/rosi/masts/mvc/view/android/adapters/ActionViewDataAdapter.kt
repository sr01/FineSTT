package com.rosi.masts.mvc.view.android.adapters

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.rosi.masts.R
import com.rosi.masts.mvc.view.android.activity.OnItemSelectedListener
import com.rosi.masts.mvc.view.android.activity.keybinding.ActionViewHolder
import com.rosi.masts.mvc.view.android.activity.keybinding.ActionViewData
import com.rosi.masts.utils.android.layoutInflater

class ActionViewDataAdapter(private val enableSelection : Boolean, private val onItemSelectedListener: OnItemSelectedListener<ActionViewData>)
    : RecyclerView.Adapter<ActionViewHolder>() {

    val actions = mutableListOf<ActionViewData>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActionViewHolder {
        val view = parent.context.layoutInflater.inflate(R.layout.item_keyaction_chip, parent, false)
        return ActionViewHolder(view, onItemSelectedListener, enableSelection)
    }

    override fun onBindViewHolder(holder: ActionViewHolder, position: Int) {
        val action = actions[position]
        holder.bind(action)
    }

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
}
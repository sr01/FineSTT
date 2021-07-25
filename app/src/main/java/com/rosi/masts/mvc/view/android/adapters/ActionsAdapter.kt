package com.rosi.masts.mvc.view.android.adapters

import android.view.ViewGroup
import com.rosi.masts.R
import com.rosi.masts.mvc.view.android.activity.OnItemSelectedListener
import com.rosi.masts.mvc.view.android.activity.keybinding.ActionViewData
import com.rosi.masts.mvc.view.android.activity.keybinding.ActionViewHolder
import com.rosi.masts.utils.android.layoutInflater

class ActionsAdapter(private val enableSelection: Boolean,
                     private val onItemSelectedListener: OnItemSelectedListener<ActionViewData>) :
    ActionViewDataAdapterBase<ActionViewData, ActionViewHolder>(onItemSelectedListener) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActionViewHolder {
        val view = parent.context.layoutInflater.inflate(R.layout.item_action_chip, parent, false)
        return ActionViewHolder(view, onItemSelectedListener, enableSelection)
    }

    override fun onBindViewHolder(holder: ActionViewHolder, position: Int) {
        val action = actions[position]
        holder.bind(action)
    }
}


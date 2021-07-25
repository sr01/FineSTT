package com.rosi.masts.mvc.view.android.adapters

import android.view.ViewGroup
import com.rosi.masts.R
import com.rosi.masts.mvc.view.android.activity.OnItemSelectedListener
import com.rosi.masts.mvc.view.android.activity.keybinding.ActionViewData
import com.rosi.masts.mvc.view.android.activity.keybinding.KeyViewHolder
import com.rosi.masts.utils.android.layoutInflater

class KeysAdapter(private val enableSelection: Boolean,
                  private val onItemSelectedListener: OnItemSelectedListener<ActionViewData>) :
    ActionViewDataAdapterBase<ActionViewData, KeyViewHolder>(onItemSelectedListener) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): KeyViewHolder {
        val view = parent.context.layoutInflater.inflate(R.layout.item_key_chip, parent, false)
        return KeyViewHolder(view, onItemSelectedListener, enableSelection)
    }

    override fun onBindViewHolder(holder: KeyViewHolder, position: Int) {
        val action = actions[position]
        holder.bind(action)
    }
}
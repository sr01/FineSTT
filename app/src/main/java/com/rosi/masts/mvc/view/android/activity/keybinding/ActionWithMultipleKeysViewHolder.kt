package com.rosi.masts.mvc.view.android.activity.keybinding

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import com.rosi.masts.R
import com.rosi.masts.mvc.view.android.activity.OnItemSelectedListener
import com.rosi.masts.mvc.view.android.adapters.KeysAdapter

class ActionWithMultipleKeysViewHolder(view: View, val onItemSelectedListener: OnItemSelectedListener<ActionViewData>) : RecyclerView.ViewHolder(view) {

    private var actionViewMultipleKeysData: ActionWithMultipleKeysViewData? = null
    private val keysAdapter: KeysAdapter = KeysAdapter(false, onItemSelectedListener)
    private val actionTextView: TextView = view.findViewById(R.id.text_action)
    private val recyclerView: RecyclerView = view.findViewById(R.id.recycler_view)

    init {
        recyclerView.layoutManager = FlexboxLayoutManager(view.context).apply {
            flexWrap = FlexWrap.WRAP
            justifyContent = JustifyContent.FLEX_START
        }

        recyclerView.adapter = keysAdapter
    }

    fun bind(actionViewMultipleKeysData: ActionWithMultipleKeysViewData) {
        this.actionViewMultipleKeysData = actionViewMultipleKeysData
        actionTextView.text = actionViewMultipleKeysData.displayName
        keysAdapter.addItems(actionViewMultipleKeysData.keys)
    }
}
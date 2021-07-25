package com.rosi.masts.mvc.view.android.activity.keybinding

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.Chip
import com.rosi.masts.R
import com.rosi.masts.mvc.view.android.activity.OnItemSelectedListener

open class ActionViewHolder(view: View, val onItemSelectedListener: OnItemSelectedListener<ActionViewData>, private val enableSelection: Boolean = true) :
    RecyclerView.ViewHolder(view) {
    protected var chip: Chip = view.findViewById(R.id.chip)
    private var actionViewData: ActionViewData? = null

    init {
        chip.isCheckable = enableSelection

        view.setOnClickListener {
            this.actionViewData?.let {
                onItemSelectedListener.onItemSelected(it)
            }
        }
    }

    open fun bind(actionViewData: ActionViewData) {
        this.actionViewData = actionViewData
        chip.text = actionViewData.displayName
        chip.isChecked = enableSelection && actionViewData.isSelected
    }
}

class KeyViewHolder(view: View, onItemSelectedListener: OnItemSelectedListener<ActionViewData>, enableSelection: Boolean = true) :
    ActionViewHolder(view, onItemSelectedListener, enableSelection) {

    override fun bind(actionViewData: ActionViewData) {
        super.bind(actionViewData)

        chip.text = actionViewData.boundKeyName
    }
}
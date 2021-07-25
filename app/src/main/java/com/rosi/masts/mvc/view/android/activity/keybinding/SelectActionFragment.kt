package com.rosi.masts.mvc.view.android.activity.keybinding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import com.rosi.masts.R
import com.rosi.masts.databinding.FragmentSelectActionBinding
import com.rosi.masts.mvc.model.ActionTypes
import com.rosi.masts.mvc.view.android.adapters.ActionsAdapter
import com.rosi.masts.utils.android.toast

class SelectActionFragment : WizardBaseFragment() {
    private lateinit var actionsAdapter: ActionsAdapter
    private lateinit var viewBinding: FragmentSelectActionBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        viewBinding = FragmentSelectActionBinding.inflate(inflater, container, false)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        actionsAdapter = ActionsAdapter(true) { onActionItemSelectionChanged(it) }

        viewBinding.recyclerView.layoutManager = FlexboxLayoutManager(requireContext()).apply {
            flexWrap = FlexWrap.WRAP
            justifyContent = JustifyContent.CENTER
        }

        viewBinding.recyclerView.adapter = actionsAdapter

        viewBinding.buttonNext.visibility = View.INVISIBLE

        viewBinding.buttonNext.setOnClickListener { onNextClick() }
    }

    override fun onResume() {
        super.onResume()
        (requireActivity() as KeyBindingActivity).setTitle(getString(R.string.key_binding_select_action_screen_title))

        actor.getAvailableActions()
    }

    private fun onActionItemSelectionChanged(item: ActionViewData) {

        actionsAdapter.toggleItemSelection(item)

        viewBinding.buttonNext.visibility = when (item.isSelected) {
            true -> View.VISIBLE
            false -> View.INVISIBLE
        }
        actionsAdapter.notifyDataSetChanged()
    }

    private fun onNextClick() {
        val selectedItem: ActionViewData? = actionsAdapter.getSelectedItem()
        if (selectedItem == null) {
            requireActivity().toast(getString(R.string.no_action_selected_message))
        } else {
            actor.actionSelected(selectedItem.action)
        }
    }

    private fun refreshAvailableActions(availableActions: Collection<ActionTypes>) {
        logger.testPrint(TAG, "refreshAvailableActions, availableActions: ${availableActions.size}")
        actionsAdapter.addItems(availableActions.toListOfActionViewData(stringsProvider))

        if (availableActions.isEmpty()) {
            viewBinding.textInstruction.text = getString(R.string.no_action_available)
        } else {
            viewBinding.textInstruction.text = getString(R.string.select_from_available_actions)
        }
    }

    override fun onBindComplete() {
        super.onBindComplete()

        actionsAdapter.clearSelection()
    }

    override fun onAvailableActions(actions: Collection<ActionTypes>){
        refreshAvailableActions(actions)
    }

    private val TAG = "SelectActionFragment"
}
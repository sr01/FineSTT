package com.rosi.masts.mvc.view.android.activity.main

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.*
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.afollestad.materialdialogs.MaterialDialog
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import com.rosi.masts.R
import com.rosi.masts.databinding.FragmentMainBinding
import com.rosi.masts.di.controller
import com.rosi.masts.di.dependencyProvider
import com.rosi.masts.mvc.view.android.activity.keybinding.ActionViewData
import com.rosi.masts.mvc.view.android.activity.keybinding.ActionWithMultipleKeysViewData
import com.rosi.masts.mvc.view.android.activity.keybinding.KeyBindingActivityOperations
import com.rosi.masts.mvc.view.android.adapters.ActionWithMultipleKeysAdapter
import com.rosi.masts.mvc.view.android.service.AppControlService
import com.rosi.masts.mvc.view.android.service.MediaNotificationListenerService
import com.rosi.masts.utils.Logger


class MainFragment : Fragment(), MainActivityActor.Listener {

    private lateinit var logger: Logger
    private lateinit var binding: FragmentMainBinding
    private lateinit var actor: MainActivityActor
    private lateinit var actionsAdapter: ActionWithMultipleKeysAdapter
    private var isServiceRunning = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        val dependencyProvider = context.applicationContext.dependencyProvider
        logger = dependencyProvider.logger
        actor = context.applicationContext.controller.viewManager.mainActivityActor
    }

    override fun onStart() {
        super.onStart()
        actor.addListener(this)
    }

    override fun onStop() {
        super.onStop()
        actor.removeListener(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        actionsAdapter = ActionWithMultipleKeysAdapter { onActionItemSelectionChanged(it) }

        binding.recyclerView.layoutManager = FlexboxLayoutManager(requireContext()).apply {
            flexWrap = FlexWrap.WRAP
            justifyContent = JustifyContent.FLEX_START
        }

        binding.recyclerView.adapter = actionsAdapter

        binding.buttonStartService.setOnClickListener { toggleService() }

        binding.buttonAddBind.setOnClickListener { onAddBindingClick() }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_menu, menu);
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_item_settings -> {
                findNavController().navigate(R.id.action_MainFragment_to_settingsFragment)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun toggleService() {
        requireContext().run {
            when (isServiceRunning) {
                true -> stopService(AppControlService.getStartIntent(applicationContext))
                false -> {
                    if (!hasNotificationListenerPermission()) {
                        MaterialDialog(requireContext()).show {
                            title(res = R.string.permissions_required_dialog_title)
                            message(res = R.string.permissions_required_for_notifications_text)
                            positiveButton(res = R.string.settings_title) {
                                openNotificationSettings()
                            }
                            negativeButton(res = android.R.string.cancel) {

                            }
                        }
                    }
                    startService(AppControlService.getStartIntent(applicationContext))
                }
            }
        }
    }

    private fun hasNotificationListenerPermission(): Boolean {
        val cn = ComponentName(requireContext(), MediaNotificationListenerService::class.java)
        val flat: String = Settings.Secure.getString(requireContext().contentResolver, "enabled_notification_listeners")
        return flat != null && flat.contains(cn.flattenToString())
    }

    private fun onActionItemSelectionChanged(actionViewData: ActionViewData) {
        MaterialDialog(requireContext()).show {
            title(text = actionViewData.displayName)
            message(text = getString(R.string.action_bound_to_key_template, actionViewData.displayName, actionViewData.boundKeyName))
            positiveButton(res = R.string.rebind) { rebindAction(actionViewData) }
            negativeButton(res = R.string.delete) { unbindAction(actionViewData) }
        }
    }

    override fun onServiceStatusChanged(isRunning: Boolean) {
        this.isServiceRunning = isRunning

        when (isRunning) {
            true -> {
                binding.textServiceStatus.text = getString(R.string.service_is_running)
                binding.buttonStartService.text = getString(R.string.stop_service)
            }
            false -> {
                binding.textServiceStatus.text = getString(R.string.service_stopped)
                binding.buttonStartService.text = getString(R.string.start_service)
            }
        }
    }

    override fun onShowActions(actionViewDataList: Collection<ActionWithMultipleKeysViewData>) {
        actionsAdapter.actions.clear()

        actionsAdapter.actions.addAll(actionViewDataList)

        actionsAdapter.notifyDataSetChanged()

        updateNoActionsView()
    }

    override fun onActionsRemoved(bindingsIDs: Collection<String>) {
        actionsAdapter.removeKeyBindings(bindingsIDs)

        updateNoActionsView()
    }

    private fun onAddBindingClick() {
        val action = MainFragmentDirections.actionMainFragmentToKeyBindingActivity(operation = KeyBindingActivityOperations.Create)
        findNavController().navigate(action)
    }

    private fun rebindAction(actionViewData: ActionViewData) {
        navigateToKeyBindingActivity(actionViewData)
    }

    private fun navigateToKeyBindingActivity(actionViewData: ActionViewData) {
        actionViewData.bindingID?.let { bindingID ->
            val action = MainFragmentDirections.actionMainFragmentToKeyBindingActivity(operation = KeyBindingActivityOperations.ChangeKey, bindingID = bindingID)
            findNavController().navigate(action)
        }
    }

    private fun unbindAction(actionViewData: ActionViewData) {
        actor.unbindAction(actionViewData)
    }

    private fun openNotificationSettings() {
        val intent = Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS")
        startActivity(intent)
    }

    private fun updateNoActionsView() {
        binding.textNoActionBindingsExist.visibility = when (actionsAdapter.itemCount) {
            0 -> View.VISIBLE
            else -> View.INVISIBLE
        }
    }
}

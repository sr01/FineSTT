package com.rosi.masts.mvc.view.android.activity.main

import android.Manifest
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.*
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.files.fileChooser
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import com.google.android.material.snackbar.Snackbar
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
import com.rosi.masts.utils.android.AndroidTextFileReadWrite
import com.rosi.masts.utils.Logger
import com.rosi.masts.utils.android.AndroidIntents


class MainFragment : Fragment(), MainActivityActor.Listener {

    private val TAG = "MainFragment"
    private lateinit var logger: Logger
    private lateinit var settings: com.rosi.masts.mvc.model.settings.Settings
    private lateinit var binding: FragmentMainBinding
    private lateinit var actor: MainActivityActor
    private lateinit var actionsAdapter: ActionWithMultipleKeysAdapter
    private var isServiceRunning = false
    private lateinit var requestPermissionLauncher: ActivityResultLauncher<String>
    private val requiredPermission = Manifest.permission.WRITE_EXTERNAL_STORAGE
    private var pendingPermissionsAction: (() -> Unit?)? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        val dependencyProvider = context.applicationContext.dependencyProvider
        logger = dependencyProvider.logger
        actor = context.applicationContext.controller.viewManager.mainActivityActor
        settings = dependencyProvider.settings

        requestPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                logger.d(TAG, "permission granted")
                pendingPermissionsAction?.invoke()

            } else {
                showPermissionRequestAndOpenApplicationDetailsSettings()
            }
        }
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
            R.id.menu_item_share -> {
                shareBindings()
                true
            }
            R.id.menu_item_export -> {
                exportBindings()
                true
            }
            R.id.menu_item_import -> {
                importBindings()
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

    override fun onShareBindings(bindingsJson: String) {
        if (bindingsJson.isEmpty()) {
            Toast.makeText(requireContext(), getString(R.string.share_bindings_message_when_no_bindings_exist), Toast.LENGTH_SHORT).show()
        } else {
            startActivity(AndroidIntents.newShareIntent(bindingsJson,getString(R.string.share_bindings_subject)))
        }
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

    private fun shareBindings() {
        actor.shareBindings()
    }

    private fun importBindings() {
        checkPermissions {
            val context = requireContext()
            val initialDirectory = AndroidTextFileReadWrite.getWorkingDirectory(context)
            MaterialDialog(context).show {
                fileChooser(initialDirectory = initialDirectory, context = context) { dialog, file ->
                    actor.importBindings(file)
                }
            }
        }
    }

    private fun exportBindings() {
        checkPermissions {
            actor.exportBindings()
        }
    }

    private fun checkPermissions(action: () -> Unit) {
        this.pendingPermissionsAction = action

        when {
            ContextCompat.checkSelfPermission(requireContext(), requiredPermission) == PackageManager.PERMISSION_GRANTED -> {
                logger.d(TAG, "permission granted")
                pendingPermissionsAction?.invoke()
            }
            shouldShowRequestPermissionRationale(requiredPermission) -> {
                logger.d(TAG, "checkPermissions, 2")
                showPermissionRequest()
            }
            else -> {
                logger.d(TAG, "checkPermissions, 3")
                requestPermissionLauncher.launch(requiredPermission)
            }
        }
    }

    private fun showPermissionRequest() {
        Snackbar.make(binding.root, R.string.permissions_request_text, Snackbar.LENGTH_LONG)
            .setAction(R.string.permissions_request_action_text) { requestPermissionLauncher.launch(requiredPermission) }
            .show()
    }

    private fun showPermissionRequestAndOpenApplicationDetailsSettings() {
        Snackbar.make(binding.root, R.string.permissions_request_via_settings_text, Snackbar.LENGTH_LONG)
            .setAction(R.string.permissions_request_via_settings_action_text) { openApplicationDetailsSettings() }
            .show()
    }

    private fun openApplicationDetailsSettings() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:${requireActivity().packageName}")).apply {
            addCategory(Intent.CATEGORY_DEFAULT)
        }
        startActivity(intent)
    }
}

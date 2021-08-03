package com.rosi.masts.mvc.view.android.activity.keybinding

import android.os.Bundle
import androidx.activity.viewModels
import androidx.navigation.navArgs
import com.rosi.masts.R
import com.rosi.masts.databinding.ActivityKeybindingBinding
import com.rosi.masts.di.KeyBindingViewModelFactory
import com.rosi.masts.di.dependencyProvider
import com.rosi.masts.mvc.view.android.activity.keybinding.KeyBindingFragmentStateAdapter.Companion.SELECT_ACTION_FRAGMENT_INDEX
import com.rosi.masts.mvc.view.android.activity.keybinding.KeyBindingFragmentStateAdapter.Companion.SELECT_KEY_FRAGMENT_INDEX
import com.rosi.masts.utils.LocaleBaseActivity
import com.rosi.masts.base.Logger
import com.rosi.masts.utils.android.toast

class KeyBindingActivity : LocaleBaseActivity() {
    private val tag = "KeyBindingActivity"
    private lateinit var logger: Logger
    private val viewModel: KeyBindingViewModel by viewModels() {
        KeyBindingViewModelFactory(this)
    }

    val args: KeyBindingActivityArgs by navArgs()

    private lateinit var viewBinding: ActivityKeybindingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewBinding = ActivityKeybindingBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        setSupportActionBar(viewBinding.toolbarLayout.toolbar)

        logger = application.dependencyProvider.logger
        viewBinding.viewPager.adapter = KeyBindingFragmentStateAdapter(this)
        viewBinding.viewPager.isUserInputEnabled = false

        viewModel.notifications.observe(this) { notification ->
            when (notification) {
                is ViewNotification.ShowSelectAction -> viewBinding.viewPager.setCurrentItem(
                    SELECT_ACTION_FRAGMENT_INDEX,
                    false
                )
                is ViewNotification.ShowSelectKey -> viewBinding.viewPager.setCurrentItem(
                    SELECT_KEY_FRAGMENT_INDEX,
                    false
                )
                is ViewNotification.Close -> {
                    toast(getString(R.string.bind_success_message))
                    finish()
                }
            }
        }

        when (args.operation) {
            KeyBindingActivityOperations.Create -> {
                viewBinding.viewPager.setCurrentItem(SELECT_ACTION_FRAGMENT_INDEX, false)
                viewModel.startBinding()

            }
            KeyBindingActivityOperations.ChangeKey -> {
                viewBinding.viewPager.setCurrentItem(SELECT_KEY_FRAGMENT_INDEX, false)
                args.bindingID.let { bindingID ->
                    if (bindingID == null) {
                        logger.e(tag, "bindingID arg is null")
                        finish()
                    } else {
                        viewModel.startBindingForBindingID(bindingID)
                    }
                }
            }
        }
    }

    fun setTitle(title: String) {
        viewBinding.toolbarLayout.toolbar.title = title
    }
}

enum class KeyBindingActivityOperations {
    Create,
    ChangeKey
}

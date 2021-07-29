package com.rosi.masts.mvc.view.android.activity.keybinding

import android.os.Bundle
import androidx.navigation.navArgs
import com.rosi.masts.databinding.ActivityKeybindingBinding
import com.rosi.masts.di.dependencyProvider
import com.rosi.masts.mvc.model.ActionTypes
import com.rosi.masts.mvc.view.android.activity.keybinding.KeyBindingFragmentStateAdapter.Companion.SELECT_ACTION_FRAGMENT_INDEX
import com.rosi.masts.mvc.view.android.activity.keybinding.KeyBindingFragmentStateAdapter.Companion.SELECT_KEY_FRAGMENT_INDEX
import com.rosi.masts.utils.LocaleBaseActivity
import com.rosi.masts.utils.Logger

class KeyBindingActivity : LocaleBaseActivity(), KeyBindingActivityActor.Listener {
    private val tag = "KeyBindingActivity"
    private lateinit var logger: Logger
    private lateinit var actor: KeyBindingActivityActor

    val args: KeyBindingActivityArgs by navArgs()

    private lateinit var viewBinding: ActivityKeybindingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewBinding = ActivityKeybindingBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        setSupportActionBar(viewBinding.toolbarLayout.toolbar)

        logger = application.dependencyProvider.logger
        actor = application.dependencyProvider.controller.viewManager.keyBindingActivityActor

        actor.addListener(this)
        viewBinding.viewPager.adapter = KeyBindingFragmentStateAdapter(this)
        viewBinding.viewPager.isUserInputEnabled = false

        when (args.operation) {
            KeyBindingActivityOperations.Create -> {
                viewBinding.viewPager.setCurrentItem(SELECT_ACTION_FRAGMENT_INDEX, false)
                actor.startBinding()
            }
            KeyBindingActivityOperations.ChangeKey -> {
                viewBinding.viewPager.setCurrentItem(SELECT_KEY_FRAGMENT_INDEX, false)
                args.bindingID.let { bindingID ->
                    if (bindingID == null) {
                        logger.e(tag, "bindingID arg is null")
                        finish()
                    } else {
                        actor.startBindingForBindingID(bindingID)
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        actor.stopBinding()
        actor.removeListener(this)
        super.onDestroy()
    }

    override fun onSelectAction(availableActions: Collection<ActionTypes>) {
        viewBinding.viewPager.setCurrentItem(SELECT_ACTION_FRAGMENT_INDEX, false)
    }

    override fun onSelectKey() {
        viewBinding.viewPager.setCurrentItem(SELECT_KEY_FRAGMENT_INDEX, false)
    }

    override fun onKeyDetected(key: String, keyDetectedCount: Int) {
    }

    override fun onBindSuccess() {
    }

    override fun onBindComplete() {
        finish()
    }

    override fun onAvailableActions(actions: Collection<ActionTypes>) {

    }

    fun setTitle(title: String) {
        viewBinding.toolbarLayout.toolbar.title = title
    }

}

enum class KeyBindingActivityOperations {
    Create,
    ChangeKey
}

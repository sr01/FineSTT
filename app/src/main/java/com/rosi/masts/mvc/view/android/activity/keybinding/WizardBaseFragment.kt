package com.rosi.masts.mvc.view.android.activity.keybinding

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.rosi.masts.di.KeyBindingViewModelFactory
import com.rosi.masts.di.dependencyProvider
import com.rosi.masts.mvc.view.KeyBindingActivityActor
import com.rosi.masts.mvc.view.resources.StringsProvider
import com.rosi.masts.base.Logger

abstract class WizardBaseFragment : Fragment() {
    protected lateinit var logger: Logger
    protected lateinit var actor: KeyBindingActivityActor
    protected lateinit var stringsProvider: StringsProvider
    protected val viewModel: KeyBindingViewModel by activityViewModels() {
        KeyBindingViewModelFactory(requireContext())
    }

    private val TAG = this::class.java.simpleName

    override fun onAttach(context: Context) {
        super.onAttach(context)
        val deps = context.dependencyProvider
        logger = deps.logger
        actor = deps.controller.viewManager.keyBindingActivityActor
        stringsProvider = deps.stringsProvider

        logger.d(TAG, "onAttach")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        logger.d(TAG, "onCreate")
    }

    override fun onDestroy() {
        super.onDestroy()
        logger.d(TAG, "onDestroy")
    }

    override fun onStart() {
        logger.d(TAG, "onStart")
        super.onStart()
    }

    override fun onStop() {
        logger.d(TAG, "onStop")
        super.onStop()
    }

    override fun onResume() {
        super.onResume()
        logger.d(TAG, "onResume")

    }

    override fun onPause() {
        super.onPause()
        logger.d(TAG, "onPause")
    }
}
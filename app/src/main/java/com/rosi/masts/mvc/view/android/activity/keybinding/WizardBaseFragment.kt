package com.rosi.masts.mvc.view.android.activity.keybinding

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.rosi.masts.di.dependencyProvider
import com.rosi.masts.mvc.model.ActionTypes
import com.rosi.masts.mvc.view.resources.StringsProvider
import com.rosi.masts.utils.Logger

abstract class WizardBaseFragment : Fragment(), KeyBindingActivityActor.Listener {
    protected lateinit var logger: Logger
    protected lateinit var actor: KeyBindingActivityActor
    protected lateinit var stringsProvider: StringsProvider

    private val TAG = this::class.java.simpleName

    override fun onAttach(context: Context) {
        super.onAttach(context)
        logger = context.applicationContext.dependencyProvider.logger
        actor = context.applicationContext.dependencyProvider.controller.viewManager.keyBindingActivityActor
        stringsProvider = context.applicationContext.dependencyProvider.stringsProvider

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
        actor.addListener(this)
    }

    override fun onStop() {
        logger.d(TAG, "onStop")
        super.onStop()
        actor.removeListener(this)
    }

    override fun onResume() {
        super.onResume()
        logger.d(TAG, "onResume")

    }

    override fun onPause() {
        super.onPause()
        logger.d(TAG, "onPause")
    }

    override fun onSelectAction(availableActions: Collection<ActionTypes>) {
    }

    override fun onSelectKey() {
    }

    override fun onKeyDetected(key: String, keyDetectedCount: Int) {
    }

    override fun onBindSuccess() {
    }

    override fun onBindComplete() {
    }

    override fun onAvailableActions(actions: Collection<ActionTypes>) {

    }
}
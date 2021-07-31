package com.rosi.masts.di

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.rosi.masts.mvc.view.android.activity.keybinding.KeyBindingViewModel
import java.lang.IllegalArgumentException

class KeyBindingViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(KeyBindingViewModel::class.java)) {
            with(context.dependencyProvider) {
                KeyBindingViewModel(controller.viewManager.keyBindingActivityActor, logger) as T
            }
        } else {
            throw IllegalArgumentException("ViewModel not found")
        }
    }
}
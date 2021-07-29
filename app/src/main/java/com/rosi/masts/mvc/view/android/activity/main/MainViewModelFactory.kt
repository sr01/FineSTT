package com.rosi.masts.mvc.view.android.activity.main

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.rosi.masts.di.dependencyProvider
import com.rosi.masts.mvc.view.resources.StringsProvider
import com.rosi.masts.utils.Logger
import java.lang.IllegalArgumentException

class MainViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            with(context.dependencyProvider) {
                MainViewModel(controller.viewManager.mainActivityActor, stringsProvider, logger) as T
            }
        } else {
            throw IllegalArgumentException("ViewModel not found")
        }
    }
}
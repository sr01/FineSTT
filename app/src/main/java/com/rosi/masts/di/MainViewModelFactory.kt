package com.rosi.masts.di

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.rosi.masts.mvc.view.android.activity.main.MainViewModel
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
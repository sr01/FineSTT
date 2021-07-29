package com.rosi.masts.mvc.view.android.activity.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.rosi.masts.mvc.view.resources.StringsProvider
import com.rosi.masts.utils.Logger
import java.lang.IllegalArgumentException

class MainViewModelFactory(private val actor: MainActivityActor, private val stringsProvider: StringsProvider, private val logger: Logger) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            MainViewModel(actor, stringsProvider, logger) as T
        } else {
            throw IllegalArgumentException("ViewModel not found")
        }
    }
}
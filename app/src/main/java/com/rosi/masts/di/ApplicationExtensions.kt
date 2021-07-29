package com.rosi.masts.di

import android.app.Application
import android.content.Context
import com.rosi.masts.MASApplication

val Context.dependencyProvider: DependencyProvider
    get() = (this.applicationContext as MASApplication).dependencyProvider

val Application.dependencyProvider: DependencyProvider
    get() = (this as MASApplication).dependencyProvider

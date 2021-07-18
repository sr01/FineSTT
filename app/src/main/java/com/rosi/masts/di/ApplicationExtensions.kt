package com.rosi.masts.di

import android.app.Application
import android.content.Context
import com.rosi.masts.MASApplication
import com.rosi.masts.mvc.control.Controller

val Context.dependencyProvider: DependencyProvider
    get() = (this as MASApplication).dependencyProvider

val Application.dependencyProvider: DependencyProvider
    get() = (this as MASApplication).dependencyProvider

val Context.controller: Controller
    get() = (this as MASApplication).controller

val Application.controller: Controller
    get() = (this as MASApplication).controller

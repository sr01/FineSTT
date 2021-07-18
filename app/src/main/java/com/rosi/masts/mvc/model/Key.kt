package com.rosi.masts.mvc.model

import kotlinx.serialization.Polymorphic

@Polymorphic
interface Key {
    val displayName: String
}
package com.rosi.masts.utils

fun <A, B> ifNotNull(a: A?, b: B?, block: (A, B) -> Unit) {
    if (a != null && b != null) {
        block(a, b)
    }
}
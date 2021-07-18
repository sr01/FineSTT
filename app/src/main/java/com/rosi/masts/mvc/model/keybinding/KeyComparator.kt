package com.rosi.masts.mvc.model.keybinding

import com.rosi.masts.mvc.model.mcu.MCUInputKey

interface KeyComparator<T> {
    fun areEquals(x: T, y: T): Boolean
}

object MCUInputKey3Comparator : KeyComparator<MCUInputKey> {
    override fun areEquals(x: MCUInputKey, y: MCUInputKey): Boolean = x.b3 == y.b3
}

object MCUInputKey34Comparator : KeyComparator<MCUInputKey> {
    override fun areEquals(x: MCUInputKey, y: MCUInputKey): Boolean = x.b3 == y.b3 && x.b4 == y.b4
}

object MCUInputKey345Comparator : KeyComparator<MCUInputKey> {
    override fun areEquals(x: MCUInputKey, y: MCUInputKey): Boolean = x.b3 == y.b3 && x.b4 == y.b4 && x.b5 == y.b5
}

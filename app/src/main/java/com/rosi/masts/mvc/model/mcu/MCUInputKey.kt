package com.rosi.masts.mvc.model.mcu

import com.rosi.masts.mvc.model.Key
import kotlinx.serialization.*

@Serializable
data class MCUInputKey(val b3: Byte, val b4: Byte, val b5: Byte) : Key {
    companion object {
        fun fromBytes(bytes: ByteArray, length: Int): MCUInputKey {
            return MCUInputKey(bytes[3], bytes[4], bytes[5])
        }
    }

    override val displayName: String
        get() = "MCU:[$b3,$b4,$b5]"

    override fun toString(): String {
        return "$b3,$b4,$b5"
    }
}
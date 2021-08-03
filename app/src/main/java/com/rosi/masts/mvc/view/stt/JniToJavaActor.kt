package com.rosi.masts.mvc.view.stt

import com.rosi.masts.base.actor.Actor
import com.rosi.masts.base.actor.send
import com.rosi.masts.mvc.control.Controller
import com.rosi.masts.mvc.InputKeyMessage
import com.rosi.masts.mvc.model.mcu.McuToArmCommands
import com.rosi.masts.mvc.model.settings.Settings
import com.rosi.masts.utils.ByteArrayUtils
import com.rosi.masts.base.Logger
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class JniToJavaActor(private val controller: Controller, private val settings: Settings, name: String, logger: Logger, scope: CoroutineScope) : Actor(name, logger, scope) {

    private val tag = "JniToJavaActor"
    private val scope = CoroutineScope(Dispatchers.Default)

    private var lastMcuData: ByteArray? = null
    private var lastMcuDataLength: Int = -1
    private var lastMcuDataTimestamp = 0L

    fun onMcuData(buf: ByteArray, len: Int) {
        scope.launch {
            logger.testPrint(tag, "onMcuData")

            val now = System.currentTimeMillis()
            val areEqual = ByteArrayUtils.areEquals(lastMcuData, lastMcuDataLength, buf, len)
            val isDebounced = now - lastMcuDataTimestamp > settings.getInputKeyDebounceMillis()

            if (!areEqual || isDebounced) {
                when (buf[0]) {
                    McuToArmCommands.InputPacket.value -> {
                        lastMcuData = buf
                        lastMcuDataLength = len
                        lastMcuDataTimestamp = now
                        this@JniToJavaActor send InputKeyMessage(data = buf, length = len) to controller
                    }
                }
            }
        }
    }

    /*
    *   Release     | FF,3,3C       1111 1111, 00000011, 0011 1100
    * -------------------------------
    *   Vol+    1th | B5,2,71       1011 0101, 00000010, 0111 0001
    *           2nd | B4,2,72       1011 0100, 00000010, 0111 0010
    * -------------------------------
    *   Vol-    1th | 2,3,C7        0000 0010, 00000011, 1100 0111
    *           2nd | 1,3,C6        0000 0001, 00000011, 1100 0110
    * -------------------------------
    *   Next    1th | A0,0,68       1010 0000, 00000000, 0110 1000
    *           2nd | A1,0,67       1010 0001, 00000000, 0110 0111
    * -------------------------------
    *   Prev    1th | 58,1,9F       0101 1000, 00000001, 1001 1111
    *           2nd | 59,1,A0       0101 1001, 00000001, 1010 0000
    * -------------------------------
    *   Play/   1th | F8,1,3F       1111 1000, 00000001, 0011 1111
    *   Pause   2nd | F9,1,40       1111 1001, 00000001, 0100 0000
    * -------------------------------
    *
    * */
}


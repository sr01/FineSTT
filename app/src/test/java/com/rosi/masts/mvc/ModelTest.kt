package com.rosi.masts.mvc

import com.rosi.masts.mvc.model.ActionTypes
import com.rosi.masts.mvc.model.mcu.MCUInputKey
import com.rosi.masts.mvc.model.mcu.McuToArmCommands
import com.rosi.masts.mvc.view.android.activity.keybinding.ActionViewData
import com.rosi.masts.mvc.view.android.activity.keybinding.KeyBindingActivityActor
import com.rosi.masts.mvc.view.android.activity.main.MainActivityActor
import com.rosi.masts.test.onceWithTimeout
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Test
import org.mockito.kotlin.*

class ModelTest : ControllerTestBase() {

    @Test
    fun serviceStatusChangedTest() = runBlocking {
        val mainActivityActor = controller.viewManager.mainActivityActor
        val listener: MainActivityActor.Listener = mock()

        // main activity start
        mainActivityActor.addListener(listener)

        val isServiceRunningCaptor = argumentCaptor<Boolean>()
        verify(listener, onceWithTimeout()).onServiceStatusChanged(isServiceRunningCaptor.capture())
        verify(listener, onceWithTimeout()).onShowActions(any())
        assertFalse(isServiceRunningCaptor.firstValue)

        // service start
        clearInvocations(listener)
        serviceActor.start()

        verify(listener, onceWithTimeout()).onServiceStatusChanged(isServiceRunningCaptor.capture())
        assertTrue(isServiceRunningCaptor.secondValue)

        // service stop
        clearInvocations(listener)
        serviceActor.stop()

        verify(listener, onceWithTimeout()).onServiceStatusChanged(isServiceRunningCaptor.capture())
        assertFalse(isServiceRunningCaptor.thirdValue)

        // main activity stop
        mainActivityActor.removeListener(listener)
        verifyNoMoreInteractions(listener)
    }

    @Test
    fun createKeyBindingTest() = runBlocking {
        val mainActivityActor = controller.viewManager.mainActivityActor
        val listener: MainActivityActor.Listener = mock()

        // main activity: start
        mainActivityActor.addListener(listener)

        val actionsCaptor = argumentCaptor<Collection<ActionViewData>>()
        verify(listener, onceWithTimeout()).onServiceStatusChanged(any())
        verify(listener, onceWithTimeout()).onShowActions(actionsCaptor.capture())
        assertEquals(0, actionsCaptor.firstValue.size)
        clearInvocations(listener)

        // main activity: stop
        mainActivityActor.removeListener(listener)

        val keyActionBinding = createKeyBinding()

        // main activity: start
        mainActivityActor.addListener(listener)
        verify(listener, onceWithTimeout()).onServiceStatusChanged(any())
        verify(listener, onceWithTimeout()).onShowActions(actionsCaptor.capture())
        val actionViewData = actionsCaptor.secondValue.first()
        assertEquals(keyActionBinding.actionType, actionViewData.action)
        assertEquals(keyActionBinding.key.displayName, actionViewData.boundKeyName)
    }

    @Test
    fun modifyKeyBindingTest() = runBlocking {
        val mainActivityActor = controller.viewManager.mainActivityActor
        val listener: MainActivityActor.Listener = mock()

        // main activity: start
        mainActivityActor.addListener(listener)

        val actionsCaptor = argumentCaptor<Collection<ActionViewData>>()
        verify(listener, onceWithTimeout()).onServiceStatusChanged(any())
        verify(listener, onceWithTimeout()).onShowActions(actionsCaptor.capture())
        assertEquals(0, actionsCaptor.firstValue.size)
        clearInvocations(listener)

        // main activity: stop
        mainActivityActor.removeListener(listener)

        val createdKeyActionBinding = createKeyBinding()

        // main activity: start
        mainActivityActor.addListener(listener)
        verify(listener, onceWithTimeout()).onServiceStatusChanged(any())
        verify(listener, onceWithTimeout()).onShowActions(actionsCaptor.capture())
        val actionViewData = actionsCaptor.secondValue.first()
        assertEquals(createdKeyActionBinding.actionType, actionViewData.action)
        assertEquals(createdKeyActionBinding.key.displayName, actionViewData.boundKeyName)
        clearInvocations(listener)

        // main activity: stop
        mainActivityActor.removeListener(listener)

        val modifiedKeyActionBinding = modifyKeyBinding(actionViewData.bindingID!!)

        // main activity: start
        mainActivityActor.addListener(listener)
        verify(listener, onceWithTimeout()).onServiceStatusChanged(any())
        verify(listener, onceWithTimeout()).onShowActions(actionsCaptor.capture())
        val modifiedActionViewData = actionsCaptor.thirdValue.first()
        assertEquals(actionViewData.bindingID, modifiedActionViewData.bindingID)
        assertEquals(modifiedKeyActionBinding.key.displayName, modifiedActionViewData.boundKeyName)
    }

    private fun modifyKeyBinding(bindingID: String): KeyBindingIDPair {
        val keyBindingActivityActor = controller.viewManager.keyBindingActivityActor
        val listener: KeyBindingActivityActor.Listener = mock()

        //KeyBindingActivity: start (modify binding)
        keyBindingActivityActor.addListener(listener)
        keyBindingActivityActor.startBindingForBindingID(bindingID)

        verify(listener, onceWithTimeout()).onSelectKey()

        // SelectKeyFragment: start
        // user press a key
        val keyData = byteArrayOf(McuToArmCommands.InputPacket.value, 6, 7, 8, 9, 10)
        controller.viewManager.jniToJavaActor.onMcuData(keyData, keyData.size)

        val keyCaptor = argumentCaptor<String>()
        val mcuKey = MCUInputKey.fromBytes(keyData, keyData.size)
        verify(listener, onceWithTimeout()).onKeyDetected(keyCaptor.capture(), any())
        assertEquals(mcuKey.toString(), keyCaptor.firstValue)

        // SelectKeyFragment: bind
        keyBindingActivityActor.keySelected()
        verify(listener, onceWithTimeout()).onBindSuccess()
        verify(listener, onceWithTimeout()).onBindComplete()

        //KeyBindingActivity: stop
        keyBindingActivityActor.stopBinding()
        keyBindingActivityActor.removeListener(listener)
        verifyNoMoreInteractions(listener)

        return KeyBindingIDPair(key = mcuKey, bindingID = bindingID)
    }

    private fun createKeyBinding(): KeyActionPair {
        val keyBindingActivityActor = controller.viewManager.keyBindingActivityActor
        val listener: KeyBindingActivityActor.Listener = mock()

        //KeyBindingActivity: start (create binding)
        keyBindingActivityActor.addListener(listener)
        keyBindingActivityActor.startBinding()

        val availableActionsCaptor = argumentCaptor<Collection<ActionTypes>>()
        verify(listener, onceWithTimeout()).onSelectAction(availableActionsCaptor.capture())
        assertEquals(ActionTypes.values().size, availableActionsCaptor.firstValue.size)

        // SelectActionFragment: start
        keyBindingActivityActor.getAvailableActions()
        verify(listener, onceWithTimeout()).onAvailableActions(availableActionsCaptor.capture())
        assertEquals(ActionTypes.values().size, availableActionsCaptor.secondValue.size)

        // SelectActionFragment: select action
        val action = availableActionsCaptor.firstValue.first()
        keyBindingActivityActor.actionSelected(action)

        verify(listener, onceWithTimeout()).onSelectKey()

        // SelectKeyFragment: start
        // user press a key
        val keyData = byteArrayOf(McuToArmCommands.InputPacket.value, 1, 2, 3, 4, 5)
        controller.viewManager.jniToJavaActor.onMcuData(keyData, keyData.size)

        val keyCaptor = argumentCaptor<String>()
        val mcuKey = MCUInputKey.fromBytes(keyData, keyData.size)
        verify(listener, onceWithTimeout()).onKeyDetected(keyCaptor.capture(), any())
        assertEquals(mcuKey.toString(), keyCaptor.firstValue)

        // SelectKeyFragment: bind
        keyBindingActivityActor.keySelected()
        verify(listener, onceWithTimeout()).onBindSuccess()
        verify(listener, onceWithTimeout()).onBindComplete()

        //KeyBindingActivity: stop
        keyBindingActivityActor.stopBinding()
        keyBindingActivityActor.removeListener(listener)
        verifyNoMoreInteractions(listener)

        return KeyActionPair(key = mcuKey, actionType = action)
    }

    data class KeyActionPair(val key: MCUInputKey, val actionType: ActionTypes)
    data class KeyBindingIDPair(val key: MCUInputKey, val bindingID: String)
}
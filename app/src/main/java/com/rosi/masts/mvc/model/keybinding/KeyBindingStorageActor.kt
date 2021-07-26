package com.rosi.masts.mvc.model.keybinding

import com.rosi.masts.base.actor.*
import com.rosi.masts.mvc.*
import com.rosi.masts.mvc.control.Controller
import com.rosi.masts.mvc.model.ActionTypes
import com.rosi.masts.mvc.model.Key
import com.rosi.masts.mvc.model.ModelManager
import com.rosi.masts.mvc.model.mcu.MCUInputKey
import com.rosi.masts.mvc.model.settings.Settings
import com.rosi.masts.utils.DateTimeProvider
import com.rosi.masts.utils.Logger
import com.rosi.masts.utils.TextFileReadWrite
import kotlinx.coroutines.CoroutineScope
import kotlinx.serialization.*
import kotlinx.serialization.json.*
import kotlinx.serialization.modules.SerializersModule
import java.io.File

open class KeyBindingStorageActor(private val controller: Controller,
                                  private val modelManager: ModelManager,
                                  private val settings: Settings,
                                  private val textFileReadWrite: TextFileReadWrite,
                                  private val dateTimeProvider: DateTimeProvider,
                                  name: String,
                                  override val logger: Logger,
                                  scope: CoroutineScope) :
    Actor(name, logger, scope) {

    private val tag = "KeyBindingStorageActor"
    private val storage = KeyBindingStorage()

    init {
        load()
    }

    override suspend fun receive(message: Message) {
        super.receive(message)

        when (message) {
            is AddOrUpdateKeyActionBindingMessage -> addOrUpdateKeyActionBindingMessage(message.key, message.action, message.bindingID)
            is GetKeyActionBindingsMessage -> getKeyActionBindingsMessage(message)
            is StartKeyBindingForActionMessage -> startKeyBindingForActionMessage(message, message.bindingID)
            is SelectActionMessage -> selectActionMessage(message)
            is GetAvailableActionsMessage -> getAvailableActionsMessage(message, message.recipient)
            is RemoveKeyActionBindingMessage -> removeKeyActionBindingMessage(message, message.bindingID)
            is InputKeyMessage -> if (message.key != null) inputKeyMessage(message, message.key)
            is ExportKeyBindingsMessage -> exportKeyBindingsMessage()
            is ImportKeyBindingsMessage -> importKeyBindingsMessage(message, message.file)
            else -> printUnknownMessage(message)
        }
    }

    private fun inputKeyMessage(message: InputKeyMessage, key: MCUInputKey) {
        val action = storage.getByKey(key)?.actionType
        logger.d(tag, "inputKeyMessage, found action: $action, for key: $key")
        this send message.withAction(action) to controller
    }

    private suspend fun addOrUpdateKeyActionBindingMessage(key: MCUInputKey, actionType: ActionTypes, bindingID: String?) {
        logger.testPrint(tag,
            "addOrUpdateKeyActionBindingMessage, key: $key, actionType: $actionType, bindingID: $bindingID, settings.isMultipleKeysPerActionAllowed: ${settings.isMultipleKeysPerActionAllowed()}")

        val keyActionBinding = storage.addOrUpdateKey(bindingID, key, actionType, settings.isMultipleKeysPerActionAllowed())
        save()

        logger.testPrint(tag, "addOrUpdateKeyActionBindingMessage, saved keyActionBinding: $keyActionBinding")
        this send BindSuccessMessage(keyActionBinding = keyActionBinding) to modelManager
    }

    private fun getKeyActionBindingsMessage(message: GetKeyActionBindingsMessage) {
        val bindings = storage.getAll()
        this send message.withActions(bindings) to message.recipient
    }

    private suspend fun startKeyBindingForActionMessage(message: StartKeyBindingForActionMessage, bindingID: String) {
        val keyActionBinding = storage.getByID(bindingID)
        if (keyActionBinding != null) {
            this send message.withKeyActionBinding(keyActionBinding) to modelManager
        } else {
            logger.e(tag, "no key action binding found with id: $bindingID")
        }
    }

    private fun selectActionMessage(message: SelectActionMessage) {
        val availableActions = getAvailableActions()
        this send message.withAvailableActions(availableActions) to controller
    }

    private fun getAvailableActionsMessage(message: GetAvailableActionsMessage, recipient: ISender) {
        val availableActions = getAvailableActions()
        this send message.withAvailableActions(availableActions) to recipient
    }

    private fun removeKeyActionBindingMessage(message: RemoveKeyActionBindingMessage, bindingID: String) {
        logger.testPrint(tag, "removeKeyActionBindingMessage, bindingID: $bindingID")
        val removedBinding = storage.removeByID(bindingID)
        save()
        this send message.withRemovedBindings(listOfNotNull(removedBinding)) to controller
    }

    private fun exportKeyBindingsMessage() {
        val json = serializer.encodeToString(storage)
        val filename = "FineSTT-${dateTimeProvider.currentDateTimeForFilename()}.json"
        textFileReadWrite.write(filename, json)
    }

    private fun importKeyBindingsMessage(message: ImportKeyBindingsMessage, file: File) {
        try {
            val json = textFileReadWrite.read(file)
            val loadedStorage = serializer.decodeFromString<KeyBindingStorage>(json)
            this.storage.replaceAll(loadedStorage)
            save()
            val bindings = storage.getAll()
            this send message.withActions(bindings) to controller
        } catch (e: Exception) {
            logger.e(tag, "failed to import from file: $file", e)
        }
    }

    private fun getAvailableActions(): Collection<ActionTypes> {
        val allActions: Array<ActionTypes> = ActionTypes.values()

        return if (settings.isMultipleKeysPerActionAllowed()) {
            allActions.toList()
        } else {
            val boundActions = storage.getAll().map { it.actionType }
            allActions.subtract(boundActions)
        }
    }

    private fun load() {
        try {
            val json = settings.getKeyBindingsJSON()
            val loadedStorage = serializer.decodeFromString<KeyBindingStorage>(json)
            this.storage.addAll(loadedStorage)
        } catch (e: Exception) {
            logger.e(tag, "failed to parse json into key binding map", e)
        }
    }

    private fun save() {
        val json = serializer.encodeToString(storage)
        settings.setKeyBindingsJSON(json)
    }

    companion object {
        private val serializer = Json {
            allowStructuredMapKeys = true
            serializersModule = SerializersModule {
                polymorphic(Key::class, MCUInputKey::class, MCUInputKey.serializer())
            }
        }
    }
}


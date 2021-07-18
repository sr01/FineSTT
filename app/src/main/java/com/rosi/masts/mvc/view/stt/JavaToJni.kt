@file:Suppress("FunctionName")

package com.rosi.masts.mvc.view.stt

interface JavaToJni {
    fun JavaToJniInit()

    fun InitArmMcuCom()

    fun isArmMcuComOpen(): Int
}
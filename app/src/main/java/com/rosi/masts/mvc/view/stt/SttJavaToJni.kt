package com.rosi.masts.mvc.view.stt

import com.stt.JavaJni.JavaToJni

object SttJavaToJni : com.rosi.masts.mvc.view.stt.JavaToJni {
    override fun JavaToJniInit() {
        JavaToJni.JavaToJniInit()
    }

    override fun InitArmMcuCom() {
        JavaToJni.InitArmMcuCom()
    }

    override fun isArmMcuComOpen(): Int = JavaToJni.isArmMcuComOpen()
}
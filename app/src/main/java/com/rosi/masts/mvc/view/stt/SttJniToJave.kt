package com.rosi.masts.mvc.view.stt

import com.rosi.masts.utils.Logger
import com.stt.JavaJni.JniToJava

object SttJniToJave : com.rosi.masts.mvc.view.stt.JniToJava {
    override fun init(actor: JniToJavaActor, logger: Logger) {
        JniToJava.init(actor, logger)
    }
}
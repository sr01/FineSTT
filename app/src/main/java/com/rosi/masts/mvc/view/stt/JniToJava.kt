package com.rosi.masts.mvc.view.stt

import com.rosi.masts.utils.Logger

interface JniToJava {
    fun init(actor: JniToJavaActor, logger: Logger)
}
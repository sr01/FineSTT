package com.rosi.masts.utils

import java.io.File

interface TextFileReadWrite {

    fun write(filename: String, text: String)

    fun read(file: File): String
}

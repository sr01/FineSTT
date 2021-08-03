package com.rosi.masts.utils.android

import android.content.Context
import android.os.Environment
import android.widget.Toast
import com.rosi.masts.base.Logger
import com.rosi.masts.utils.TextFileReadWrite
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileReader
import java.io.FileWriter

class AndroidTextFileReadWrite(private val context: Context, private val logger: Logger, private val mainScope: CoroutineScope) : TextFileReadWrite {
    private val tag = "AndroidFileSystem"

    override fun write(filename: String, text: String) {
        val file = File(getWorkingDirectory(context), filename)

        logger.d(tag, "writing data to file: $file")

        val writer = FileWriter(file)
        writer.use {
            it.write(text)
            mainScope.launch {
                Toast.makeText(context, "data exported to: $file", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun read(file: File): String {
        logger.d(tag, "reading data from file: $file")
        val reader = FileReader(file)
        reader.use {
            val json = it.readText()
            logger.d(tag, "read data: $json")
            return json
        }
    }

    companion object {
        fun getWorkingDirectory(context: Context): File? {
            return context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)
        }
    }
}
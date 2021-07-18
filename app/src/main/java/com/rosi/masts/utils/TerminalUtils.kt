package com.rosi.masts.utils

import android.util.Log
import java.io.BufferedReader
import java.io.DataOutputStream
import java.io.InputStreamReader
import java.lang.StringBuilder

object TerminalUtils : RootChecker{
    private const val tag = "TerminalUtils"

    fun runAsRoot(cmds: Array<String>) {
        try {
            val p = Runtime.getRuntime().exec("su")
            val os = DataOutputStream(p.outputStream)
            val bf = BufferedReader(InputStreamReader(p.inputStream))
            for (it in cmds) {
                os.writeBytes(it + "\n")
                os.flush()

                val text = p.inputStream.readTextWithTimeout()
                Log.i(tag, text)
            }

            //os.writeBytes("exit\n");
            os.flush()

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun runAsRootReturnResult(vararg cmds: String): Result<String> {
        val sb = StringBuilder()

        try {
            val p = Runtime.getRuntime().exec("su")
            val os = DataOutputStream(p.outputStream)

            for (tmpCmd in cmds) {
                os.writeBytes(tmpCmd + "\n")
                os.flush()

                val text = p.inputStream.readTextWithTimeout()
                sb.append(text)
            }

            os.flush()

            return if (sb.isNotEmpty()) {
                Result.Success(sb.toString())
            } else {
                Result.Failure(Exception("failed to execute command, probably no root"))
            }

        } catch (e: Exception) {
            e.printStackTrace()
            return Result.Failure(e)
        }
    }

    override fun isRooted(): Boolean {
        return when (runAsRootReturnResult("ls /data/data")) {
            is Result.Success -> true
            is Result.Failure -> false
        }
    }

    fun isProcessExist(processName: String): Boolean? {
        return when (val result = runAsRootReturnResult("ps -A")) {
            is Result.Success -> result.data.contains(processName)
            is Result.Failure -> null
        }
    }

    sealed class Result<T>(open val data: T?, open val exception: Exception?) {
        data class Success<T>(override val data: T) : Result<T>(data, null)
        data class Failure<T>(override val exception: Exception) : Result<T>(null, exception)
    }
}

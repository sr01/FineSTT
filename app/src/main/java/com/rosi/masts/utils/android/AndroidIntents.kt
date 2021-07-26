package com.rosi.masts.utils.android

import android.content.Intent

object AndroidIntents {
    private const val MIME_PLAINTEXT = "text/plain"

    fun newShareIntent(text: String, subject: String): Intent {
        return Intent(Intent.ACTION_SEND).apply {
            putExtra(Intent.EXTRA_TEXT, text)
            putExtra(Intent.EXTRA_SUBJECT, subject)
            type = MIME_PLAINTEXT
        }
    }

    fun newEmailIntent(recipient: Array<String>, subject: String, body: String): Intent {
        return Intent(Intent.ACTION_SEND).apply {
            putExtra(Intent.EXTRA_EMAIL, recipient)
            putExtra(Intent.EXTRA_SUBJECT, subject)
            putExtra(Intent.EXTRA_TEXT, body)
            type = MIME_PLAINTEXT
        }
    }
}
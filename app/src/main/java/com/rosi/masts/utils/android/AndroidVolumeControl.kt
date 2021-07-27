package com.rosi.masts.utils.android

import android.content.Context
import android.media.AudioManager
import com.rosi.masts.utils.VolumeControl


class AndroidVolumeControl(context: Context) : VolumeControl {
    private val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager

    override fun volumeUp() {
        audioManager.adjustVolume(AudioManager.ADJUST_RAISE, AudioManager.FLAG_PLAY_SOUND)
    }

    override fun volumeDown() {
        audioManager.adjustVolume(AudioManager.ADJUST_LOWER, AudioManager.FLAG_PLAY_SOUND)
    }

    override fun mute() {
        audioManager.adjustVolume(AudioManager.ADJUST_TOGGLE_MUTE, AudioManager.FLAG_PLAY_SOUND)
    }
}
package com.example.mymusiconmobile

import android.media.MediaPlayer

object MyMediaPlayer {
    var instance: MediaPlayer? = null
    fun getInstanz(): MediaPlayer? {
        if (instance == null) {
            instance = MediaPlayer()
        }
        return instance
    }

    var currentIndex = -1
}
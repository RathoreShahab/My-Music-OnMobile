package com.example.mymusiconmobile.feature.musicplayer

import android.app.Application
import android.content.Intent
import android.media.MediaPlayer
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.mymusiconmobile.MusicFile
import com.example.mymusiconmobile.MyMediaPlayer
import java.io.IOException
import java.util.concurrent.TimeUnit

class MusicPlayerViewModel(val appContext: Application) : AndroidViewModel(appContext) {

    var songsList: ArrayList<MusicFile>? = null
    var currentSong: MusicFile? = null
    var mediaPlayer: MediaPlayer = MyMediaPlayer.getInstanz()!!
    var currentIndex = mediaPlayer.currentPosition

    private var observerPlay: MutableLiveData<Unit> = MutableLiveData()

    fun getObserverPlay() = observerPlay


    fun init(intent: Intent?) {
        songsList = intent?.getSerializableExtra("LIST") as ArrayList<MusicFile>?
        currentIndex = intent?.getIntExtra("POSITION", 0) ?: 0
        currentSong = songsList?.get(currentIndex)
    }


     fun playMusic() {
        mediaPlayer?.reset()
        try {
            currentSong = songsList?.get(currentIndex)
            mediaPlayer?.setDataSource(currentSong?.path)
            mediaPlayer?.prepare()
            mediaPlayer?.start()
            observerPlay.postValue(Unit)
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    fun playNextSong() {
        if (currentIndex == songsList!!.size - 1) return
        mediaPlayer?.reset()
        currentIndex += 1
        playMusic()
    }

    fun playPreviousSong() {
        if (currentIndex == 0) return
        mediaPlayer?.reset()
        currentIndex -= 1
        playMusic()
    }

    fun pausePlay() {
        if (mediaPlayer?.isPlaying() == true)
            mediaPlayer?.pause()
        else
            mediaPlayer?.start()
    }

    fun convertToMMSS(duration: String): String {
        val millis = duration.toLong()
        return String.format(
            "%02d:%02d",
            TimeUnit.MILLISECONDS.toMinutes(millis) % TimeUnit.HOURS.toMinutes(1),
            TimeUnit.MILLISECONDS.toSeconds(millis) % TimeUnit.MINUTES.toSeconds(1)
        )
    }

    override fun onCleared() {
        super.onCleared()
    }
}
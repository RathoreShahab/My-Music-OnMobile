package com.example.mymusiconmobile

import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import androidx.appcompat.app.AppCompatActivity
import com.example.mymusiconmobile.databinding.ActivityMusicPlayerBinding
import java.io.IOException
import java.util.concurrent.TimeUnit

class MusicPlayerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMusicPlayerBinding
    var songsList: ArrayList<MusicFile>? = null
    var currentSong: MusicFile? = null
    var mediaPlayer: MediaPlayer? = MyMediaPlayer.getInstanz()
    var x = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMusicPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        songsList = intent.getSerializableExtra("LIST") as ArrayList<MusicFile>?
        binding.songTitle.isSelected = true
        setResourcesWithMusic()

        runOnUiThread(object : Runnable {
            override fun run() {
                if (mediaPlayer != null) {
                    binding.seekBar.setProgress(mediaPlayer!!.currentPosition)
                    binding.currentTime.setText(convertToMMSS(mediaPlayer!!.currentPosition.toString() + ""))
                    if (mediaPlayer!!.isPlaying) {
                        binding.pausePlay.setImageResource(R.drawable.ic_baseline_pause_circle_outline_24)
                        binding.musicIconBig.setRotation(x++.toFloat())
                    } else {
                        binding.pausePlay.setImageResource(R.drawable.ic_baseline_play_circle_outline_24)
                        binding.musicIconBig.setRotation(0f)
                    }
                }
                Handler().postDelayed(this, 100)
            }
        })

        binding.seekBar.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                if (mediaPlayer != null && fromUser) {
                    mediaPlayer!!.seekTo(progress)
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {}
            override fun onStopTrackingTouch(seekBar: SeekBar) {}
        })
    }

    fun setResourcesWithMusic() {
        currentSong = songsList!![MyMediaPlayer.currentIndex]
        binding.songTitle.setText(currentSong?.title)
        binding.totalTime.setText(convertToMMSS(currentSong?.duration?: "0"))
        binding.pausePlay.setOnClickListener(View.OnClickListener { v: View? -> pausePlay() })
        binding.next.setOnClickListener(View.OnClickListener { v: View? -> playNextSong() })
        binding.previous.setOnClickListener(View.OnClickListener { v: View? -> playPreviousSong() })
        playMusic()
    }


    private fun playMusic() {
        mediaPlayer?.reset()
        try {
            mediaPlayer?.setDataSource(currentSong?.path)
            mediaPlayer?.prepare()
            mediaPlayer?.start()
            binding.seekBar.setProgress(0)
            binding.seekBar.setMax(mediaPlayer?.duration ?: 0)
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun playNextSong() {
        if (MyMediaPlayer.currentIndex == songsList!!.size - 1) return
        MyMediaPlayer.currentIndex += 1
        mediaPlayer?.reset()
        setResourcesWithMusic()
    }

    private fun playPreviousSong() {
        if (MyMediaPlayer.currentIndex == 0) return
        MyMediaPlayer.currentIndex -= 1
        mediaPlayer?.reset()
        setResourcesWithMusic()
    }

    private fun pausePlay() {
        if (mediaPlayer?.isPlaying() == true)
            mediaPlayer?.pause()
        else
            mediaPlayer?.start()
    }

    companion object {
        fun convertToMMSS(duration: String): String {
            val millis = duration.toLong()
            return String.format(
                "%02d:%02d",
                TimeUnit.MILLISECONDS.toMinutes(millis) % TimeUnit.HOURS.toMinutes(1),
                TimeUnit.MILLISECONDS.toSeconds(millis) % TimeUnit.MINUTES.toSeconds(1)
            )
        }
    }
}
package com.example.mymusiconmobile.feature.musicplayer

import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.mymusiconmobile.R
import com.example.mymusiconmobile.databinding.ActivityMusicPlayerBinding

class MusicPlayerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMusicPlayerBinding
    private lateinit var viewModel: MusicPlayerViewModel
    var x = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMusicPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this).get(MusicPlayerViewModel::class.java)

        viewModel.init(intent)
        listeners()
        observers()

        binding.songTitle.isSelected = true
        setResourcesWithMusic()

        runOnUiThread(object : Runnable {
            override fun run() {
                if (viewModel.mediaPlayer != null) {
                    binding.seekBar.progress = viewModel.mediaPlayer!!.currentPosition
                    binding.currentTime.text = viewModel.convertToMMSS(viewModel.mediaPlayer!!.currentPosition.toString() + "")
                    if (viewModel.mediaPlayer!!.isPlaying) {
                        binding.pausePlay.setImageResource(R.drawable.ic_baseline_pause_circle_outline_24)
                        binding.musicIconBig.rotation = x++.toFloat()
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
                if (viewModel.mediaPlayer != null && fromUser) {
                    viewModel.mediaPlayer!!.seekTo(progress)
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {}
            override fun onStopTrackingTouch(seekBar: SeekBar) {}
        })
    }

    private fun observers() {

        viewModel.getObserverPlay().observe(this) {
            binding.songTitle.text = viewModel.currentSong?.title
            binding.totalTime.text = viewModel.convertToMMSS(viewModel.currentSong?.duration ?: "0")
            binding.seekBar.progress = 0
            binding.seekBar.max = viewModel.mediaPlayer?.duration ?: 0
        }

    }

    private fun listeners() {
        binding.pausePlay.setOnClickListener(View.OnClickListener { v: View? -> viewModel.pausePlay() })
        binding.next.setOnClickListener(View.OnClickListener { v: View? -> viewModel.playNextSong() })
        binding.previous.setOnClickListener(View.OnClickListener { v: View? -> viewModel.playPreviousSong() })
    }

    fun setResourcesWithMusic() {
        viewModel.playMusic()
    }

    override fun onStop() {
        super.onStop()
        viewModel.mediaPlayer.pause()
    }
    override fun onDestroy() {
        super.onDestroy()
        viewModel.mediaPlayer.release()
    }

}
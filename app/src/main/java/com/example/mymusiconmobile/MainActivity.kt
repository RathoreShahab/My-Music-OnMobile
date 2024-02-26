package com.example.mymusiconmobile

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mymusiconmobile.adapter.MusicAdapter
import com.example.mymusiconmobile.databinding.ActivityMainBinding
import com.example.mymusiconmobile.viewmodel.MainActivityViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (!checkPermission()) {
            requestPermission()
            return
        }

        viewModel = ViewModelProvider(this).get(MainActivityViewModel::class.java)

        val musicAdapter = MusicAdapter { musicFile,position ->

            //navigate to another acitivty
            viewModel.startMusicActivity(this,musicFile, position)
        }

        binding.recyclerView.apply {
            adapter = musicAdapter
            layoutManager = LinearLayoutManager(this@MainActivity)
        }

        binding.recyclerView.apply {
            adapter = musicAdapter
            layoutManager = LinearLayoutManager(this@MainActivity)
        }

        viewModel.getMusicFiles().let { musicFiles ->
            musicAdapter.submitList(musicFiles)
        }

    }

    fun checkPermission(): Boolean {
        val result = ContextCompat.checkSelfPermission(this@MainActivity, Manifest.permission.READ_EXTERNAL_STORAGE)
        return result == PackageManager.PERMISSION_GRANTED
    }

    fun requestPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this@MainActivity, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            Toast.makeText(
                this@MainActivity,
                "READ PERMISSION IS REQUIRED,PLEASE ALLOW FROM SETTTINGS",
                Toast.LENGTH_SHORT
            ).show()
        } else
            ActivityCompat.requestPermissions(
            this@MainActivity,
            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
            123
        )
    }


}
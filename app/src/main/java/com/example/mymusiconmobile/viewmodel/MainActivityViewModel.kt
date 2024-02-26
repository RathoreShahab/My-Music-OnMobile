package com.example.mymusiconmobile.viewmodel

import android.app.Application
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore
import androidx.lifecycle.AndroidViewModel
import com.example.mymusiconmobile.MusicFile
import com.example.mymusiconmobile.MusicPlayerActivity
import com.example.mymusiconmobile.MyMediaPlayer

class MainActivityViewModel(val appContext: Application) : AndroidViewModel(appContext) {

    private var musicList: List<MusicFile> = mutableListOf()

    fun getMusicFiles(): List<MusicFile> {
        val musicFiles = mutableListOf<MusicFile>()
        val uri: Uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
        val projection = arrayOf(
            MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.DATA,
            MediaStore.Audio.Media.DURATION
        )

        val selection = MediaStore.Audio.Media.IS_MUSIC + " != 0"
        val cursor: Cursor? = appContext.contentResolver.query(
            uri,
            projection,
            selection,
            null,
            null
        )
        cursor?.use {
            val titleColumn = it.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE)
            val pathColumn = it.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA)
            val durationColumn = it.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION)
            while (it.moveToNext()) {
                val title = it.getString(titleColumn)
                val path = it.getString(pathColumn)
                val duration = it.getString(durationColumn)
                musicFiles.add(MusicFile(title, path, duration))
            }
        }
        musicList = musicFiles
        return musicFiles
    }


    override fun onCleared() {
        super.onCleared()
    }

    fun startMusicActivity(baseContext: Context, musicFile: MusicFile, position: Int) {
        MyMediaPlayer.getInstanz()?.reset()
        MyMediaPlayer.currentIndex = position
        val intent = Intent(baseContext, MusicPlayerActivity::class.java)
        intent.putExtra("LIST", musicList.toMutableList() as ArrayList<MusicFile>)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        baseContext.startActivity(intent)
    }
}

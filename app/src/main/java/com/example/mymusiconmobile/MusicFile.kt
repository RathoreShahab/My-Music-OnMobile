package com.example.mymusiconmobile

import java.io.Serializable

data class MusicFile(val title: String, val path: String, val duration: String) : Serializable

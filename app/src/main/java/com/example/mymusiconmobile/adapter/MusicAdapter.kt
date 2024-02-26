package com.example.mymusiconmobile.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.mymusiconmobile.MusicFile
import com.example.mymusiconmobile.MyMediaPlayer
import com.example.mymusiconmobile.databinding.RecyclerItemBinding

class MusicAdapter(private val onItemClick: (MusicFile, Position:Int) -> Unit) :
    ListAdapter<MusicFile, MusicAdapter.MusicViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MusicViewHolder {
        val binding =
            RecyclerItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MusicViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MusicViewHolder, position: Int) {
        val musicFile = getItem(position)
        holder.bind(musicFile)
        holder.itemView.setOnClickListener { onItemClick(musicFile, position) }
    }

    inner class MusicViewHolder(private val binding: RecyclerItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(musicFile: MusicFile) {
            binding.apply {
                musicTitleText.text = musicFile.title
                if (MyMediaPlayer.currentIndex == position) {
                    musicTitleText.setTextColor(Color.parseColor("#FF0000"))
                } else {
                    musicTitleText.setTextColor(Color.parseColor("#000000"))
                }
            }
        }
    }

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<MusicFile>() {
            override fun areItemsTheSame(oldItem: MusicFile, newItem: MusicFile): Boolean {
                return oldItem.title == newItem.title
            }

            override fun areContentsTheSame(oldItem: MusicFile, newItem: MusicFile): Boolean {
                return oldItem == newItem
            }
        }
    }
}

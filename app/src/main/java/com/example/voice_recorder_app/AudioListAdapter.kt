package com.example.voice_recorder_app

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.io.File

class AudioListAdapter(private val allFiles: Array<File>): RecyclerView.Adapter<AudioListAdapter.AudioViewHolder>() {

    lateinit var listener: OnItemListClick

    class AudioViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val list_image: ImageView = itemView.findViewById(R.id.list_image_view)
        val list_title: TextView = itemView.findViewById(R.id.list_title)
        val list_date: TextView = itemView.findViewById(R.id.list_date)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AudioViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.single_list_item, parent, false)
        return AudioViewHolder(view)
    }

    override fun getItemCount(): Int {
        return allFiles.size
    }

    override fun onBindViewHolder(holder: AudioViewHolder, position: Int) {
        val timeAgo = TimeAgo()
        holder.list_title.text = allFiles[position].name
        holder.list_date.text = timeAgo.getTimeAgo(allFiles[position].lastModified())
        holder.itemView.setOnClickListener {
            listener.onClickListener(it, allFiles[position], position)
        }
    }

    interface OnItemListClick {
        fun onClickListener(view: View, file: File, position: Int)
    }

    fun setOnItemClickListener(listener: OnItemListClick) {
        this.listener = listener
    }
}
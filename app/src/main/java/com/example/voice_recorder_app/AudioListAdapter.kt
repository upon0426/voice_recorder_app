package com.example.voice_recorder_app

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.io.File

class AudioListAdapter(
    var allFiles: Array<File>?
): RecyclerView.Adapter<AudioListAdapter.AudioViewHolder>() {
    class AudioViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val list_image = itemView.findViewById<ImageView>(R.id.list_image_view)
        val list_title = itemView.findViewById<TextView>(R.id.list_title)
        val list_date = itemView.findViewById<TextView>(R.id.list_date)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AudioViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.single_list_item, parent, false)
        return AudioViewHolder(view)
    }

    override fun onBindViewHolder(holder: AudioViewHolder, position: Int) {
        val timeAgo = TimeAgo()
        holder.list_title.text = allFiles?.get(position)?.name
        holder.list_date.text = allFiles?.get(position)?.lastModified()?.let { timeAgo.getTimeAgo(it) }
    }

    override fun getItemCount(): Int {
        return if (allFiles.isNullOrEmpty()) {
            0
        } else {
            allFiles!!.size
        }
    }
}
package com.example.skmn.videos

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.RecyclerView
import com.example.skmn.R
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView

class ListAdapter(private val itemList: ArrayList<ListLayout>, private val lifecycle: Lifecycle): RecyclerView.Adapter<ListAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_layout, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.cueVideo(itemList[position].videoId)
        holder.title.text = itemList[position].title
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private var youTubePlayer: YouTubePlayer? = null
        private var currentVideoId: String? = null
        val title: TextView = itemView.findViewById(R.id.title)
        val yt: YouTubePlayerView = itemView.findViewById(R.id.youtube_player_view)

        fun cueVideo(videoId: String?) {
            currentVideoId = videoId
            if (youTubePlayer == null) return
            youTubePlayer!!.cueVideo(videoId!!, 0f)
        }

        init {
            yt.addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
                override fun onReady(initializedYouTubePlayer: YouTubePlayer) {
                    youTubePlayer = initializedYouTubePlayer
                    youTubePlayer!!.cueVideo(currentVideoId!!, 0f)
                }
            })
        }

    }
}
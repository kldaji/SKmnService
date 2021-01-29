package com.example.skmn.videos

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName="video")
data class VideoEntity(
    @PrimaryKey(autoGenerate = true)
    var id: Long?,
    @ColumnInfo(name = "video_id")
    var videoId: String = "",
    @ColumnInfo(name = "title")
    var title: String = "",
    @ColumnInfo(name = "category")
    var category: String = ""
)
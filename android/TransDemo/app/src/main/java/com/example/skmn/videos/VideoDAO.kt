package com.example.skmn.videos

import androidx.room.*

@Dao
interface VideoDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(video: VideoEntity)

    @Query("SELECT * FROM video")
    fun getAll(): List<VideoEntity>

    @Delete
    fun delete(video: VideoEntity)

    @Query("SELECT * FROM video WHERE category = :key")
    fun getCategory(key: String): List<VideoEntity>
}
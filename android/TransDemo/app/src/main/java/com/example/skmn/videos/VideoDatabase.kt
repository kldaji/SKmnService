package com.example.skmn.videos

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = arrayOf(VideoEntity::class), version = 1)
abstract class VideoDatabase: RoomDatabase() {
    abstract fun videoDAO(): VideoDAO

    // singleton pattern
    companion object{
        var INSTANCE: VideoDatabase? = null

        fun getInstance(context: Context): VideoDatabase?{
            if(INSTANCE == null){
                synchronized(VideoDatabase::class){
                    INSTANCE = Room.databaseBuilder(context.applicationContext,
                    VideoDatabase::class.java, "video.db")
                        .fallbackToDestructiveMigration()
                        .build()
                }
            }

            return INSTANCE
        }
    }
}
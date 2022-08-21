package com.example.movieapp.databaseremmovie

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [RemMovie::class], version = 1, exportSchema = false)
abstract class RemMovieDatabase : RoomDatabase(){
    abstract fun remMovieDao() : RemMovieDao
    companion object{
        @Volatile
        private var INSTANCE : RemMovieDatabase? = null
        fun getInstance(context: Context) : RemMovieDatabase {
            synchronized(this) {
                var instance = INSTANCE
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        RemMovieDatabase::class.java,
                        "rem_movie_data_database_new"
                    ).allowMainThreadQueries().build()
                }
                return instance
            }
        }
    }
}
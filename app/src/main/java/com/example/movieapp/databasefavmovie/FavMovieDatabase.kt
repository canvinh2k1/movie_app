package com.example.movieapp.databasefavmovie

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [FavMovie::class], version = 1, exportSchema = false)
abstract class FavMovieDatabase : RoomDatabase(){
    abstract fun favMovieDao() : FavMovieDao
    companion object{
        @Volatile
        private var INSTANCE : FavMovieDatabase? = null
        fun getInstance(context: Context) : FavMovieDatabase {
            synchronized(this) {
                var instance = INSTANCE
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        FavMovieDatabase::class.java,
                        "fav_movie_data_database_new_new"
                    ).allowMainThreadQueries().build()
                }
                return instance
            }
        }
    }
}
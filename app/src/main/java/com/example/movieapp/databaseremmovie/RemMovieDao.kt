package com.example.movieapp.databaseremmovie

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface RemMovieDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRemMovie(remMovie: RemMovie)

    @Delete
    suspend fun deleteRemMovie(remMovie: RemMovie)

    @Query(value = "SELECT * FROM rem_movie_table_new")
    fun getAllRemMovies() : LiveData<List<RemMovie>>
}
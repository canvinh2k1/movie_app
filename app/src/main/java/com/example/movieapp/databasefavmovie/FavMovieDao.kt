package com.example.movieapp.databasefavmovie

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface FavMovieDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavMovie(favMovie: FavMovie)

    @Delete
    suspend fun deleteFavMovie(favMovie: FavMovie)

    @Query(value = "SELECT * FROM fav_movie_table_new_new")
    fun getAllFavMovies() : LiveData<List<FavMovie>>

    @Query(value = "SELECT COUNT(*) FROM fav_movie_table_new_new")
    fun getNumbersOfData() : LiveData<Int>
}
package com.example.movieapp.databasefavmovie

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class FavMovieViewModel(private val dao  : FavMovieDao) : ViewModel() {
    val number = dao.getNumbersOfData()
    val favMovies = dao.getAllFavMovies()
    fun insertFavMovie(movie: FavMovie) = viewModelScope.launch {
        dao.insertFavMovie(movie)
    }
    fun deleteFavMovie(movie: FavMovie) = viewModelScope.launch {
        dao.deleteFavMovie(movie)
    }
}
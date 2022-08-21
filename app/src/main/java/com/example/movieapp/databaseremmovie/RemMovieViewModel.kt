package com.example.movieapp.databaseremmovie

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class RemMovieViewModel(private val dao  : RemMovieDao) : ViewModel() {

    val remMovies = dao.getAllRemMovies()
    fun insertRemMovie(movie: RemMovie) = viewModelScope.launch {
        dao.insertRemMovie(movie)
    }
    fun deleteRemMovie(movie: RemMovie) = viewModelScope.launch {
        dao.deleteRemMovie(movie)
    }
}
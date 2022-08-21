package com.example.movieapp.databasefavmovie

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import java.lang.IllegalAccessException

class FavMovieViewModelFactory(private val dao : FavMovieDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FavMovieViewModel::class.java)){
            return FavMovieViewModel(dao) as T
        }
        throw IllegalAccessException("Unknow View Model Class")
    }
}
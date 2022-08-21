package com.example.movieapp.databaseremmovie

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import java.lang.IllegalAccessException

class RemMovieViewModelFactory(private val dao : RemMovieDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RemMovieViewModel::class.java)){
            return RemMovieViewModel(dao) as T
        }
        throw IllegalAccessException("Unknow View Model Class")
    }
}
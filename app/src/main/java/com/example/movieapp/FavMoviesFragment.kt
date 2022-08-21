package com.example.movieapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

private lateinit var favMoviesLayoutMgr: LinearLayoutManager
private lateinit var favMoviesList: RecyclerView

class FavMoviesFragment(
    private var favMoviesAdapter: MovieFavAdapter,
) : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_fav_movies, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        favMoviesList = view.findViewById(R.id.fav_movie)
        favMoviesLayoutMgr = LinearLayoutManager(context)
        favMoviesList.layoutManager = favMoviesLayoutMgr
        favMoviesList.adapter = favMoviesAdapter
    }
}
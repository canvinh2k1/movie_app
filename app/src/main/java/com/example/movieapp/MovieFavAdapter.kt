package com.example.movieapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.example.movieapp.databasefavmovie.FavMovie

class MovieFavAdapter(
    private val onMovieClick : (id: Int, backdrop : String, title : String) -> Unit
) : RecyclerView.Adapter<MovieFavAdapter.MovieFavViewHolder>() {

    private val favMovies = ArrayList<FavMovie>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieFavViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.item_movie_fav, parent, false)
        return MovieFavViewHolder(view)
    }

    override fun getItemCount(): Int = favMovies.size

    fun setList (favMovieList : List<FavMovie>) {
        favMovies.clear()
        favMovies.addAll(favMovieList)
    }
    override fun onBindViewHolder(holder: MovieFavViewHolder, position: Int) {
        holder.bind(favMovies[position])
    }

    inner class MovieFavViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val title : TextView = itemView.findViewById(R.id.tvMovieFav)
        private val backdrop: ImageView = itemView.findViewById(R.id.movie_backdrop_fav)
        fun bind(movie: FavMovie) {
            Glide.with(itemView)
                .load("https://image.tmdb.org/t/p/w342${movie.backdrop}")
                .transform(CenterCrop())
                .into(backdrop)
            title.text = movie.title

            itemView.setOnClickListener { onMovieClick.invoke(movie.id,movie.backdrop,movie.title) }
        }
    }
}
package com.example.movieapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop

class MoviesAdapter(
    private var movies: MutableList<Movie>,
    private val onMovieClickAdd: (movie: Movie) -> Unit,
    private val onMovieClickRemove: (movie: Movie) -> Unit,
    private val onMovieClick: (movie: Movie) -> Unit,
    private val onSetNumber: () -> Unit,
) : RecyclerView.Adapter<MoviesAdapter.MovieViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.item_movie, parent, false)
        return MovieViewHolder(view)
    }

    override fun getItemCount(): Int = movies.size

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        holder.bind(movies[position])
    }

    fun appendMovies(movies: List<Movie>) {
        this.movies.addAll(movies)
        notifyItemRangeInserted(
            this.movies.size,
            movies.size - 1
        )
    }

    inner class MovieViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val poster: ImageView = itemView.findViewById(R.id.item_movie_poster)
        private val btnFav: Button = itemView.findViewById(R.id.btnFav)

        fun bind(movie: Movie) {
            Glide.with(itemView)
                .load("https://image.tmdb.org/t/p/w342${movie.posterPath}")
                .transform(CenterCrop())
                .into(poster)

            itemView.setOnClickListener { onMovieClick.invoke(movie) }

            btnFav.setOnClickListener {

                if (!movie.isFav) {
                    btnFav.setBackgroundResource(R.drawable.ic_favorite)
                    onMovieClickAdd(movie)
                    onSetNumber()
                    movie.isFav = true
                } else {
                    btnFav.setBackgroundResource(R.drawable.ic_not_favorite)
                    onMovieClickRemove(movie)
                    onSetNumber()
                    movie.isFav = false
                }
            }
        }
    }
}
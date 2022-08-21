package com.example.movieapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.example.movieapp.databaseremmovie.RemMovie

class MoviesRemAdapter(
    private val removeMovieRem : (id: Int, title: String, poster: String, time: String, ratting: Float) -> Unit,
) : RecyclerView.Adapter<MoviesRemAdapter.MoviesRemViewHolder>() {

    private val remMovies = ArrayList<RemMovie>()
    override fun getItemCount(): Int {
        return remMovies.size
    }

    fun setList (remMovieList : List<RemMovie>) {
        remMovies.clear()
        remMovies.addAll(remMovieList)
    }

    override fun onBindViewHolder(holder: MoviesRemViewHolder, position: Int) {
        holder.bind(remMovies[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MoviesRemAdapter.MoviesRemViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.item_movie_reminder, parent, false)
        return MoviesRemViewHolder(view)
    }

    inner class MoviesRemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val title : TextView = itemView.findViewById(R.id.rem_movie_title)
        private val poster : ImageView = itemView.findViewById(R.id.image_movie_rem)
        private val time : TextView = itemView.findViewById(R.id.reminder_day)
        private val ratting : RatingBar = itemView.findViewById(R.id.rem_movie_rating)
        private val ibClose : ImageButton = itemView.findViewById(R.id.ibClose)

        fun bind(movie : RemMovie) {
            Glide.with(itemView)
                .load("https://image.tmdb.org/t/p/w342${movie.poster}")
                .transform(CenterCrop())
                .into(poster)
            title.text = movie.title
            time.text = movie.time
            ratting.rating = movie.ratting

            ibClose.setOnClickListener {
                removeMovieRem(movie.id, movie.title, movie.poster, movie.time, movie.ratting)
            }
        }
    }
}
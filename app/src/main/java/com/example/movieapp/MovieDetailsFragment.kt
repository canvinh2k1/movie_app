package com.example.movieapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop


class MovieDetailsFragment(
    private val onMovieClickAdd: (title: String, poster: String, time: String, ratting: Float) -> Unit,
    private val onMovieClickRemove: (id: Int, title: String, poster: String, time: String, ratting: Float) -> Unit
) : Fragment() {

    private lateinit var backdrop: ImageView
    private lateinit var poster: ImageView
    private lateinit var title: TextView
    private lateinit var rating: RatingBar
    private lateinit var releaseDate: TextView
    private lateinit var overview: TextView

    private lateinit var ibNotification: ImageButton
    private lateinit var tvTime: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_movie_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        backdrop = view.findViewById(R.id.movie_backdrop)
        poster = view.findViewById(R.id.movie_poster)
        title = view.findViewById(R.id.movie_title)
        rating = view.findViewById(R.id.movie_rating)
        releaseDate = view.findViewById(R.id.movie_release_date)
        overview = view.findViewById(R.id.movie_overview)

        ibNotification = view.findViewById(R.id.ibNotification)
        tvTime = view.findViewById(R.id.tvTime)

        val extras = this.arguments
        if (extras != null) {
            extras.getString(MOVIE_BACKDROP)?.let { backdropPath ->
                Glide.with(this)
                    .load("https://image.tmdb.org/t/p/w1280$backdropPath")
                    .transform(CenterCrop())
                    .into(backdrop)
            }

            extras.getString(MOVIE_POSTER)?.let { posterPath ->
                Glide.with(this)
                    .load("https://image.tmdb.org/t/p/w342$posterPath")
                    .transform(CenterCrop())
                    .into(poster)
            }
            val posterString = extras.getString(MOVIE_POSTER)
            title.text = extras.getString(MOVIE_TITLE, "")
            val titleString = extras.getString(MOVIE_TITLE, "")
            rating.rating = extras.getString(MOVIE_RATING, "").toFloat() / 2
            val rattingFloat = extras.getString(MOVIE_RATING, "").toFloat() / 2
            releaseDate.text = extras.getString(MOVIE_RELEASE_DATE, "")
            overview.text = extras.getString(MOVIE_OVERVIEW, "")

            ibNotification.setOnClickListener {
                val datePickerFragment = DatePickerFragment()
                val supportFragmentManager = requireActivity().supportFragmentManager
                supportFragmentManager.setFragmentResultListener(
                    "REQUEST_KEY",
                    viewLifecycleOwner
                ) {
                        resultKey, bundle -> if (resultKey == "REQUEST_KEY") {
                    val date = bundle.getString("SELECTED_DATE")
                    tvTime.text = date
                    onMovieClickAdd(titleString, posterString.toString(),date.toString(),rattingFloat)
                }
                }
                datePickerFragment.show(supportFragmentManager,"DatePickerFragment")
            }
        }
    }
    companion object {

        const val MOVIE_BACKDROP = "extra_movie_backdrop"
        const val MOVIE_POSTER = "extra_movie_poster"
        const val MOVIE_TITLE = "extra_movie_title"
        const val MOVIE_RATING = "extra_movie_rating"
        const val MOVIE_RELEASE_DATE = "extra_movie_release_date"
        const val MOVIE_OVERVIEW = "extra_movie_overview"

    }
}
package com.example.movieapp

import android.annotation.SuppressLint
import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.findFragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.movieapp.MovieDetailsFragment.Companion.MOVIE_BACKDROP
import com.example.movieapp.MovieDetailsFragment.Companion.MOVIE_OVERVIEW
import com.example.movieapp.MovieDetailsFragment.Companion.MOVIE_POSTER
import com.example.movieapp.MovieDetailsFragment.Companion.MOVIE_RATING
import com.example.movieapp.MovieDetailsFragment.Companion.MOVIE_RELEASE_DATE
import com.example.movieapp.MovieDetailsFragment.Companion.MOVIE_TITLE
import com.example.movieapp.databasefavmovie.FavMovie
import com.example.movieapp.databasefavmovie.FavMovieDatabase
import com.example.movieapp.databasefavmovie.FavMovieViewModel
import com.example.movieapp.databasefavmovie.FavMovieViewModelFactory
import com.example.movieapp.databaseremmovie.RemMovie
import com.example.movieapp.databaseremmovie.RemMovieDatabase
import com.example.movieapp.databaseremmovie.RemMovieViewModel
import com.example.movieapp.databaseremmovie.RemMovieViewModelFactory

class MainActivity : AppCompatActivity() {
    private lateinit var popularMovies: RecyclerView
    private lateinit var popularMoviesAdapter: MoviesAdapter
    private lateinit var popularMoviesLayoutMgr: LinearLayoutManager

    private var popularMoviesPage = 1

    private lateinit var topRatedMovies: RecyclerView
    private lateinit var topRatedMoviesAdapter: MoviesAdapter
    private lateinit var topRatedMoviesLayoutMgr: LinearLayoutManager

    private var topRatedMoviesPage = 1

    private lateinit var upcomingMovies: RecyclerView
    private lateinit var upcomingMoviesAdapter: MoviesAdapter
    private lateinit var upcomingMoviesLayoutMgr: LinearLayoutManager

    private var upcomingMoviesPage = 1

    private lateinit var viewModel: FavMovieViewModel
    private lateinit var favMoviesAdapter: MovieFavAdapter

    private lateinit var remViewModel : RemMovieViewModel
    private lateinit var remMoviesAdapter : MoviesRemAdapter

    private lateinit var tvNumber : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        popularMovies = findViewById(R.id.popular_movies)
        popularMoviesLayoutMgr = LinearLayoutManager(
            this,
            LinearLayoutManager.HORIZONTAL,
            false
        )
        popularMovies.layoutManager = popularMoviesLayoutMgr
        popularMoviesAdapter = MoviesAdapter(mutableListOf(), {
            movie -> addMovie(movie)
        }, {
            movie -> removeMovie(movie)
        }, {
            movie -> showMovieDetails(movie)
        }, {
            setNumber()
        })

        popularMovies.adapter = popularMoviesAdapter


        topRatedMovies = findViewById(R.id.top_rated_movies)
        topRatedMoviesLayoutMgr = LinearLayoutManager(
            this,
            LinearLayoutManager.HORIZONTAL,
            false
        )
        topRatedMovies.layoutManager = topRatedMoviesLayoutMgr
        topRatedMoviesAdapter = MoviesAdapter(mutableListOf(), {
                movie -> addMovie(movie)
        }, {
                movie -> removeMovie(movie)
        }, {
                movie -> showMovieDetails(movie)
        }, {
            setNumber()
        })
        topRatedMovies.adapter = topRatedMoviesAdapter


        upcomingMovies = findViewById(R.id.upcoming_movies)
        upcomingMoviesLayoutMgr = LinearLayoutManager(
            this,
            LinearLayoutManager.HORIZONTAL,
            false
        )
        upcomingMovies.layoutManager = upcomingMoviesLayoutMgr
        upcomingMoviesAdapter = MoviesAdapter(mutableListOf(), {
                movie -> addMovie(movie)
        }, {
                movie -> removeMovie(movie)
        }, {
                movie -> showMovieDetails(movie)
        }, {
            setNumber()
        })
        upcomingMovies.adapter = upcomingMoviesAdapter


        getPopularMovies()
        getTopRatedMovies()
        getUpcomingMovies()

        val btnHome = findViewById<ImageButton>(R.id.ibHome)
        btnHome.setOnClickListener {
            backToHome()
        }

        val btnFav = findViewById<ImageButton>(R.id.ibStar)
        val dao = FavMovieDatabase.getInstance(application).favMovieDao()
        val factory = FavMovieViewModelFactory(dao)
        viewModel = ViewModelProvider(this, factory)[FavMovieViewModel::class.java]
        favMoviesAdapter = MovieFavAdapter { id, backdrop, title ->
            showDialogFav(
                id,
                backdrop,
                title
            )
        }

        setListMovie()
        setNumber()
        btnFav.setOnClickListener {
            val fragment = FavMoviesFragment(favMoviesAdapter)
            val fragmentManager : FragmentManager = supportFragmentManager
            val fragmentTransaction : FragmentTransaction = fragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.mvFragment, fragment)
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()
        }

        val btnPerson = findViewById<ImageButton>(R.id.ibPerson)
        val daoRem = RemMovieDatabase.getInstance(application).remMovieDao()
        val factoryRem = RemMovieViewModelFactory(daoRem)
        remViewModel = ViewModelProvider(this,factoryRem)[RemMovieViewModel::class.java]
        remMoviesAdapter = MoviesRemAdapter { id, title, poster, time, ratting ->
            removeMovieRem(id, title, poster, time, ratting)
        }
        setListRemMovie()

        btnPerson.setOnClickListener {
            tranferPerson(remMoviesAdapter)
        }
    }

    private fun tranferPerson(remMoviesAdapter: MoviesRemAdapter) {
        val fragment = PersonFragment((remMoviesAdapter), {
            showDialog()
        }, {
            tranferEdit(remMoviesAdapter)
        })
        val fragmentManager : FragmentManager = supportFragmentManager
        val fragmentTransaction : FragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.mvFragment, fragment)
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
    }
    private fun showDialogFav(id: Int, backdrop : String, title : String) {
        val builder:AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setTitle("Delete your favorite movie")
        builder.setMessage("Do you want to delete this movie?")
        builder.setPositiveButton("Yes",
            DialogInterface.OnClickListener{ dialog, which ->
                dialog.dismiss()
                viewModel.deleteFavMovie(
                    FavMovie(
                        id,
                        backdrop,
                        title
                    )
                )
            }
        )

        builder.setNegativeButton("No",
            DialogInterface.OnClickListener{ dialog, which ->
                dialog.dismiss()
            }
        )
        val alertDialog : AlertDialog = builder.create()
        alertDialog.show()
    }

    private fun backToHome() {
        val frames = findViewById<FrameLayout>(R.id.mvFragment)
        frames.removeAllViews()

    }

    private fun tranferEdit(remMoviesAdapter: MoviesRemAdapter) {
        val fragment = EditProfileFragment ((remMoviesAdapter),{
            backToHome()
        }, {
            showDialog()
        }, {
            tranferEdit(remMoviesAdapter)
        })
        val fragmentManager : FragmentManager = supportFragmentManager
        val fragmentTransaction : FragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.mvFragment, fragment)
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
    }

    private fun showDialog() {
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setTitle("About moive app")
        builder.setMessage("This is movie app designed by Vinh.")
        val alertDialog : AlertDialog = builder.create()
        alertDialog.show()
    }

    private fun setNumber()  {
        val tvNumber = findViewById<TextView>(R.id.tvNumber)
        viewModel.number.observe(this) {
            tvNumber.text = it.toString()
        }

    }

    @SuppressLint("NotifyDataSetChanged")
    private fun setListMovie() {
        viewModel.favMovies.observe(this) {
            favMoviesAdapter.setList(it)
            favMoviesAdapter.notifyDataSetChanged()
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun setListRemMovie() {

        remViewModel.remMovies.observe(this) {
            remMoviesAdapter.setList(it)
            remMoviesAdapter.notifyDataSetChanged()
        }

    }
    private fun addMovie(movie: Movie) {
        viewModel.insertFavMovie(
            FavMovie(
                movie.id.toInt(),
                movie.backdropPath,
                movie.title
            )
        )
    }

    private fun removeMovie(movie: Movie) {
        viewModel.deleteFavMovie(
            FavMovie(
                movie.id.toInt(),
                movie.backdropPath,
                movie.title
            )
        )
    }

    private fun addMovieRem(title: String, poster: String, time: String, ratting: Float) {
        remViewModel.insertRemMovie(
            RemMovie(
                0,
                poster,
                title,
                time,
                ratting
            )
        )
    }

    private fun removeMovieRem(id: Int, title: String, poster: String, time: String, ratting: Float) {
        remViewModel.deleteRemMovie(
            RemMovie(
                id,
                poster,
                title,
                time,
                ratting
            )
        )
    }

    private fun showMovieDetails(movie: Movie) {

        val bundle = Bundle()
        bundle.putString(MOVIE_BACKDROP, movie.backdropPath)
        bundle.putString(MOVIE_POSTER, movie.posterPath)
        bundle.putString(MOVIE_TITLE, movie.title)
        bundle.putString(MOVIE_RATING, movie.rating.toString())
        bundle.putString(MOVIE_RELEASE_DATE, movie.releaseDate)
        bundle.putString(MOVIE_OVERVIEW, movie.overview)

        val fragment = MovieDetailsFragment({
            title, poster, time, ratting -> addMovieRem(title, poster, time, ratting)
        }, {
            id, title, poster, time, ratting -> removeMovieRem(id, title, poster, time, ratting)
        })
        fragment.arguments = bundle
        val fragmentManager : FragmentManager = supportFragmentManager
        val fragmentTransaction : FragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.mvFragment, fragment)
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
    }



    private fun onError() {
        Toast.makeText(this, getString(R.string.error_fetch_movies), Toast.LENGTH_SHORT).show()
    }


    private fun getPopularMovies() {
        MoviesRepository.getPopularMovies(
            popularMoviesPage,
            ::onPopularMoviesFetched,
            ::onError
        )
    }
    private fun onPopularMoviesFetched(movies: List<Movie>) {
        popularMoviesAdapter.appendMovies(movies)
        attachPopularMoviesOnScrollListener()
    }
    private fun attachPopularMoviesOnScrollListener() {
        popularMovies.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                val totalItemCount = popularMoviesLayoutMgr.itemCount
                val visibleItemCount = popularMoviesLayoutMgr.childCount
                val firstVisibleItem = popularMoviesLayoutMgr.findFirstVisibleItemPosition()

                if (firstVisibleItem + visibleItemCount >= totalItemCount / 2) {
                    popularMovies.removeOnScrollListener(this)
                    popularMoviesPage++
                    getPopularMovies()
                }
            }
        })
    }



    private fun getTopRatedMovies() {
        MoviesRepository.getTopRatedMovies(
            topRatedMoviesPage,
            ::onTopRatedMoviesFetched,
            ::onError
        )
    }
    private fun onTopRatedMoviesFetched(movies: List<Movie>) {
        topRatedMoviesAdapter.appendMovies(movies)
        attachTopRatedMoviesOnScrollListener()
    }
    private fun attachTopRatedMoviesOnScrollListener() {
        topRatedMovies.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                val totalItemCount = topRatedMoviesLayoutMgr.itemCount
                val visibleItemCount = topRatedMoviesLayoutMgr.childCount
                val firstVisibleItem = topRatedMoviesLayoutMgr.findFirstVisibleItemPosition()

                if (firstVisibleItem + visibleItemCount >= totalItemCount / 2) {
                    topRatedMovies.removeOnScrollListener(this)
                    topRatedMoviesPage++
                    getTopRatedMovies()
                }
            }
        })
    }


    private fun getUpcomingMovies() {
        MoviesRepository.getUpcomingMovies(
            upcomingMoviesPage,
            ::onUpcomingMoviesFetched,
            ::onError
        )
    }
    private fun onUpcomingMoviesFetched(movies: List<Movie>) {
        upcomingMoviesAdapter.appendMovies(movies)
        attachUpcomingMoviesOnScrollListener()
    }
    private fun attachUpcomingMoviesOnScrollListener() {
        upcomingMovies.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                val totalItemCount = upcomingMoviesLayoutMgr.itemCount
                val visibleItemCount = upcomingMoviesLayoutMgr.childCount
                val firstVisibleItem = upcomingMoviesLayoutMgr.findFirstVisibleItemPosition()

                if (firstVisibleItem + visibleItemCount >= totalItemCount / 2) {
                    upcomingMovies.removeOnScrollListener(this)
                    upcomingMoviesPage++
                    getUpcomingMovies()
                }
            }
        })
    }
}
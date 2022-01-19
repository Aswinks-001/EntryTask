package com.entri.task.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.GridLayout
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.entri.task.R
import com.entri.task.data.Movies
import com.entri.task.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.movie_load_state_footer.*


@AndroidEntryPoint
class MainActivity : AppCompatActivity(),MoviesPagingAdapter.Onitemclick {

   // lateinit var viewModel: HomeViewModel
    private val viewModel by viewModels<HomeViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding:ActivityMainBinding = DataBindingUtil.setContentView(this,R.layout.activity_main)

        moviesList.layoutManager = GridLayoutManager(this,2)

        val moviesPagingAdapter = MoviesPagingAdapter(this)
        binding.apply {
            moviesList.setHasFixedSize(true)
            moviesList.adapter = moviesPagingAdapter.withLoadStateHeaderAndFooter(
                header = MoviesLoadStateAdapter {moviesPagingAdapter.retry()},
                footer = MoviesLoadStateAdapter {moviesPagingAdapter.retry()},
            )
            buttonRetry.setOnClickListener {
                moviesPagingAdapter.retry()
            }
        }

        viewModel.movies.observe(this, Observer {
            moviesPagingAdapter.submitData(lifecycle,it)
        })

        moviesPagingAdapter.addLoadStateListener { loadstate ->
            binding.apply {
                progressBar.isVisible = loadstate.source.refresh is LoadState.Loading
                moviesList.isVisible = loadstate.source.refresh is LoadState.NotLoading
                buttonRetry.isVisible = loadstate.source.refresh is LoadState.Error
                tvError.isVisible = loadstate.source.refresh is LoadState.Error


                if (loadstate.source.refresh is LoadState.NotLoading &&
                    loadstate.append.endOfPaginationReached
                    && moviesPagingAdapter.itemCount <1 ) {
                    moviesList.isVisible = false
                    tvEmpty.isVisible = true

                }else{
                    tvEmpty.isVisible = false
                }
            }

        }


    }

    override fun onItemClickListner(movie: Movies) {
        val intent = Intent(this, DetailActivity::class.java).apply {
            putExtra("movie_id", movie.id)
        }
        startActivity(intent)
    }
}
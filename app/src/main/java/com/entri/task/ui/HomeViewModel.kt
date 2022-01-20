package com.entri.task.ui

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import com.entri.task.api.MovieApi
import com.entri.task.data.MovieRepository
import com.entri.task.data.Movies
import com.entri.task.data.MoviesRemoteMediator
import com.entri.task.room.MoviesDao
import kotlinx.coroutines.flow.Flow


class HomeViewModel @ViewModelInject constructor(
    private val movieRepository: MovieRepository,
    private val moviesDao: MoviesDao,
    private val movieApi: MovieApi
) : ViewModel() {


    val movies = movieRepository.getMovies().cachedIn(viewModelScope)

    @ExperimentalPagingApi
    val moviesresult: Flow<PagingData<Movies>> =
        movieRepository.getResult().cachedIn(viewModelScope)


    @ExperimentalPagingApi
    val pager = Pager(
        PagingConfig(pageSize = 20),
        remoteMediator = MoviesRemoteMediator(moviesDao, movieApi, 1)
    ) {
        moviesDao.getAllMovies()
    }.flow

}
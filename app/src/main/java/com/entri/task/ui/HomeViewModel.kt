package com.entri.task.ui

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.entri.task.data.MovieRepository

class HomeViewModel @ViewModelInject constructor(
    private val movieRepository: MovieRepository
    ) : ViewModel() {

        val movies = movieRepository.getMovies().cachedIn(viewModelScope)

}
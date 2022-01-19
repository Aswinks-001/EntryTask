package com.entri.task.ui

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import com.entri.task.data.MovieRepository
import com.entri.task.data.MovieResponseDetail

class DetailViewModel @ViewModelInject constructor(
        private val movieRepository: MovieRepository) : ViewModel() {

     suspend fun getDetail(movieId:Int) : MovieResponseDetail{
          return movieRepository.getMovieDetail(movieId!!)
     }

}
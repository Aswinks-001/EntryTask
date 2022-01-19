package com.entri.task.data

import android.util.Log
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.liveData
import com.entri.task.api.Constants
import com.entri.task.api.MovieApi
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MovieRepository @Inject constructor(private val movieApi: MovieApi) {

    fun getMovies() =
        Pager(
            config = PagingConfig(
                pageSize = 20,
                maxSize = 100,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { MoviePagingSource(movieApi)}
        ).liveData


    suspend fun getMovieDetail(movie_id:Int) : MovieResponseDetail {
        val respsnse = movieApi.getMovieDetail(movie_id, Constants.API_KEY,Constants.LANGUAGE_KEY)
        if (respsnse.isSuccessful){
            Log.e("response","::success")
        }else{
            Log.e("response","::False")
        }

        return respsnse.body()!!

    }

}
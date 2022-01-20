package com.entri.task.data

import android.util.Log
import androidx.paging.*
import com.entri.task.api.Constants
import com.entri.task.api.MovieApi
import com.entri.task.room.MovieDataBase
import com.entri.task.room.MoviesDao
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MovieRepository @Inject constructor(private val movieApi: MovieApi,private val moviesDao: MoviesDao,private val db:MovieDataBase) {

    //--for fetching from network
    fun getMovies() =
        Pager(
            config = PagingConfig(
                pageSize = 20,
                maxSize = 100,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { MoviePagingSource(movieApi)}
        ).liveData


    //-- fore fetching data from newtwork to local
    @ExperimentalPagingApi
    fun getResult() : Flow<PagingData<Movies>>{
        val pagingsourcefactory = {moviesDao.getAllMovies()}
        return Pager(
            config = PagingConfig(pageSize = 20,enablePlaceholders = false),
            remoteMediator = RemoteMediatorPaging(moviesDao,movieApi,db),
            pagingSourceFactory = pagingsourcefactory
        ).flow

    }


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
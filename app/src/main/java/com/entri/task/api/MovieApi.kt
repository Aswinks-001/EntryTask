package com.entri.task.api

import com.entri.task.data.MovieResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface MovieApi {

    companion object {
        const val BASE_URL = "https://api.themoviedb.org/3/"
    }

    @GET("movie/now_playing")
    suspend fun getMovies(@Query("api_key") api_key:String,
                          @Query("language") language:String,
                          @Query("page") page:Int): Response<MovieResponse>
}
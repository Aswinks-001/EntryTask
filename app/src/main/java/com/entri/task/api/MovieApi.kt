package com.entri.task.api

import com.entri.task.data.MovieResponse
import com.entri.task.data.MovieResponseDetail
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieApi {

    companion object {
        const val BASE_URL = "https://api.themoviedb.org/3/"
    }

    @GET("movie/now_playing")
    suspend fun getMovies(@Query("api_key") api_key:String,
                          @Query("language") language:String,
                          @Query("page") page:Int): MovieResponse

    @GET("movie/now_playing")
    suspend fun getAllMovies(@Query("api_key") api_key:String,
                          @Query("language") language:String,
                          @Query("page") page:Int): Response<MovieResponse>

    @GET("movie/{movie_id}")
    suspend fun getMovieDetail(
            @Path(value = "movie_id", encoded = false) key: Int,
            @Query("api_key") api_key:String,
            @Query("language") language:String
    ) : Response<MovieResponseDetail>
}
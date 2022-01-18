package com.entri.task.data

import android.util.Log
import androidx.paging.PagingSource
import com.entri.task.api.MovieApi
import retrofit2.HttpException
import java.io.IOException

private const val STARTING_PAGE_INDEX = 1
class MoviePagingSource(
    private val movieApi: MovieApi
) : PagingSource<Int,Movies>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Movies> {

        val position =params.key ?: STARTING_PAGE_INDEX

        return try {

            val response = movieApi.getMovies("1daf510c3b98d8ba51a93ae33040c71a","en-US",position)
            val movies = response.results
            Log.e("movies","::${movies.size}")
            LoadResult.Page(
                data = movies,
                prevKey = if (position == STARTING_PAGE_INDEX) null else position - 1,
                nextKey = if (movies.isEmpty()) null else position + 1
            )

        }catch (e : IOException){
            Log.e("Catch","::${e.toString()}")
            LoadResult.Error(e)
        }catch (e : HttpException){
            LoadResult.Error(e)
        }


    }

}
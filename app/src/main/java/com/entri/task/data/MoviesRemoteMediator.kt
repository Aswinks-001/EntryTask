package com.entri.task.data

import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.entri.task.api.Constants
import com.entri.task.api.MovieApi
import com.entri.task.room.MovieRemoteKeys
import com.entri.task.room.MoviesDao
import retrofit2.HttpException
import java.io.InvalidObjectException

private const val STARTING_PAGE_INDEX: Int = 1

@ExperimentalPagingApi
class MoviesRemoteMediator(
    private val moviesDao: MoviesDao,
    private val movieApi: MovieApi,
    private val initialPage: Int = 1
) : RemoteMediator<Int, Movies>() {


    override suspend fun load(loadType: LoadType, state: PagingState<Int, Movies>): MediatorResult {

        return try {

            val page = when (loadType) {
                LoadType.APPEND -> {
                    val remoteKeys = getLastKey(state) ?: throw InvalidObjectException("null")
                    remoteKeys.next ?: return MediatorResult.Success(endOfPaginationReached = false)
                }
                LoadType.PREPEND -> {
                    return MediatorResult.Success(endOfPaginationReached = true)
                }
                LoadType.REFRESH -> {
                    val remoteKeys = getClosestKey(state)
                    remoteKeys?.next?.minus(1) ?: initialPage
                   // null

                }


            }

            val response = movieApi.getAllMovies(Constants.API_KEY, Constants.LANGUAGE_KEY, page!!)
            val endofPagination = response.body()?.results?.size!! < state.config.pageSize

            Log.e("pagesize", "::${response.body()?.results?.size!!}")
            Log.e("end of page", "::${endofPagination}")
            Log.e("page", "::${page}")

            if (response.isSuccessful) {

                response.body()?.let {

                    if (loadType == LoadType.REFRESH) {
                        moviesDao.deleteAllMovies()
                        moviesDao.deleteAllremotekey()
                    }

                    val prev = if (page == initialPage) null else page - 1
                    val next = if (endofPagination) null else page + 1

                    val list = response.body()?.results?.map {
                        MovieRemoteKeys(it.id, prev, next)
                    }

                    Log.e("next,prev", "prev::${prev} :: next ${next}")

                    if (list != null) {
                        moviesDao.insertAllRemoteKey(list)
                    }

                    moviesDao.insertMovies(it.results)
                }
                MediatorResult.Success(endOfPaginationReached = endofPagination)
            } else {
                MediatorResult.Success(endOfPaginationReached = true)

            }


        } catch (e: Exception) {
            MediatorResult.Error(e)
        }catch (e :HttpException){
            MediatorResult.Error(e)
        }

        // return MediatorResult.Success(endOfPaginationReached = true)

    }

    suspend fun getClosestKey(state: PagingState<Int, Movies>): MovieRemoteKeys? {
        return state.anchorPosition?.let {
            state.closestItemToPosition(it)?.let {
                moviesDao.getAllRemoteKeys(it.id)
            }
        }
    }

    suspend fun getLastKey(state: PagingState<Int, Movies>): MovieRemoteKeys? {
        return state.lastItemOrNull()?.let {
            moviesDao.getAllRemoteKeys(it.id)
        }
    }
}
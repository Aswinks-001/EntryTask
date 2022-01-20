package com.entri.task.data

import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.RoomDatabase
import androidx.room.withTransaction
import com.entri.task.api.Constants
import com.entri.task.api.MovieApi
import com.entri.task.room.MovieDataBase
import com.entri.task.room.MovieRemoteKeys
import com.entri.task.room.MoviesDao
import retrofit2.HttpException
import java.io.IOException

@ExperimentalPagingApi
class RemoteMediatorPaging(
    private val moviesDao: MoviesDao,
    private val movieApi: MovieApi,
    private val db :MovieDataBase
) : RemoteMediator<Int, Movies>() {

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, Movies>
    ): MediatorResult {

        val pagekey = getPagekeyData(state,loadType)
        val page = when(pagekey){
            is MediatorResult.Success ->{
                return pagekey
            }else -> {
                pagekey as Int
            }
        }

        return try {
            val response = movieApi.getMovies(Constants.API_KEY, Constants.LANGUAGE_KEY, page!!)
            val isListEmpty = response.results.isEmpty()

            db.withTransaction {


            if (loadType == LoadType.REFRESH){
                moviesDao.deleteAllMovies()
                moviesDao.deleteAllremotekey()
            }

            val prevkey = if (page == 1) null else page - 1
            val nextkey = if (isListEmpty) null else page + 1

            val keys = response.results.map {
                MovieRemoteKeys(it.id,prevkey,nextkey)
            }

            moviesDao.insertMovies(response.results)
            moviesDao.insertAllRemoteKey(keys)

            Log.e("page","::${page.toString()}")
            Log.e("islist","::${isListEmpty}")
            }
            return MediatorResult.Success(endOfPaginationReached = isListEmpty)
        }catch (e : IOException){
            MediatorResult.Error(e)
        }catch (e: HttpException){
            MediatorResult.Error(e)
        }
    }

    private suspend fun getPagekeyData(state: PagingState<Int, Movies>, loadType: LoadType): Any {

        return when(loadType){
            LoadType.REFRESH -> {
                val remotekey = getCurrentPosition(state)
                val current = remotekey?.next?.minus(1)
                if (current != null){
                    return current
                }else{
                    1
                }
            }

            LoadType.PREPEND -> {

                val remotekey = getFirestposition(state)
                val prevkey = remotekey?.prev ?: MediatorResult.Success(endOfPaginationReached = true)
                prevkey
            }

            LoadType.APPEND -> {
                val remotekeys = getLastRemoteKey(state)
                val nextkey = remotekeys?.next
                return if (nextkey != null) nextkey else MediatorResult.Success(endOfPaginationReached = false)
            }
        }
    }

    private suspend fun getLastRemoteKey(state: PagingState<Int, Movies>): MovieRemoteKeys? {
        return state.pages.lastOrNull { it.data.isNotEmpty() }
            ?.data?.lastOrNull()
            ?.let { movies ->
                moviesDao.getAllRemoteKeys(movies.id)
            }
    }

    private suspend fun getFirestposition(state: PagingState<Int, Movies>): MovieRemoteKeys? {
        return state.pages.
                firstOrNull{it.data.isNotEmpty()}
            ?.data?.firstOrNull()
            ?.let { movies ->
                moviesDao.getAllRemoteKeys(movies.id)
            }
    }

    private suspend fun getCurrentPosition(state: PagingState<Int, Movies>): MovieRemoteKeys? {

        return state.anchorPosition?.let {
            state.closestItemToPosition(it)?.id?.let { id ->
                moviesDao.getAllRemoteKeys(id)
            }
        }
    }


}
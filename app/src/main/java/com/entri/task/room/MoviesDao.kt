package com.entri.task.room

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.entri.task.data.Movies

@Dao
interface MoviesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovies(list: List<Movies>)

    @Query("SELECT * FROM Movies")
    fun getAllMovies(): PagingSource<Int, Movies>

    @Query("DELETE FROM movies")
    suspend fun deleteAllMovies()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllRemoteKey(list: List<MovieRemoteKeys>)

    @Query("SELECT * FROM MovieRemoteKeys WHERE id = :id")
    suspend fun getAllRemoteKeys(id: Int): MovieRemoteKeys

    @Query("DELETE FROM MovieRemoteKeys")
    suspend fun deleteAllremotekey()


}
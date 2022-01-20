package com.entri.task.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.entri.task.data.Movies

@Database(entities = [Movies::class,MovieRemoteKeys::class],version = 1)
abstract class MovieDataBase :RoomDatabase(){

//    companion object{
//        fun getInstance(context: Context) :MovieDataBase{
//            return Room.databaseBuilder(context,MovieDataBase::class.java,"movie_database").build()
//        }
//    }

    companion object {

        @Volatile
        private var INSTANCE: MovieDataBase? = null

        fun getInstance(context: Context): MovieDataBase =
            INSTANCE ?: synchronized(this) {
                INSTANCE
                    ?: buildDatabase(context).also { INSTANCE = it }
            }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(context.applicationContext,
                MovieDataBase::class.java, "movie_database")
                .build()
    }

    abstract fun getMoviesDao() : MoviesDao
}
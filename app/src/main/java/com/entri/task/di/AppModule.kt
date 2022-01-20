package com.entri.task.di

import android.content.Context
import com.entri.task.api.MovieApi
import com.entri.task.room.MovieDataBase
import com.entri.task.room.MoviesDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit =
        Retrofit.Builder()
            .baseUrl(MovieApi.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    @Provides
    @Singleton
    fun provideMyApi(retrofit: Retrofit): MovieApi =
        retrofit.create(MovieApi::class.java)


    @Provides
    @Singleton
    fun provideMovieDataBase(@ApplicationContext context: Context): MovieDataBase {
        return MovieDataBase.getInstance(context)
    }

    @Provides
    @Singleton
    fun provideMoviesDao(movieDataBase: MovieDataBase): MoviesDao {
        return movieDataBase.getMoviesDao()
    }

}
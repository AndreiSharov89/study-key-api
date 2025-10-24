package com.example.key_api.util

import android.content.Context
import com.example.key_api.BuildConfig
import com.example.key_api.data.MoviesRepositoryImpl
import com.example.key_api.data.SearchHistoryRepositoryImpl
import com.example.key_api.data.network.RetrofitNetworkClient
import com.example.key_api.data.storage.PrefsStorageClient
import com.example.key_api.domain.api.MoviesInteractor
import com.example.key_api.domain.api.MoviesRepository
import com.example.key_api.domain.api.SearchHistoryRepository
import com.example.key_api.domain.impl.MoviesInteractorImpl
import com.example.key_api.domain.models.Movie
import com.google.gson.reflect.TypeToken

object Creator {

    private fun getMoviesRepository(context: Context): MoviesRepository {
        return MoviesRepositoryImpl(RetrofitNetworkClient(BuildConfig.API_KEY, context))
    }

    fun provideMoviesInteractor(context: Context): MoviesInteractor {
        return MoviesInteractorImpl(getMoviesRepository(context))
    }
    private fun getSearchHistoryRepository(context: Context): SearchHistoryRepository {
        return SearchHistoryRepositoryImpl(
            PrefsStorageClient<ArrayList<Movie>>(
                context,
                "HISTORY",
                object : TypeToken<ArrayList<Movie>>() {}.type
            )
        )
    }

}
package com.example.key_api

import com.example.key_api.data.MoviesRepositoryImpl
import com.example.key_api.data.network.RetrofitNetworkClient
import com.example.key_api.domain.api.MoviesInteractor
import com.example.key_api.domain.api.MoviesRepository
import com.example.key_api.domain.impl.MoviesInteractorImpl

object Creator {
    private fun getMoviesRepository(): MoviesRepository {
        return MoviesRepositoryImpl(RetrofitNetworkClient(BuildConfig.API_KEY))
    }

    fun provideMoviesInteractor(): MoviesInteractor {
        return MoviesInteractorImpl(getMoviesRepository())
    }
}
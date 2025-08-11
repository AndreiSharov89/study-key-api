package com.example.key_api

import android.app.Activity
import android.content.Context
import com.example.key_api.data.MoviesRepositoryImpl
import com.example.key_api.data.network.RetrofitNetworkClient
import com.example.key_api.domain.api.MoviesInteractor
import com.example.key_api.domain.api.MoviesRepository
import com.example.key_api.domain.impl.MoviesInteractorImpl
import com.example.key_api.presentation.presenters.MoviesSearchController
import com.example.key_api.presentation.presenters.PosterController
import com.example.key_api.presentation.ui.movies.MoviesAdapter

object Creator {

    private fun getMoviesRepository(context: Context): MoviesRepository {
        return MoviesRepositoryImpl(RetrofitNetworkClient(BuildConfig.API_KEY, context))
    }

    fun provideMoviesInteractor(context: Context): MoviesInteractor {
        return MoviesInteractorImpl(getMoviesRepository(context))
    }

    fun provideMoviesSearchController(
        activity: Activity,
        adapter: MoviesAdapter
    ): MoviesSearchController {
        return MoviesSearchController(activity, adapter)
    }

    fun providePosterController(activity: Activity): PosterController {
        return PosterController(activity)
    }
}
package com.example.key_api.util

import android.content.Context
import com.example.key_api.BuildConfig
import com.example.key_api.data.MoviesRepositoryImpl
import com.example.key_api.data.network.RetrofitNetworkClient
import com.example.key_api.domain.api.MoviesInteractor
import com.example.key_api.domain.api.MoviesRepository
import com.example.key_api.domain.impl.MoviesInteractorImpl
import com.example.key_api.presentation.presenters.movies.MoviesSearchPresenter
import com.example.key_api.presentation.presenters.movies.MoviesView
import com.example.key_api.presentation.presenters.posters.PosterPresenter
import com.example.key_api.presentation.presenters.posters.PosterView

object Creator {

    private fun getMoviesRepository(context: Context): MoviesRepository {
        return MoviesRepositoryImpl(RetrofitNetworkClient(BuildConfig.API_KEY, context))
    }

    fun provideMoviesInteractor(context: Context): MoviesInteractor {
        return MoviesInteractorImpl(getMoviesRepository(context))
    }

    fun provideMoviesSearchPresenter(
        moviesView: MoviesView,
        context: Context
    ): MoviesSearchPresenter {
        return MoviesSearchPresenter(
            view = moviesView,
            context = context,
        )
    }

    fun providePosterPresenter(
        posterView: PosterView,
        imageUrl: String
    ): PosterPresenter {
        return PosterPresenter(posterView, imageUrl)
    }
}
package com.example.key_api.di

import com.example.key_api.presentation.presenters.cast.MoviesCastViewModel
import com.example.key_api.presentation.presenters.history.HistoryViewModel
import com.example.key_api.presentation.presenters.movies.MoviesViewModel
import com.example.key_api.presentation.presenters.names.NamesViewModel
import com.example.key_api.presentation.presenters.posters.AboutViewModel
import com.example.key_api.presentation.presenters.posters.PosterViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel {
        MoviesViewModel(get())
    }
    viewModel { (movieId: String) ->
        AboutViewModel(movieId, get())
    }

    viewModel { (posterUrl: String) ->
        PosterViewModel(posterUrl)
    }
    viewModel { (movieId: String) ->
        MoviesCastViewModel(movieId, get())
    }
    viewModel {
        NamesViewModel(androidContext(), get())
    }
    viewModel {
        HistoryViewModel(androidContext(), get())
    }

}
package com.example.key_api.di

import com.example.key_api.presentation.presenters.movies.MoviesViewModel
import com.example.key_api.presentation.presenters.posters.AboutViewModel
import com.example.key_api.presentation.presenters.posters.PosterViewModel
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

}
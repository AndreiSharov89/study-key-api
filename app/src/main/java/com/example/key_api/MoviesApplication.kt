package com.example.key_api

import android.app.Application
import com.example.key_api.presentation.presenters.movies.MoviesSearchPresenter

class MoviesApplication : Application() {

    var moviesSearchPresenter: MoviesSearchPresenter? = null

}
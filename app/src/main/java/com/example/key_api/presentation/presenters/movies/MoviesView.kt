package com.example.key_api.presentation.presenters.movies

import moxy.MvpView

interface MoviesView : MvpView {

    // Методы, меняющие внешний вид экрана
    fun render(state: MoviesState)

    // Методы «одноразовых событий»
    fun showToast(additionalMessage: String)

}
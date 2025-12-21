package com.example.key_api.presentation.presenters.cast

sealed interface MoviesCastState {

    object Loading : MoviesCastState

    data class Content(
        val fullTitle: String,
        val items: List<MoviesCastRVItem>,
    ) : MoviesCastState

    data class Error(
        val message: String,
    ) : MoviesCastState

}
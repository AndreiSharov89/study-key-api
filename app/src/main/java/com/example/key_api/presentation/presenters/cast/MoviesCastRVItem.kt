package com.example.key_api.presentation.presenters.cast

import com.example.key_api.domain.models.MovieCastPerson

sealed interface MoviesCastRVItem {

    data class HeaderItem(
        val headerText: String,
    ) : MoviesCastRVItem

    data class PersonItem(
        val data: MovieCastPerson,
    ) : MoviesCastRVItem

}
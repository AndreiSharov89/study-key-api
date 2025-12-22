package com.example.key_api.presentation.presenters.cast

import com.example.key_api.core.ui.RVItem
import com.example.key_api.domain.models.MovieCastPerson

sealed interface MoviesCastRVItem : RVItem {

    data class HeaderItem(
        val headerText: String,
    ) : MoviesCastRVItem

    data class PersonItem(
        val data: MovieCastPerson,
    ) : MoviesCastRVItem

}
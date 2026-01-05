package com.example.key_api.presentation.presenters.cast

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.key_api.domain.api.MoviesInteractor
import com.example.key_api.domain.models.MovieCast
import kotlinx.coroutines.launch

class MoviesCastViewModel(
    private val movieId: String,
    private val moviesInteractor: MoviesInteractor,
) : ViewModel() {
    private val stateLiveData = MutableLiveData<MoviesCastState>()
    fun observeState(): LiveData<MoviesCastState> = stateLiveData

    init {
        stateLiveData.postValue(MoviesCastState.Loading)

        viewModelScope.launch {
            moviesInteractor.getMovieCast(movieId).collect { pair ->
                processResult(pair.first, pair.second)
            }
        }
    }


    private fun processResult(movieCast: MovieCast?, errorMessage: String?) {

        when {
            (movieCast != null) -> {
                stateLiveData.postValue(castToUiStateContent(movieCast))
            }

            else -> {
                stateLiveData.postValue(MoviesCastState.Error(errorMessage ?: "Unknown error"))
            }
        }
    }

    private fun castToUiStateContent(cast: MovieCast): MoviesCastState {
        // Строим список элементов RecyclerView
        val items = buildList<MoviesCastRVItem> {
            // Если есть хотя бы один режиссёр, добавим заголовок
            if (cast.directors.isNotEmpty()) {
                this += MoviesCastRVItem.HeaderItem("Directors")
                this += cast.directors.map { MoviesCastRVItem.PersonItem(it) }
            }

            // Если есть хотя бы один сценарист, добавим заголовок
            if (cast.writers.isNotEmpty()) {
                this += MoviesCastRVItem.HeaderItem("Writers")
                this += cast.writers.map { MoviesCastRVItem.PersonItem(it) }
            }

            // Если есть хотя бы один актёр, добавим заголовок
            if (cast.actors.isNotEmpty()) {
                this += MoviesCastRVItem.HeaderItem("Actors")
                this += cast.actors.map { MoviesCastRVItem.PersonItem(it) }
            }

            // Если есть хотя бы один дополнительный участник, добавим заголовок
            if (cast.others.isNotEmpty()) {
                this += MoviesCastRVItem.HeaderItem("Others")
                this += cast.others.map { MoviesCastRVItem.PersonItem(it) }
            }
        }


        return MoviesCastState.Content(
            fullTitle = cast.fullTitle,
            items = items
        )
    }
}

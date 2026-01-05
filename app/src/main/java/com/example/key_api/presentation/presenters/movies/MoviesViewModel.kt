package com.example.key_api.presentation.presenters.movies

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.key_api.domain.api.MoviesInteractor
import com.example.key_api.domain.models.Movie
import com.example.key_api.presentation.SingleLiveEvent
import com.example.key_api.util.debounce
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class MoviesViewModel(private val moviesInteractor: MoviesInteractor) : ViewModel() {
    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 1000L
    }

    private var latestSearchText: String? = null
    private var searchJob: Job? = null

    private val movieSearchDebounce = debounce<String>(
        SEARCH_DEBOUNCE_DELAY,
        viewModelScope,
        true
    ) { changedText ->
        searchRequest(changedText)
    }

    private val stateLiveData = MutableLiveData<MoviesState>()
    fun observeState(): LiveData<MoviesState> = stateLiveData

    private val showToast = SingleLiveEvent<String?>()
    fun observeShowToast(): LiveData<String?> = showToast

    fun searchDebounce(changedText: String) {
        if (latestSearchText == changedText) {
            return
        }

        this.latestSearchText = changedText
        movieSearchDebounce(changedText)
    }

    private fun searchRequest(newSearchText: String) {
        if (newSearchText.isNotEmpty()) {
            renderState(MoviesState.Loading)

            searchJob?.cancel()
            searchJob = viewModelScope.launch {
                moviesInteractor.searchMovies(newSearchText)
                    .collect { pair ->
                        processResult(pair.first, pair.second)
                    }
            }
        }
    }

    private fun processResult(foundMovies: List<Movie>?, errorMessage: String?) {
        val movies = foundMovies ?: emptyList()

        when {
            errorMessage != null -> {
                renderState(MoviesState.Error(errorMessage = errorMessage))
                showToast.postValue(errorMessage)
            }

            movies.isEmpty() -> {
                renderState(MoviesState.Empty(message = "Nothing found"))
            }

            else -> {
                renderState(MoviesState.Content(movies = movies))
            }
        }
    }

    private fun renderState(state: MoviesState) {
        stateLiveData.postValue(state)
    }
}

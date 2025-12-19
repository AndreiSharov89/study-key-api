package com.example.key_api.presentation.ui.cast

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.key_api.domain.api.MoviesInteractor

class MoviesCastViewModel(
    private val movieId: String,
    private val moviesInteractor: MoviesInteractor,
) : ViewModel() {
    private val stateLiveData = MutableLiveData<MoviesCastState>()
    fun observeState(): LiveData<MoviesCastState> = stateLiveData

    init {
        // При старте экрана покажем ProgressBar
        stateLiveData.postValue(MoviesCastState.Loading)

        // Выполняем сетевой запрос
        moviesInteractor.getMovieCast(movieId, object : MoviesInteractor.MovieCastConsumer {

            // Обрабатываем результат этого запроса
            override fun consume(movieCast: MovieCast?, errorMessage: String?) {
                if (movieCast != null) {
                    stateLiveData.postValue(MoviesCastState.Content(movieCast))
                } else {
                    stateLiveData.postValue(MoviesCastState.Error(errorMessage ?: "Unknown error"))
                }
            }

        })
    }
}
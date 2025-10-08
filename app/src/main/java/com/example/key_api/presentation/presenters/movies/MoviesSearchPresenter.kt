package com.example.key_api.presentation.presenters.movies

import android.content.Context
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import com.example.key_api.R
import com.example.key_api.domain.api.MoviesInteractor
import com.example.key_api.domain.models.Movie
import com.example.key_api.util.Creator

class MoviesSearchPresenter(private val context: Context) {

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
        private val SEARCH_REQUEST_TOKEN = Any()
    }

    private val moviesInteractor = Creator.provideMoviesInteractor(context)

    private val movies = ArrayList<Movie>()

    private var view: MoviesView? = null
    private var state: MoviesState? = null
    private var latestSearchText: String? = null

    private val handler = Handler(Looper.getMainLooper())

    fun searchDebounce(changedText: String) {
        if (latestSearchText == changedText) {
            return
        }

        this.latestSearchText = changedText
        handler.removeCallbacksAndMessages(SEARCH_REQUEST_TOKEN)

        val searchRunnable = Runnable { searchRequest(changedText) }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            handler.postDelayed(
                searchRunnable,
                SEARCH_REQUEST_TOKEN,
                SEARCH_DEBOUNCE_DELAY
            )
        } else {
            val postTime = SystemClock.uptimeMillis() + SEARCH_DEBOUNCE_DELAY
            handler.postAtTime(
                searchRunnable,
                SEARCH_REQUEST_TOKEN,
                postTime,
            )
        }
    }

    private fun searchRequest(newSearchText: String) {
        if (newSearchText.isNotEmpty()) {
            renderState(MoviesState.Loading)

            moviesInteractor.searchMovies(newSearchText, object : MoviesInteractor.MoviesConsumer {
                override fun consume(foundMovies: List<Movie>?, errorMessage: String?) {
                    handler.post {
                        if (foundMovies != null) {
                            movies.clear()
                            movies.addAll(foundMovies)
                        }

                        when {
                            errorMessage != null -> {
                                renderState(
                                    MoviesState.Error(
                                        errorMessage = context.getString(R.string.something_went_wrong),
                                    )
                                )
                                view?.showToast(errorMessage)
                            }

                            movies.isEmpty() -> {
                                renderState(
                                    MoviesState.Empty(
                                        message = context.getString(R.string.nothing_found),
                                    )
                                )
                            }

                            else -> {
                                renderState(
                                    MoviesState.Content(
                                        movies = movies,
                                    )
                                )
                            }
                        }

                    }
                }
            })
        }
    }

    private fun renderState(state: MoviesState) {
        this.state = state
        this.view?.render(state)
    }

    fun onDestroy() {
        handler.removeCallbacksAndMessages(SEARCH_REQUEST_TOKEN)
    }

    fun attachView(view: MoviesView) {
        this.view = view
        state?.let { view.render(it) }
    }

    fun detachView() {
        this.view = null
    }
}
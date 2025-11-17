package com.example.key_api.domain.impl

import com.example.key_api.domain.api.MoviesInteractor
import com.example.key_api.domain.api.MoviesRepository
import com.example.key_api.util.Resource
import java.util.concurrent.Executors

class MoviesInteractorImpl(private val repository: MoviesRepository) : MoviesInteractor {
    private val executor = Executors.newCachedThreadPool()
    override fun searchMovies(expression: String, consumer: MoviesInteractor.MoviesConsumer) {
        executor.execute {
            try {
                when (val result = repository.searchMovies(expression)) {
                    is Resource.Success -> consumer.consume(result.data, null)
                    is Resource.Error -> consumer.consume(null, result.message)
                }
            } catch (e: Exception) {
                consumer.consume(null, "Unexpected error: ${e.localizedMessage ?: "unknown"}")
            }
        }
    }

    override fun getMoviesDetails(
        movieId: String,
        consumer: MoviesInteractor.MovieDetailsConsumer
    ) {
        executor.execute {
            try {
                when (val resource = repository.getMovieDetails(movieId)) {
                    is Resource.Success -> {
                        consumer.consume(resource.data, null)
                    }

                    is Resource.Error -> {
                        consumer.consume(resource.data, resource.message)
                    }
                }
            } catch (e: Exception) {
                consumer.consume(null, "Unexpected error: ${e.localizedMessage ?: "unknown"}")
            }
        }
    }
}
package com.example.key_api.domain.impl

import com.example.key_api.domain.api.MoviesInteractor
import com.example.key_api.domain.api.MoviesRepository
import com.example.key_api.util.Resource

class MoviesInteractorImpl(private val repository: MoviesRepository) : MoviesInteractor {

    override fun searchMovies(expression: String, consumer: MoviesInteractor.MoviesConsumer) {
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
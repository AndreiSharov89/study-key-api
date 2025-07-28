package com.example.key_api.domain.api

import com.example.key_api.domain.models.Movie

interface MoviesRepository {
    fun searchMovies(expression: String): List<Movie>
}
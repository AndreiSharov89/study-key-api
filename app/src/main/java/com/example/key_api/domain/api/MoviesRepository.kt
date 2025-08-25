package com.example.key_api.domain.api

import com.example.key_api.domain.models.Movie
import com.example.key_api.util.Resource

interface MoviesRepository {
    fun searchMovies(expression: String): Resource<List<Movie>>
}
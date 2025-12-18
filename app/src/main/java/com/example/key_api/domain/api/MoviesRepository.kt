package com.example.key_api.domain.api

import com.example.key_api.domain.models.Movie
import com.example.key_api.domain.models.MovieDetails
import com.example.key_api.util.Resource

interface MoviesRepository {
    fun searchMovies(expression: String): Resource<List<Movie>>
    fun getMovieDetails(movieId: String): Resource<MovieDetails>
    fun getMovieCast(movieId: String): Resource<MovieCast>
}
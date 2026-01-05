package com.example.key_api.domain.api

import com.example.key_api.domain.models.Movie
import com.example.key_api.domain.models.MovieCast
import com.example.key_api.domain.models.MovieDetails
import com.example.key_api.util.Resource
import kotlinx.coroutines.flow.Flow

interface MoviesRepository {
    suspend fun searchMovies(expression: String): Flow<Resource<List<Movie>>>
    suspend fun getMovieDetails(movieId: String): Flow<Resource<MovieDetails>>
    suspend fun getMovieCast(movieId: String): Flow<Resource<MovieCast>>
}
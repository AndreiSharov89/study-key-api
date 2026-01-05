package com.example.key_api.domain.api

import com.example.key_api.domain.models.Movie
import com.example.key_api.domain.models.MovieCast
import com.example.key_api.domain.models.MovieDetails
import kotlinx.coroutines.flow.Flow

interface MoviesInteractor {
    suspend fun searchMovies(expression: String): Flow<Pair<List<Movie>?, String?>>
    suspend fun getMoviesDetails(movieId: String): Flow<Pair<MovieDetails?, String?>>
    suspend fun getMovieCast(movieId: String): Flow<Pair<MovieCast?, String?>>
}

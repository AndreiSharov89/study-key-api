package com.example.key_api.domain.api

import com.example.key_api.domain.models.Movie
import com.example.key_api.domain.models.MovieCast
import com.example.key_api.domain.models.MovieDetails

interface MoviesInteractor {
    fun searchMovies(expression: String, consumer: MoviesConsumer)
    fun getMoviesDetails(movieId: String, consumer: MovieDetailsConsumer)
    fun getMovieCast(movieId: String, consumer: MovieCastConsumer)

    interface MoviesConsumer {
        fun consume(foundMovies: List<Movie>?, errorMessage: String?)
    }
    interface MovieDetailsConsumer {
        fun consume(movieDetails: MovieDetails?, errorMessage: String?)
    }
    interface MovieCastConsumer {
        fun consume(movieCast: MovieCast?, errorMessage: String?)
    }
}
package com.example.key_api.data.converter

import com.example.key_api.data.db.entity.MovieEntity
import com.example.key_api.domain.models.Movie

class MovieDbConverter {

    fun map(movie: Movie): MovieEntity {
        return MovieEntity(movie.id, movie.resultType, movie.image, movie.title, movie.description)
    }

    fun map(movie: MovieEntity): Movie {
        return Movie(movie.id, movie.resultType, movie.image, movie.title, movie.description)
    }
}
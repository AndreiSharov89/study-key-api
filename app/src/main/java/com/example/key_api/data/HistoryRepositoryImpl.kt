package com.example.key_api.data

import com.example.key_api.data.converter.MovieDbConverter
import com.example.key_api.data.db.AppDatabase
import com.example.key_api.data.db.entity.MovieEntity
import com.example.key_api.domain.db.HistoryRepository
import com.example.key_api.domain.models.Movie
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow


class HistoryRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val movieDbConverter: MovieDbConverter,
) : HistoryRepository {

    override fun historyMovies(): Flow<List<Movie>> = flow {
        val movies = appDatabase.movieDao().getMovies()
        emit(convertFromMovieEntity(movies))
    }

    private fun convertFromMovieEntity(movies: List<MovieEntity>): List<Movie> {
        return movies.map { movie -> movieDbConverter.map(movie) }
    }
}
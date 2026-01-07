package com.example.key_api.domain.db

import com.example.key_api.domain.models.Movie
import kotlinx.coroutines.flow.Flow

interface HistoryRepository {
    fun historyMovies(): Flow<List<Movie>>

}
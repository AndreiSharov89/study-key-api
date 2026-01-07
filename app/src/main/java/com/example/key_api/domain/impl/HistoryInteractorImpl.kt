package com.example.key_api.domain.impl

import com.example.key_api.domain.db.HistoryInteractor
import com.example.key_api.domain.db.HistoryRepository
import com.example.key_api.domain.models.Movie
import kotlinx.coroutines.flow.Flow

class HistoryInteractorImpl(
    private val historyRepository: HistoryRepository
) : HistoryInteractor {

    override fun historyMovies(): Flow<List<Movie>> {
        return historyRepository.historyMovies()
    }
}
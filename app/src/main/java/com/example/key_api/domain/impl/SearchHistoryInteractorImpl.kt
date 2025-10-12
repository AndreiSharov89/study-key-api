package com.example.key_api.domain.impl

import com.example.key_api.domain.api.SearchHistoryInteractor
import com.example.key_api.domain.api.SearchHistoryRepository
import com.example.key_api.domain.models.Movie

class SearchHistoryInteractorImpl(
    private val repository: SearchHistoryRepository
) : SearchHistoryInteractor {

    override fun getHistory(consumer: SearchHistoryInteractor.HistoryConsumer) {
        consumer.consume(repository.getHistory().data)
    }

    override fun saveToHistory(m: Movie) {
        repository.saveToHistory(m)
    }
}
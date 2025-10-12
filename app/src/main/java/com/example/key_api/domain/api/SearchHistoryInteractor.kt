package com.example.key_api.domain.api

import com.example.key_api.domain.models.Movie

interface SearchHistoryInteractor {
    fun getHistory(consumer: HistoryConsumer)
    fun saveToHistory(m: Movie)

    interface HistoryConsumer {
        fun consume(searchHistory: List<Movie>?)
    }
}
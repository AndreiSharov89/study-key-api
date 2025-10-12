package com.example.key_api.domain.api

import com.example.key_api.domain.models.Movie
import com.example.key_api.util.Resource

interface SearchHistoryRepository {
    fun saveToHistory(m: Movie)
    fun getHistory(): Resource<List<Movie>>
}
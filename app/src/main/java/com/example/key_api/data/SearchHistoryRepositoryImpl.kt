package com.example.key_api.data

import com.example.key_api.domain.api.SearchHistoryRepository
import com.example.key_api.domain.models.Movie
import com.example.key_api.util.Resource

class SearchHistoryRepositoryImpl(
    private val storage: StorageClient<ArrayList<Movie>>
) : SearchHistoryRepository {

    override fun saveToHistory(m: Movie) {
        val movies = storage.getData() ?: arrayListOf()
        movies.add(m)
        storage.storeData(movies)
    }

    override fun getHistory(): Resource<List<Movie>> {
        val movies = storage.getData() ?: listOf()
        return Resource.Success(movies)
    }
}

package com.example.key_api.domain.impl

import com.example.key_api.domain.api.NamesInteractor
import com.example.key_api.domain.api.NamesRepository
import com.example.key_api.domain.models.Person
import com.example.key_api.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class NamesInteracrorImpl(private val repository: NamesRepository) : NamesInteractor {

    override suspend fun searchNames(expression: String): Flow<Pair<List<Person>?, String?>> {
        return repository.searchNames(expression).map { result ->
            when (result) {
                is Resource.Success -> {
                    Pair(result.data, null)
                }

                is Resource.Error -> {
                    Pair(null, result.message)
                }
            }
        }
    }
}
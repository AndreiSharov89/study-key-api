package com.example.key_api.domain.api

import com.example.key_api.domain.models.Person
import com.example.key_api.util.Resource
import kotlinx.coroutines.flow.Flow

interface NamesRepository {
    suspend fun searchNames(expression: String): Flow<Resource<List<Person>>>
}
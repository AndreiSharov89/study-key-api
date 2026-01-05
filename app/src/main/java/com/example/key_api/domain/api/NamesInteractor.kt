package com.example.key_api.domain.api

import com.example.key_api.domain.models.Person
import kotlinx.coroutines.flow.Flow

interface NamesInteractor {

    suspend fun searchNames(expression: String): Flow<Pair<List<Person>?, String?>>
}
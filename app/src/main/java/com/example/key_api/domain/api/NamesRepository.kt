package com.example.key_api.domain.api

import com.example.key_api.domain.models.Person
import com.example.key_api.util.Resource

interface NamesRepository {

    suspend fun searchNames(expression: String): Resource<List<Person>>
}
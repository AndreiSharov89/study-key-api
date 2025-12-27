package com.example.key_api.domain.api

import com.example.key_api.domain.models.Person

interface NamesInteractor {

    suspend fun searchNames(expression: String, consumer: NamesConsumer)

    interface NamesConsumer {
        fun consume(foundNames: List<Person>?, errorMessage: String?)
    }
}
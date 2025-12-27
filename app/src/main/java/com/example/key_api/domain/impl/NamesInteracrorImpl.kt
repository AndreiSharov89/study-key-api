package com.example.key_api.domain.impl

import com.example.key_api.domain.api.NamesInteractor
import com.example.key_api.domain.api.NamesRepository
import com.example.key_api.util.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class NamesInteracrorImpl(private val repository: NamesRepository) : NamesInteractor {

    override suspend fun searchNames(expression: String, consumer: NamesInteractor.NamesConsumer) {
        // Use withContext(Dispatchers.IO) to switch to the background thread correctly
        withContext(Dispatchers.IO) {
            val resource = repository.searchNames(expression)

            // Switch back to Main thread if your consumer interacts with the UI (optional, but safer)
            withContext(Dispatchers.Main) {
                when (resource) {
                    is Resource.Success -> {
                        consumer.consume(resource.data, null)
                    }

                    is Resource.Error -> {
                        // Pass null for data and the message for the error
                        consumer.consume(null, resource.message)
                    }
                }
            }
        }
    }
}
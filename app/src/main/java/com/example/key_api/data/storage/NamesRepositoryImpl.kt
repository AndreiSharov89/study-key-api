package com.example.key_api.data.storage

import android.util.Log
import com.example.key_api.data.NetworkClient
import com.example.key_api.data.dto.NamesRequest
import com.example.key_api.data.dto.NamesResponse
import com.example.key_api.data.dto.NamesSearchRequest
import com.example.key_api.data.dto.NamesSearchResponse
import com.example.key_api.domain.api.NamesRepository
import com.example.key_api.domain.models.Person
import com.example.key_api.util.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext

class NamesRepositoryImpl(private val networkClient: NetworkClient) : NamesRepository {

    override suspend fun searchNames(expression: String): Resource<List<Person>> =
        withContext(Dispatchers.IO) {
            val response = networkClient.doRequest(NamesSearchRequest(expression))

            when (response.resultCode) {
                -1 -> Resource.Error("Проверьте подключение к интернету")
                200 -> {
                    val searchResults = (response as NamesSearchResponse).results.take(10)

                    // 2. Use coroutineScope to run detail requests in parallel
                    coroutineScope {
                        val deferredPeople = searchResults.map { item ->
                            async {
                                Person(
                                    id = item.id,
                                    name = item.title,
                                    // Call the detail function for each ID
                                    description = getPersonDescription(item.id),
                                    photoUrl = item.image
                                )
                            }
                        }
                        // Wait for ALL parallel calls to finish
                        Resource.Success(deferredPeople.awaitAll())
                    }
                }

                else -> Resource.Error("Ошибка сервера")
            }
        }

    private fun getPersonDescription(id: String): String {
        val response = networkClient.doRequest(NamesRequest(id))

        // Fix 1: Correct the debug log (use 'response' instead of 'myData')
        Log.d(
            "DEBUG",
            "Request ID: $id, Response Type: ${response::class.java.simpleName}, Code: ${response.resultCode}"
        )

        return if (response.resultCode == 200) {
            // Fix 2: Check the type before casting to avoid ClassCastException
            if (response is NamesResponse && response.result != null) {
                response.result.summary ?: "No summary available"
            } else {
                // This is where your crash was happening.
                // If it hits this line, your NetworkClient is returning the wrong class type.
                Log.e("ERROR", "Expected NamesResponse but got ${response::class.java.simpleName}")
                "Data format error"
            }
        } else {
            "Error code: ${response.resultCode}"
        }
    }
}
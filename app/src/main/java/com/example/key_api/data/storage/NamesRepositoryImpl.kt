package com.example.key_api.data.storage

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

        return if (response.resultCode == 200) {
            if (response is NamesResponse) {
                val rawSummary = response.summary ?: "No summary available"
                val personName = response.name ?: "" // Get the name to remove it from summary

                // 1. Unescape HTML (removes &quot;, &apos;, etc.)
                val cleanSummary =
                    android.text.Html.fromHtml(rawSummary, android.text.Html.FROM_HTML_MODE_LEGACY)
                        .toString()

                // 2. Shorten and clean the text
                shortenSummary(cleanSummary, personName)
            } else {
                "Data format error"
            }
        } else {
            "Error code: ${response.resultCode}"
        }
    }

    private fun shortenSummary(text: String, name: String): String {
        // 1. Remove the repeating name from the beginning if it exists
        // Regex looks for the name followed by a dot and space at the very start
        var processedText = if (name.isNotEmpty() && text.startsWith("$name.")) {
            text.removePrefix("$name.").trimStart()
        } else {
            text
        }

        // 2. Trim after the second dot
        val sentences = processedText.split(". ")
        return if (sentences.size > 2) {
            "${sentences[0]}. ${sentences[1]}..."
        } else {
            processedText
        }
    }
}
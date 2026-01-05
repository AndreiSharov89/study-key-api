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
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class NamesRepositoryImpl(private val networkClient: NetworkClient) : NamesRepository {

    override suspend fun searchNames(expression: String): Flow<Resource<List<Person>>> = flow {
        val response = networkClient.doRequest(NamesSearchRequest(expression))

        when (response.resultCode) {
            -1 -> emit(Resource.Error<List<Person>>("Проверьте подключение к интернету"))
            200 -> {
                val searchResponse = response as NamesSearchResponse
                val people = searchResponse.results.map {
                    Person(
                        id = it.id,
                        name = it.title,
                        description = "Loading . . .",
                        photoUrl = it.image
                    )
                }.toMutableList()

                emit(Resource.Success(people.toList()))

                people.forEachIndexed { index, person ->
                    delay(200L)
                    val description = getPersonDescription(person.id)
                    people[index] = person.copy(description = description)
                    emit(Resource.Success(people.toList()))
                }
            }

            else -> emit(Resource.Error<List<Person>>("Ошибка сервера"))
        }
    }.flowOn(Dispatchers.IO)

    private suspend fun getPersonDescription(id: String): String {
        val response = networkClient.doRequest(NamesRequest(id))

        return if (response.resultCode == 200) {
            if (response is NamesResponse) {
                val rawSummary = response.summary ?: "No summary available"
                val personName = response.name ?: ""

                val cleanSummary = android.text.Html.fromHtml(
                    rawSummary,
                    android.text.Html.FROM_HTML_MODE_LEGACY
                ).toString()

                shortenSummary(cleanSummary, personName)
            } else {
                "Data format error"
            }
        } else {
            "Error code: ${response.resultCode}"
        }
    }

    private fun shortenSummary(text: String, name: String): String {
        val processedText = if (name.isNotEmpty() && text.startsWith("$name.")) {
            text.removePrefix("$name.").trimStart()
        } else {
            text
        }

        val sentences = processedText.split(". ")
        return if (sentences.size > 2) {
            "${sentences[0]}. ${sentences[1]}..."
        } else {
            processedText
        }
    }
}

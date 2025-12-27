package com.example.key_api.data.dto

class NamesResponse(
    val searchType: String,
    val expression: String,
    val result: PersonShortDto
) : Response()
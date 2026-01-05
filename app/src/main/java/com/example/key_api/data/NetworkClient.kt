package com.example.key_api.data

import com.example.key_api.data.dto.Response

interface NetworkClient {
    suspend fun doRequest(dto: Any): Response
}
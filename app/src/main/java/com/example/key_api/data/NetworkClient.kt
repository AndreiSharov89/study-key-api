package com.example.key_api.data

import com.example.key_api.data.dto.Response

interface NetworkClient {
    fun doRequest(dto: Any): Response

}
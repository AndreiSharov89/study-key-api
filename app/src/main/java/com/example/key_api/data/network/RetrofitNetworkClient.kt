package com.example.key_api.data.network

import com.example.key_api.BuildConfig //not so clear until DI
import com.example.key_api.data.NetworkClient
import com.example.key_api.data.dto.MoviesSearchRequest
import com.example.key_api.data.dto.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitNetworkClient(private val apiKey: String) : NetworkClient {
    val key: String = BuildConfig.API_KEY //but secure for VCS
    private val imdbBaseUrl = "https://tv-api.com"

    private val retrofit = Retrofit.Builder()
        .baseUrl(imdbBaseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val imdbService = retrofit.create(ImdbApi::class.java)

    override fun doRequest(dto: Any): Response {
        if (dto is MoviesSearchRequest) {
            val resp = imdbService.getMovies(key, dto.expression).execute()

            val body = resp.body() ?: Response()

            return body.apply { resultCode = resp.code() }
        } else {
            return Response().apply { resultCode = 400 }
        }
    }
}
package com.example.key_api.data.network

import com.example.key_api.data.dto.MoviesSearchResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface ImdbApi {
    @GET("/en/API/SearchMovie/{key}/{expression}")
    fun getMovies(
        @Path("key") key: String,
        @Path("expression") expression: String
    ): Call<MoviesSearchResponse>
}
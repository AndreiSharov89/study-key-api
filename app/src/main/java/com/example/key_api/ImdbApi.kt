package com.example.key_api

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ImdbApi {
    @GET("/en/API/SearchMovie/{key}/{expression}")
    fun getMovies(@Path("key") key: String, @Path("expression") expression: String): Call<MoviesResponse>
}
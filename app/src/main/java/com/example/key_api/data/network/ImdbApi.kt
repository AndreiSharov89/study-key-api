package com.example.key_api.data.network

import com.example.key_api.data.dto.MovieDetailsResponse
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

    @GET("/en/API/Title/{key}/{movie_id}")
    fun getMovieDetails(
        @Path("key") key: String,
        @Path("movie_id") movieId: String
    ): Call<MovieDetailsResponse>

}
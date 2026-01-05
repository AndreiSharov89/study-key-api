package com.example.key_api.data.network

import com.example.key_api.data.dto.MovieCastResponse
import com.example.key_api.data.dto.MovieDetailsResponse
import com.example.key_api.data.dto.MoviesSearchResponse
import com.example.key_api.data.dto.NamesResponse
import com.example.key_api.data.dto.NamesSearchResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface ImdbApi {
    @GET("/en/API/SearchMovie/{key}/{expression}")
    suspend fun getMovies(
        @Path("key") key: String,
        @Path("expression") expression: String
    ): MoviesSearchResponse

    @GET("/en/API/Title/{key}/{movie_id}")
    suspend fun getMovieDetails(
        @Path("key") key: String,
        @Path("movie_id") movieId: String
    ): MovieDetailsResponse

    @GET("/en/API/FullCast/{key}/{movie_id}")
    suspend fun getFullCast(
        @Path("key") key: String,
        @Path("movie_id") movieId: String
    ): MovieCastResponse

    @GET("/en/API/SearchName/{key}/{expression}")
    suspend fun searchNames(
        @Path("key") key: String,
        @Path("expression") expression: String
    ): NamesSearchResponse

    @GET("/en/API/Name/{key}/{id}")
    suspend fun names(
        @Path("key") key: String,
        @Path("id") id: String
    ): NamesResponse
}

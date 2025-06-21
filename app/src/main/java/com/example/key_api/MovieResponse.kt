package com.example.key_api

class MoviesResponse(val searchType: String,
                     val expression: String,
                     val results: List<Movie>)

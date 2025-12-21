package com.example.key_api.data.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.example.key_api.BuildConfig
import com.example.key_api.data.NetworkClient
import com.example.key_api.data.dto.MovieCastRequest
import com.example.key_api.data.dto.MovieDetailsRequest
import com.example.key_api.data.dto.MoviesSearchRequest
import com.example.key_api.data.dto.Response

class RetrofitNetworkClient(
    private val imdbService: ImdbApi,
    private val context: Context
) : NetworkClient {

    private fun isConnected(): Boolean {
        val connectivityManager = context.getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager
        val capabilities =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        if (capabilities != null) {
            when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> return true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> return true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> return true
            }
        }
        return false
    }

    override fun doRequest(dto: Any): Response {
        if (!isConnected()) {
            return Response().apply { resultCode = -1 }
        }

        if ((dto !is MoviesSearchRequest) && (dto !is MovieDetailsRequest) && (dto !is MovieCastRequest)) {
            return Response().apply { resultCode = 400 }
        }
        val response = when (dto) {
            is MoviesSearchRequest -> imdbService.getMovies(
                BuildConfig.IMDB_API_KEY,
                dto.expression
            ).execute()

            is MovieDetailsRequest -> imdbService.getMovieDetails(
                BuildConfig.IMDB_API_KEY,
                dto.movieId
            ).execute()

            else -> imdbService.getFullCast(
                BuildConfig.IMDB_API_KEY,
                (dto as MovieCastRequest).movieId
            ).execute()
        }

        val body = response.body()
        return if (body != null) {
            body.apply { resultCode = response.code() }
        } else {
            Response().apply { resultCode = response.code() }
        }
    }
}

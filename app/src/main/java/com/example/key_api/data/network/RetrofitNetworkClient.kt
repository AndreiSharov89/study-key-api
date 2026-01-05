package com.example.key_api.data.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.example.key_api.BuildConfig
import com.example.key_api.data.NetworkClient
import com.example.key_api.data.dto.MovieCastRequest
import com.example.key_api.data.dto.MovieDetailsRequest
import com.example.key_api.data.dto.MoviesSearchRequest
import com.example.key_api.data.dto.NamesRequest
import com.example.key_api.data.dto.NamesSearchRequest
import com.example.key_api.data.dto.Response
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RetrofitNetworkClient(
    private val imdbService: ImdbApi,
    private val context: Context
) : NetworkClient {

    override suspend fun doRequest(dto: Any): Response {
        if (!isConnected()) {
            return Response().apply { resultCode = -1 }
        }

        if ((dto !is MoviesSearchRequest) && (dto !is MovieDetailsRequest) && (dto !is MovieCastRequest)
            && (dto !is NamesSearchRequest) && (dto !is NamesRequest)
        ) {
            return Response().apply { resultCode = 400 }
        }

        return withContext(Dispatchers.IO) {
            try {
                val response = when (dto) {
                    is NamesSearchRequest -> imdbService.searchNames(IMDB_API_KEY, dto.expression)
                    is NamesRequest -> imdbService.names(IMDB_API_KEY, dto.id)
                    is MoviesSearchRequest -> imdbService.getMovies(IMDB_API_KEY, dto.expression)
                    is MovieDetailsRequest -> imdbService.getMovieDetails(IMDB_API_KEY, dto.movieId)
                    is MovieCastRequest -> imdbService.getFullCast(IMDB_API_KEY, dto.movieId)
                    else -> throw IllegalArgumentException("Unknown request type")
                }
                response.apply { resultCode = 200 }
            } catch (e: Exception) {
                Response().apply { resultCode = 500 }
            }
        }
    }

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

    companion object {
        private const val IMDB_API_KEY = BuildConfig.IMDB_API_KEY
    }
}

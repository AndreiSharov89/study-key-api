package com.example.key_api.data.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.example.key_api.BuildConfig //not so clear until DI
import com.example.key_api.data.NetworkClient
import com.example.key_api.data.dto.MoviesSearchRequest
import com.example.key_api.data.dto.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitNetworkClient(private val apiKey: String, private val context: Context) :
    NetworkClient {
    val key: String = BuildConfig.API_KEY //but secure for VCS
    private val imdbBaseUrl = "https://tv-api.com"

    private val retrofit = Retrofit.Builder()
        .baseUrl(imdbBaseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val imdbService = retrofit.create(ImdbApi::class.java)

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
        if (isConnected() == false) {
            return Response().apply { resultCode = -1 }
        }
        if (dto !is MoviesSearchRequest) {
            return Response().apply { resultCode = 400 }
        }
            val resp = imdbService.getMovies(key, dto.expression).execute()
        val body = resp.body()
        return if (body != null) {
            body.apply { resultCode = resp.code() }
        } else {
            Response().apply { resultCode = resp.code() }
        }
    }
}
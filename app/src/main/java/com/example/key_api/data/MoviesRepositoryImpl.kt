package com.example.key_api.data

import com.example.key_api.data.converter.MovieCastConverter
import com.example.key_api.data.dto.MovieCastRequest
import com.example.key_api.data.dto.MovieCastResponse
import com.example.key_api.data.dto.MovieDetailsRequest
import com.example.key_api.data.dto.MovieDetailsResponse
import com.example.key_api.data.dto.MoviesSearchRequest
import com.example.key_api.data.dto.MoviesSearchResponse
import com.example.key_api.domain.api.MoviesRepository
import com.example.key_api.domain.models.Movie
import com.example.key_api.domain.models.MovieCast
import com.example.key_api.domain.models.MovieDetails
import com.example.key_api.util.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class MoviesRepositoryImpl(
    private val networkClient: NetworkClient,
    private val movieCastConverter: MovieCastConverter
) : MoviesRepository {

    override suspend fun searchMovies(expression: String): Flow<Resource<List<Movie>>> = flow {
        val response = networkClient.doRequest(MoviesSearchRequest(expression))
        when (response.resultCode) {
            -1 -> emit(Resource.Error("Проверьте подключение к интернету"))

            200 -> {
                val searchResponse = response as MoviesSearchResponse
                emit(Resource.Success(searchResponse.results.map {
                    Movie(it.id, it.resultType, it.image, it.title, it.description)
                }))
            }

            else -> emit(Resource.Error("Ошибка сервера"))
        }
    }.flowOn(Dispatchers.IO)

    override suspend fun getMovieDetails(movieId: String): Flow<Resource<MovieDetails>> = flow {
        val response = networkClient.doRequest(MovieDetailsRequest(movieId))
        when (response.resultCode) {
            -1 -> emit(Resource.Error("Проверьте подключение к интернету"))
            200 -> {
                val movieDetailsResponse = response as MovieDetailsResponse
                emit(
                    Resource.Success(
                        MovieDetails(
                            movieDetailsResponse.id,
                            movieDetailsResponse.title,
                            movieDetailsResponse.imDbRating,
                            movieDetailsResponse.year,
                            movieDetailsResponse.countries,
                            movieDetailsResponse.genres,
                            movieDetailsResponse.directors,
                            movieDetailsResponse.writers,
                            movieDetailsResponse.stars,
                            movieDetailsResponse.plot
                    )
                    )
                )
            }

            else -> emit(Resource.Error<MovieDetails>("Ошибка сервера"))
        }
    }.flowOn(Dispatchers.IO)

    override suspend fun getMovieCast(movieId: String): Flow<Resource<MovieCast>> = flow {
        val response = networkClient.doRequest(MovieCastRequest(movieId))
        when (response.resultCode) {
            -1 -> emit(Resource.Error("Проверьте подключение к интернету"))

            200 -> {
                val movieCastResponse = response as MovieCastResponse
                emit(Resource.Success(movieCastConverter.convert(movieCastResponse)))
            }

            else -> emit(Resource.Error<MovieCast>("Ошибка сервера"))
        }
    }.flowOn(Dispatchers.IO)
}

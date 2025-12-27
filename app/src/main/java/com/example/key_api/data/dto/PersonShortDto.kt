package com.example.key_api.data.dto

data class PersonShortDto(
    val id: String,
    val name: String?,
    val role: String?,
    val image: String?,
    val summary: String?,
    val birthDate: String?,
    val deathDate: String?,
    val awards: String?,
    val height: String?,
    val knownFor: List<KnownForDto>?,
    val castMovies: List<CastMovie>?
) {
    data class KnownForDto(
        val id: String,
        val title: String,
        val fullTitle: String?,
        val year: String?,
        val role: String?,
        val image: String?
    )

    data class CastMovie(
        val id: String,
        val role: String?,
        val title: String?,
        val year: String?,
        val description: String?
    )
}


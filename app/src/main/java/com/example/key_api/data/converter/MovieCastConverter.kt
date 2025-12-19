package com.example.key_api.data.converter

import com.example.key_api.data.dto.ActorResponse
import com.example.key_api.data.dto.CastItemResponse
import com.example.key_api.data.dto.DirectorsResponse
import com.example.key_api.data.dto.MovieCastResponse
import com.example.key_api.data.dto.OtherResponse
import com.example.key_api.data.dto.WritersResponse
import com.example.key_api.domain.models.MovieCast
import com.example.key_api.domain.models.MovieCastPerson

class MovieCastConverter {

    fun convert(response: MovieCastResponse): MovieCast {
        return with(response) {
            MovieCast(
                imdbId = this.imDbId,
                fullTitle = this.fullTitle,
                directors = convertDirectors(this.directors),
                others = convertOthers(this.others),
                writers = convertWriters(this.writers),
                actors = convertActors(this.actors)
            )
        }
    }

    private fun convertDirectors(directorsResponse: DirectorsResponse): List<MovieCastPerson> {
        return directorsResponse.items.map { it.toMovieCastPerson() }
    }

    private fun convertOthers(othersResponses: List<OtherResponse>): List<MovieCastPerson> {
        return othersResponses.flatMap { otherResponse ->
            otherResponse.items.map { it.toMovieCastPerson(jobPrefix = otherResponse.job) }
        }
    }

    private fun convertWriters(writersResponse: WritersResponse): List<MovieCastPerson> {
        return writersResponse.items.map { it.toMovieCastPerson() }
    }

    private fun convertActors(actorsResponses: List<ActorResponse>): List<MovieCastPerson> {
        return actorsResponses.map { actor ->
            MovieCastPerson(
                id = actor.id,
                name = actor.name,
                description = actor.asCharacter,
                image = actor.image,
            )
        }
    }

    private fun CastItemResponse.toMovieCastPerson(jobPrefix: String = ""): MovieCastPerson {
        return MovieCastPerson(
            id = this.id,
            name = this.name,
            description = if (jobPrefix.isEmpty()) this.description else "$jobPrefix -- ${this.description}",
            image = null,
        )
    }

}
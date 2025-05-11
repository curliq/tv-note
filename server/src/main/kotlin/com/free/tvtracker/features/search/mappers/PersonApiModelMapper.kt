package com.free.tvtracker.features.search.mappers

import com.free.tvtracker.base.Mapper
import com.free.tvtracker.details.response.Credits
import com.free.tvtracker.details.response.Profile
import com.free.tvtracker.details.response.TmdbPersonDetailsApiModel
import com.free.tvtracker.tmdb.data.TmdbPersonResponse
import org.springframework.stereotype.Component

@Component
class PersonApiModelMapper : Mapper<TmdbPersonResponse, TmdbPersonDetailsApiModel> {
    override fun map(from: TmdbPersonResponse): TmdbPersonDetailsApiModel {
        return TmdbPersonDetailsApiModel(
            id = from.id,
            biography = from.biography,
            birthday = from.birthday,
            deathday = from.deathday,
            imdbId = from.imdbId,
            knownForDepartment = from.knownForDepartment,
            name = from.name,
            placeOfBirth = from.placeOfBirth,
            profilePath = from.profilePath,
            credits = Credits(
                cast = from.tvCredits.cast?.sortedWith(
                    compareBy(
                        { (it.episodeCount ?: 0) < 3 },
                        { -(it.voteCount ?: 0L) }
                    )
                )?.map {
                    Credits.Cast(
                        id = it.id,
                        posterPath = it.posterPath,
                        name = it.name,
                        voteCount = it.voteCount,
                    )
                } ?: emptyList(),
                crew = from.tvCredits.crew?.sortedWith(
                    compareBy(
                        { (it.episodeCount ?: 0) < 3 },
                        { -(it.voteCount ?: 0L) }
                    )
                )?.map {
                    Credits.Crew(
                        id = it.id,
                        posterPath = it.posterPath,
                        name = it.name,
                        voteCount = it.voteCount,
                    )
                } ?: emptyList()),
            movieCredits = Credits(
                cast = from.movieCredits.cast?.sortedByDescending { it.voteCount }?.map {
                    Credits.Cast(
                        id = it.id,
                        posterPath = it.posterPath,
                        name = it.title,
                        voteCount = it.voteCount,
                    )
                } ?: emptyList(),
                crew = from.movieCredits.crew?.sortedByDescending { it.voteCount }?.map {
                    Credits.Crew(
                        id = it.id,
                        posterPath = it.posterPath,
                        name = it.title,
                        voteCount = it.voteCount,
                    )
                } ?: emptyList(),
            ),
            images = from.images.profiles.map { Profile(it.filePath, it.voteCount) },
            instagramId = from.externalIds.instagramId,
            tiktokId = from.externalIds.tiktokId,
            twitterId = from.externalIds.twitterId,
        )
    }
}

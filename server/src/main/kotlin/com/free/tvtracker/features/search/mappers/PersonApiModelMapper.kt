package com.free.tvtracker.features.search.mappers

import com.free.tvtracker.base.Mapper
import com.free.tvtracker.tmdb.data.TmdbPersonResponse
import com.free.tvtracker.discover.response.MovieCredits
import com.free.tvtracker.discover.response.Profile
import com.free.tvtracker.discover.response.TmdbPersonDetailsApiModel
import com.free.tvtracker.discover.response.TvCredits
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
            tvCredits = TvCredits(
                from.tvCredits.cast?.sortedWith(compareBy({ it.episodeCount!! < 3 }, { -it.voteCount!! }))?.map {
                    TvCredits.Cast(
                        id = it.id,
                        posterPath = it.posterPath,
                        name = it.name,
                        voteCount = it.voteCount,
                    )
                } ?: emptyList(),
                from.tvCredits.crew?.sortedWith(compareBy({ it.episodeCount!! < 3 }, { -it.voteCount!! }))?.map {
                    TvCredits.Crew(
                        id = it.id,
                        posterPath = it.posterPath,
                        name = it.name,
                        voteCount = it.voteCount,
                    )
                } ?: emptyList()),
            movieCredits = MovieCredits(from.movieCredits.cast?.sortedByDescending { it.voteCount }?.map {
                MovieCredits.Cast(
                    id = it.id,
                    posterPath = it.posterPath,
                    title = it.title,
                    voteCount = it.voteCount,
                )
            }, from.movieCredits.crew?.sortedByDescending { it.voteCount }?.map {
                MovieCredits.Crew(
                    id = it.id,
                    posterPath = it.posterPath,
                    title = it.title,
                    voteCount = it.voteCount,
                )
            }),
            images = from.images.profiles.map { Profile(it.filePath, it.voteCount) },
            instagramId = from.externalIds.instagramId,
            tiktokId = from.externalIds.tiktokId,
            twitterId = from.externalIds.twitterId,
        )
    }
}

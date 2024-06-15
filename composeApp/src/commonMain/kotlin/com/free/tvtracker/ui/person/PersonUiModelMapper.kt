package com.free.tvtracker.ui.person

import com.free.tvtracker.base.Mapper
import com.free.tvtracker.discover.response.Profile
import com.free.tvtracker.discover.response.TmdbPersonDetailsApiModel
import com.free.tvtracker.discover.response.TvCredits
import com.free.tvtracker.ui.common.TmdbConfigData
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.format
import kotlinx.datetime.format.MonthNames
import kotlinx.datetime.format.Padding
import kotlinx.datetime.format.char
import kotlinx.datetime.periodUntil

class PersonUiModelMapper(
    private val castMapper: PersonCastUiModelMapper,
    private val crewMapper: PersonCrewUiModelMapper,
    private val photoMapper: PersonPhotoUiModelMapper,
    private val clock: Instant = Clock.System.now(),
) : Mapper<TmdbPersonDetailsApiModel, PersonUiModel> {
    override fun map(from: TmdbPersonDetailsApiModel): PersonUiModel {
        val castShows = from.tvCredits.cast.map(castMapper::map)
        val crewShows = from.tvCredits.crew.map(crewMapper::map)
        val birthDate = LocalDate.parse(input = from.birthday ?: "")
        val birthday = birthDate.format(LocalDate.Format {
            dayOfMonth(padding = Padding.NONE); char(' ')
            monthName(MonthNames.ENGLISH_FULL); char(' ')
            year()
        })
        val age = birthDate.periodUntil(LocalDate.fromEpochDays(clock.epochSeconds.toInt() / 24 / 60 / 60)).years
        return PersonUiModel(
            photoUrl = TmdbConfigData.get().getPosterUrl(from.profilePath),
            name = from.name,
            job = from.knownForDepartment,
            dob = "${birthday} (age $age)",
            bornIn = from.placeOfBirth ?: "",
            bio = from.biography ?: "",
            movies = emptyList(),
            moviesCount = 0,
            firstTwoMovies = emptyList(),
            tvShowsCast = castShows,
            tvShowsCrew = crewShows,
            firstTwoTvShows = if (castShows.size > crewShows.size) (castShows + crewShows).take(2) else (crewShows + castShows).take(2),
            tvShowsCount = castShows.size + crewShows.size,
            photos = from.images.map(photoMapper::map),
            firstTwoPhotos = from.images.take(2).map(photoMapper::map),
            instagramTag = if (from.instagramId != null) "@${from.instagramId}" else null,
            instagramUrl = "http://instagram.com/_u/${from.instagramId}"
        )
    }
}

class PersonCastUiModelMapper : Mapper<TvCredits.Cast, PersonUiModel.TvShow> {
    override fun map(from: TvCredits.Cast): PersonUiModel.TvShow {
        return PersonUiModel.TvShow(
            tmdbId = from.id.toInt(),
            posterUrl = TmdbConfigData.get().getPosterUrl(from.posterPath),
            name = from.name ?: "(missing title)",
            voteCount = from.voteCount?.toInt() ?: 0
        )
    }
}

class PersonCrewUiModelMapper : Mapper<TvCredits.Crew, PersonUiModel.TvShow> {
    override fun map(from: TvCredits.Crew): PersonUiModel.TvShow {
        return PersonUiModel.TvShow(
            tmdbId = from.id.toInt(),
            posterUrl = TmdbConfigData.get().getPosterUrl(from.posterPath),
            name = from.name ?: "(missing title)",
            voteCount = from.voteCount?.toInt() ?: 0
        )
    }
}

class PersonPhotoUiModelMapper : Mapper<Profile, String> {
    override fun map(from: Profile): String {
        return TmdbConfigData.get().getPosterUrl(from.filePath)
    }
}


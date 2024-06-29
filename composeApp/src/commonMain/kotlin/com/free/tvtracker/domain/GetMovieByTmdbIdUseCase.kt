package com.free.tvtracker.domain

import com.free.tvtracker.data.search.SearchRepository
import com.free.tvtracker.data.tracked.TrackedShowsRepository
import com.free.tvtracker.details.response.TmdbMovieDetailsApiResponse
import com.free.tvtracker.expect.data.CachingLocationService
import com.free.tvtracker.tracked.response.TrackedContentApiModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow

class GetMovieByTmdbIdUseCase(
    private val trackedShowsRepository: TrackedShowsRepository,
    private val searchRepository: SearchRepository,
    private val locationService: CachingLocationService,
) {
    data class GetTrackedShowByTmdbIdResult(
        val showData: TmdbMovieDetailsApiResponse,
        val tracked: TrackedContentApiModel?
    )

    operator fun invoke(tmdbShowId: Int): Flow<GetTrackedShowByTmdbIdResult> {
        val countryCode = locationService.getCountryCode()
        val movieResponse = flow {
            val res = searchRepository.getMovie(tmdbShowId, countryCode = countryCode)
            emit(res)
        }
        val trackedShow = trackedShowsRepository.getByTmdbIdFlow(tmdbShowId)
        return combine(movieResponse, trackedShow, transform = { a, b ->
            GetTrackedShowByTmdbIdResult(a, b)
        })
    }
}

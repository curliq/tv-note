package com.free.tvtracker.domain

import android.telephony.TelephonyManager
import com.free.tvtracker.data.search.SearchRepository
import com.free.tvtracker.data.tracked.TrackedShowsRepository
import com.free.tvtracker.data.tracked.WatchedEpisodesTaskQueue
import com.free.tvtracker.discover.response.TmdbShowDetailsApiResponse
import com.free.tvtracker.tracked.response.TrackedShowApiModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow


class GetTrackedShowByTmdbIdUseCase(
    private val watchedEpisodesTaskQueue: WatchedEpisodesTaskQueue,
    private val trackedShowsRepository: TrackedShowsRepository,
    private val searchRepository: SearchRepository,
    private val trackedShowReducer: TrackedShowReducer
) {
    data class GetTrackedShowByTmdbIdResult(val showData: TmdbShowDetailsApiResponse, val tracked: TrackedShowApiModel?)

    operator fun invoke(tmdbShowId: Int): Flow<GetTrackedShowByTmdbIdResult> {
        val showDataResponse = flow {
            val tm: TelephonyManager =
                this.getSystemService(android.content.Context.TELEPHONY_SERVICE) as TelephonyManager
            val countryCodeValue: String = tm.getNetworkCountryIso()
            val res = searchRepository.getShow(tmdbShowId, includeEpisodes = true, countryCode = )
            emit(res)
        }
        val trackedShow = trackedShowsRepository.getShowByTmdbIdFlow(tmdbShowId)
        val watchedEpisodes = watchedEpisodesTaskQueue.watchedEpisodeOrders
        return combine(showDataResponse, trackedShow, watchedEpisodes, transform = { a, b, c ->
            GetTrackedShowByTmdbIdResult(a, if (b == null) null else trackedShowReducer.reduce(b, c))
        }).catch { it.printStackTrace() }
    }
}

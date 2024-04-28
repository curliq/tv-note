package com.free.tvtracker.data.tracked

import com.free.tvtracker.Endpoints
import com.free.tvtracker.core.data.http.TvHttpClient
import com.free.tvtracker.core.data.sql.LocalSqlDataProvider
import com.free.tvtracker.data.tracked.entities.MarkEpisodeWatchedOrderClientEntity
import com.free.tvtracker.tracked.request.AddEpisodesRequest
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow

class WatchedEpisodesTaskQueue(
    private val remoteDataSource: TvHttpClient,
    private val localDataSource: LocalSqlDataProvider,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) {
    val watchedEpisodeOrders: MutableStateFlow<List<MarkEpisodeWatchedOrderClientEntity>> =
        MutableStateFlow(emptyList())

    suspend fun emitOrders() {
        val pendingOrders = localDataSource.getEpisodeWatchedOrder()
        watchedEpisodeOrders.value = pendingOrders
        trySync(pendingOrders)
    }

    suspend fun add(orders: List<MarkEpisodeWatchedOrderClientEntity>) {
        watchedEpisodeOrders.value = watchedEpisodeOrders.value.plus(orders)
        localDataSource.saveEpisodeWatchedOrder(orders)
        trySync(orders)
    }

    private suspend fun trySync(orders: List<MarkEpisodeWatchedOrderClientEntity>) {
        try {
            val eps = orders.map { AddEpisodesRequest.Episode(it.showId.toInt(), it.episodeId.toInt()) }
            remoteDataSource.call(
                Endpoints.addEpisodes,
                AddEpisodesRequest(eps)
            )
                .asSuccess { data ->
                    val watchedEps = data.map { responseEp ->
                        val showId = eps.first { it.episodeId == responseEp.storedEpisodeId }.trackedShowId
                        responseEp.toClientEntity(showId.toLong())
                    }
                    localDataSource.saveWatchedEpisodes(watchedEps)
                    localDataSource.deleteEpisodeWatchedOrder(orders.map { it.id })
                }
                .asError {

                }
        } catch (e: Throwable) {

        }
    }

}

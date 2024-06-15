package com.free.tvtracker.data.tracked

import com.free.tvtracker.Endpoints
import com.free.tvtracker.expect.data.TvHttpClient
import com.free.tvtracker.data.common.sql.LocalSqlDataProvider
import com.free.tvtracker.data.tracked.entities.MarkEpisodeWatchedOrderClientEntity
import com.free.tvtracker.tracked.request.AddEpisodesApiRequestBody
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
        if (orders.isEmpty()) return
        try {
            val eps = orders.map { AddEpisodesApiRequestBody.Episode(it.showId.toInt(), it.episodeId.toInt()) }
            remoteDataSource.call(
                Endpoints.addEpisodes,
                AddEpisodesApiRequestBody(eps)
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

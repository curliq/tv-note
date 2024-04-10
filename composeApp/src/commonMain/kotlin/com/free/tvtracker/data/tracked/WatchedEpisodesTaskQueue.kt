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
        pendingOrders.forEach {
            trySync(it)
        }
    }

    suspend fun add(order: MarkEpisodeWatchedOrderClientEntity) {
        watchedEpisodeOrders.value = watchedEpisodeOrders.value.plus(order)
        localDataSource.saveEpisodeWatchedOrder(order)
        trySync(order)
    }

    private suspend fun trySync(order: MarkEpisodeWatchedOrderClientEntity) {
        try {
            remoteDataSource.call(
                Endpoints.addEpisodes,
                AddEpisodesRequest(order.showId.toInt(), listOf(order.episodeId))
            )
                .asSuccess { data ->
                    localDataSource.saveWatchedEpisodes(order.showId, data.map { it.toClientEntity() })
                    localDataSource.deleteEpisodeWatchedOrder(order.id)
                }
                .asError {

                }
        } catch (e: Throwable) {

        }
    }

}

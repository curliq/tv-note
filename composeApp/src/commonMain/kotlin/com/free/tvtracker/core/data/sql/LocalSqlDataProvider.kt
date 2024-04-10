package com.free.tvtracker.core.data.sql

import com.free.tvtracker.data.tracked.entities.MarkEpisodeWatchedOrderClientEntity
import com.free.tvtracker.data.tracked.entities.StoredEpisodeClientEntity
import com.free.tvtracker.data.tracked.entities.TrackedShowClientEntity
import com.free.tvtracker.data.tracked.entities.WatchedEpisodeClientEntity
import com.free.tvtracker.shared.db.AppDatabase

class LocalSqlDataProvider(appDatabase: AppDatabase) {
    private val dbQuery = appDatabase.appDatabaseQueries

    fun saveTrackedShow(shows: List<TrackedShowClientEntity>) {
        dbQuery.transaction {
            shows.forEach { show ->
                dbQuery.saveStoredShow(
                    show.storedShow.tmdbId,
                    show.storedShow.title,
                    show.storedShow.posterImage,
                    show.storedShow.status
                )
                dbQuery.saveTrackedShow(show.id, show.createdAtDatetime, show.storedShow.tmdbId, show.watchlisted)
                show.storedShow.storedEpisodes.forEach { episode ->
                    dbQuery.saveStoredEpisodes(episode.id, episode.season, episode.episode, show.storedShow.tmdbId)
                }
                show.watchedEpisodes.forEach { episode ->
                    dbQuery.saveWatchedEpisodes(episode.id, episode.storedEpisodeId, show.id)
                }
            }
        }
    }

    fun saveWatchedEpisodes(showId: Long, episodes: List<WatchedEpisodeClientEntity>) {
        dbQuery.transaction {
            episodes.forEach { episode ->
                dbQuery.saveWatchedEpisodes(episode.id, episode.storedEpisodeId, showId)
            }
        }
    }

    fun getTrackedShows(): List<TrackedShowClientEntity> {
        return dbQuery.transactionWithResult {
            val shows = dbQuery.selectAllTrackedShows(TrackedShowClientEntity::fromSql).executeAsList()
            shows.forEach { show ->
                val storedEpisodes =
                    dbQuery.selectAllStoredEpisodes(show.storedShow.tmdbId, StoredEpisodeClientEntity::fromSql)
                        .executeAsList()
                val watchedEpisodes =
                    dbQuery.selectAllWatchedEpisodes(show.id, WatchedEpisodeClientEntity::fromSql).executeAsList()
                show.storedShow.storedEpisodes = storedEpisodes
                show.watchedEpisodes = watchedEpisodes
            }
            shows
        }
    }

    fun getEpisodeWatchedOrder(): List<MarkEpisodeWatchedOrderClientEntity> {
        return dbQuery.getWatchedEpisodeOrder(MarkEpisodeWatchedOrderClientEntity::fromSql).executeAsList()
    }

    fun saveEpisodeWatchedOrder(order: MarkEpisodeWatchedOrderClientEntity) {
        dbQuery.saveWatchedEpisodeOrder(order.id, order.showId, order.episodeId)
    }

    fun deleteEpisodeWatchedOrder(orderId: String) {
        dbQuery.deleteWatchedEpisodeOrder(orderId)
    }
}

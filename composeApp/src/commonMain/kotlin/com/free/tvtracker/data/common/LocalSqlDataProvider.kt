package com.free.tvtracker.data.common

import com.free.tvtracker.data.session.LocalPreferencesClientEntity
import com.free.tvtracker.data.session.SessionClientEntity
import com.free.tvtracker.data.tracked.entities.MarkEpisodeWatchedOrderClientEntity
import com.free.tvtracker.data.tracked.entities.StoredEpisodeClientEntity
import com.free.tvtracker.data.tracked.entities.TrackedShowClientEntity
import com.free.tvtracker.data.tracked.entities.WatchedEpisodeClientEntity
import com.free.tvtracker.shared.db.AppDatabase
import com.squareup.sqldelight.runtime.coroutines.asFlow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class LocalSqlDataProvider(appDatabase: AppDatabase) {
    private val dbQuery = appDatabase.appDatabaseQueries

    fun saveTrackedShows(shows: List<TrackedShowClientEntity>) {
        dbQuery.transaction {
            shows.forEach { show ->
                dbQuery.saveStoredShow(
                    show.storedShow.tmdbId,
                    show.storedShow.title,
                    show.storedShow.posterImage,
                    show.storedShow.backdropImage,
                    show.storedShow.status
                )
                dbQuery.saveTrackedShow(show.id, show.createdAtDatetime, show.storedShow.tmdbId, show.watchlisted)
                show.storedShow.storedEpisodes.forEach { episode ->
                    dbQuery.saveStoredEpisodes(
                        episode.id,
                        episode.season,
                        episode.episode,
                        show.storedShow.tmdbId,
                        episode.airDate
                    )
                }
                show.watchedEpisodes.forEach { episode ->
                    dbQuery.saveWatchedEpisodes(episode.id, episode.storedEpisodeId, show.id)
                }
            }
        }
    }

    fun deleteTrackedShow(id: Int) {
        dbQuery.deleteTrackedShow(id.toLong())
    }

    fun saveWatchedEpisodes(episodes: List<WatchedEpisodeClientEntity>) {
        dbQuery.transaction {
            episodes.forEach { episode ->
                dbQuery.saveWatchedEpisodes(episode.id, episode.storedEpisodeId, episode.trackedShowId)
            }
        }
    }

    fun getTrackedShows(): List<TrackedShowClientEntity> {
        return dbQuery.transactionWithResult {
            val shows = dbQuery.selectAllTrackedShows(TrackedShowClientEntity.Companion::fromSql).executeAsList()
            shows.forEach { show ->
                val storedEpisodes =
                    dbQuery.selectAllStoredEpisodes(show.storedShow.tmdbId, StoredEpisodeClientEntity.Companion::fromSql)
                        .executeAsList()
                val watchedEpisodes =
                    dbQuery.selectAllWatchedEpisodes(show.id, WatchedEpisodeClientEntity.Companion::fromSql).executeAsList()
                show.storedShow.storedEpisodes = storedEpisodes
                show.watchedEpisodes = watchedEpisodes
            }
            shows
        }
    }

    fun getEpisodeWatchedOrder(): List<MarkEpisodeWatchedOrderClientEntity> {
        return dbQuery.getWatchedEpisodeOrder(MarkEpisodeWatchedOrderClientEntity.Companion::fromSql).executeAsList()
    }

    fun saveEpisodeWatchedOrder(orders: List<MarkEpisodeWatchedOrderClientEntity>) {
        dbQuery.transaction {
            orders.forEach { order ->
                dbQuery.saveWatchedEpisodeOrder(order.id, order.showId, order.episodeId)
            }
        }
    }

    fun deleteEpisodeWatchedOrder(orderIds: List<String>) {
        dbQuery.transaction {
            orderIds.forEach { orderId ->
                dbQuery.deleteWatchedEpisodeOrder(orderId)
            }
        }
    }

    fun getLocalPreferences(): LocalPreferencesClientEntity {
        return dbQuery.getLocalPreferences(LocalPreferencesClientEntity.Companion::fromSql).executeAsOneOrNull()
            ?: LocalPreferencesClientEntity(
                welcomeComplete = false,
                theme = LocalPreferencesClientEntity.Theme.SystemDefault,
                purchasedApp = false,
                isHacked = false
            )
    }

    fun setLocalPreferences(prefs: LocalPreferencesClientEntity) {
        dbQuery.saveLocalPreferences(
            local_prefs_id = 1,
            welcome_complete = prefs.welcomeComplete,
            theme = prefs.theme.value.toLong(),
            purchased_app = prefs.purchasedApp,
            is_hacked = prefs.isHacked
        )
    }

    fun getSession(): SessionClientEntity? {
        val res = dbQuery.getSession(SessionClientEntity.Companion::fromSql).executeAsOneOrNull()
        return res
    }

    fun getSessionFlow(): Flow<SessionClientEntity?> {
        val res = dbQuery.getSession(SessionClientEntity.Companion::fromSql).asFlow()
        return res.map { it.executeAsOneOrNull() }
    }

    fun saveSession(session: SessionClientEntity) {
        dbQuery.saveSession(
            local_session_id = 1,
            session.userId,
            session.username,
            session.token,
            session.createdAtDatetime,
            session.email,
            session.preferencesPushAllowed,
            session.isAnonymous
        )
    }

    fun deleteEpisodeWatchedOrderForTvShow(trackedTvShowId: Int) {
        dbQuery.deleteWatchedEpisodeOrderForTvShow(trackedTvShowId.toLong())
    }
}

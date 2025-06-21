package com.free.tvtracker.features.tracked.data.shows

import com.free.tvtracker.storage.shows.data.StoredEpisodeEntity
import com.free.tvtracker.storage.shows.data.StoredShowEntity
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Repository
import java.sql.Timestamp

@Repository
class TrackedShowJdbcRepository {

    @Autowired
    lateinit var jdbcTemplate: JdbcTemplate

    fun findByUserIdAndWatchlistedWithAllRelations(
        userId: Int,
        watchlisted: Boolean
    ): List<TrackedShowEntity> {
        val query = jdbcTemplate.queryForList(
            """
            SELECT
                ts.id as "ts.id",
                ts.created_at_datetime as "ts.created_at_datetime",
                ts.user_id as "ts.user_id",
                ts.watchlisted as "ts.watchlisted",

                ss.id as "ss.id",
                ss.title as "ss.title",
                ss.tmdb_id as "ss.tmdb_id",
                ss.status as "ss.status",
                ss.poster_image as "ss.poster_image",
                ss.backdrop_image as "ss.backdrop_image",

                se.id as "se.id",
                se.season_number as "se.season_number",
                se.episode_number as "se.episode_number",
                se.air_date as "se.air_date",
                se.episode_name as "se.episode_name",
                se.thumbnail as "se.thumbnail",

                te.id as "te.id",
                te.created_at_datetime as "te.created_at_datetime",
                te.storedepisode_id as "te.storedepisode_id"
            FROM tracked_shows ts
            INNER JOIN stored_shows ss ON ts.storedshow_id = ss.id
            LEFT JOIN stored_episodes se ON se.storedshow_id = ss.id
            LEFT JOIN tracked_episodes te ON te.trackedshow_id = ts.id
            WHERE ts.user_id = $userId
            AND ts.watchlisted = $watchlisted
            """.trimIndent()
        )
        // Group results by tracked show ID to handle multiple episodes per show
        val groupedResults = query.groupBy { it["ts.id"] as Int }

        return groupedResults.map { (trackedShowId, rows) ->
            // Use the first row for tracked show and stored show data
            val firstRow = rows.first()

            // Extract all stored episodes for this show
            val storedEpisodes = rows.mapNotNull { row ->
                val seId = row["se.id"] as? Int ?: return@mapNotNull null
                StoredEpisodeEntity(
                    id = seId,
                    seasonNumber = row["se.season_number"] as? Int ?: 0,
                    episodeNumber = row["se.episode_number"] as? Int ?: 0,
                    airDate = row["se.air_date"] as? String,
                    episodeName = row["se.episode_name"] as? String,
                    thumbnail = row["se.thumbnail"] as? String
                )
            }.distinctBy { episode: StoredEpisodeEntity -> episode.id }

            // Extract all watched episodes for this show
            val watchedEpisodes = rows.mapNotNull { row ->
                val teId = row["te.id"] as? String ?: return@mapNotNull null
                TrackedShowEpisodeEntity(
                    id = teId,
                    createdAtDatetime = row["te.created_at_datetime"] as? String ?: "",
                    storedEpisodeId = row["te.storedepisode_id"] as? Int ?: 0,
                    trackedTvShowId = trackedShowId
                )
            }.distinctBy { episode: TrackedShowEpisodeEntity -> episode.id }

            TrackedShowEntity(
                id = trackedShowId,
                createdAtDatetime = (firstRow["ts.created_at_datetime"] as Timestamp).toString(),
                userId = firstRow["ts.user_id"] as Int,
                watchlisted = firstRow["ts.watchlisted"] as Boolean,
                storedShow = StoredShowEntity(
                    id = firstRow["ss.id"] as Int,
                    title = firstRow["ss.title"] as String,
                    tmdbId = firstRow["ss.tmdb_id"] as Int,
                    status = firstRow["ss.status"] as String? ?: "",
                    posterImage = firstRow["ss.poster_image"] as String? ?: "",
                    backdropImage = firstRow["ss.backdrop_image"] as String? ?: "",
                    storedEpisodes = storedEpisodes.toSet()
                ),
                watchedEpisodes = watchedEpisodes.toSet()
            )
        }
    }

    fun getEpisodesReleasedToday(): List<EpisodesReleaseTodayQueryResult> {
        val query = jdbcTemplate.queryForList(
            """
            select stored_shows.title,
                   stored_shows.id as show_id,
                   users.fcm_token
            from stored_episodes
                     JOIN stored_shows on stored_shows.id = storedshow_id
                     JOIN tracked_shows on tracked_shows.storedshow_id = stored_shows.id
                     JOIN users on users.id = tracked_shows.user_id
            WHERE air_date::date = current_date
              AND tracked_shows.watchlisted = false
              AND users.fcm_token IS NOT NULL
              AND users.preferences_push_allowed = true
            ;
        """.trimIndent()
        )
        return query.map {
            EpisodesReleaseTodayQueryResult(
                showTitle = it["title"] as String,
                showId = it["show_id"] as Int,
                userFcmToken = it["fcm_token"] as String,
            )
        }
    }

    /**
     * Change the user_id FK of the tracked shows that belong to [fromUserId]
     * If [toUserId] already has a tracked show with the same stored_show_id, then it's ignored,
     * meaning the tracked show on the [fromUserId] is lost, as intended
     */
    fun batchChangeUser(fromUserId: Int, toUserId: Int) {
        jdbcTemplate.update(
            """
            update tracked_shows t
            set user_id = $toUserId
            from (select id from tracked_shows where user_id = $fromUserId) as data_table
            where t.id = data_table.id
              and not exists(SELECT 1 FROM tracked_shows WHERE storedshow_id = t.storedshow_id AND user_id = $toUserId)
            ;
        """.trimIndent()
        )
    }
}

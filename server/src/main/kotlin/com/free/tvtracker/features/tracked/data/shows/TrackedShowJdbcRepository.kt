package com.free.tvtracker.features.tracked.data.shows

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Repository

@Repository
class TrackedShowJdbcRepository {

    @Autowired
    lateinit var jdbcTemplate: JdbcTemplate

    fun getEpisodesReleasedToday(): List<EpisodesReelaseTodayQueryResult> {
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
            EpisodesReelaseTodayQueryResult(
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

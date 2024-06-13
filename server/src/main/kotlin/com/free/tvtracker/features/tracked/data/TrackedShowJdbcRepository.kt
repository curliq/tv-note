package com.free.tvtracker.features.tracked.data

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Repository

@Repository
class TrackedShowJdbcRepository {

    @Autowired
    lateinit var jdbcTemplate: JdbcTemplate

    fun getEpisodesReleasedToday(): List<TodayReleasesQueryResult> {
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
            TodayReleasesQueryResult(
                showTitle = it["title"] as String,
                showId = it["show_id"] as Int,
                userFcmToken = it["fcm_token"] as String,
            )
        }
    }
}

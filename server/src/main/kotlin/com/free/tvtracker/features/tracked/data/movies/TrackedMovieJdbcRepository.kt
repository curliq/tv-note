package com.free.tvtracker.features.tracked.data.movies

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Repository

@Repository
class TrackedMovieJdbcRepository {

    @Autowired
    lateinit var jdbcTemplate: JdbcTemplate

    /**
     * Change the user_id FK of the tracked shows that belong to [fromUserId]
     * If [toUserId] already has a tracked show with the same stored_show_id, then it's ignored,
     * meaning the tracked show on the [fromUserId] is lost, as intended
     */
    fun batchChangeUser(fromUserId: Int, toUserId: Int) {
        jdbcTemplate.update(
            """
            update tracked_movies t
            set user_id = $toUserId
            from (select id from tracked_movies where user_id = $fromUserId) as data_table
            where t.id = data_table.id
              and not exists(SELECT 1 FROM tracked_movies WHERE storedmovie_id = t.storedmovie_id AND user_id = $toUserId)
            ;
        """.trimIndent()
        )
    }
}

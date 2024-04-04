package com.free.tvtracker.tracked.data

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Repository

@Repository
class TrackedShowJdbcRepository {

    @Autowired
    lateinit var jdbcTemplate: JdbcTemplate

    fun save(show: TrackedShowEntity) {
        jdbcTemplate.update(
            "insert into tracked_shows (created_at_datetime, storedshow_id, user_id, watchlisted) values (localtimestamp, ?, ?, ?)",
            show.storedShow.id,
            show.userId,
            show.watchlisted
        )
    }
}

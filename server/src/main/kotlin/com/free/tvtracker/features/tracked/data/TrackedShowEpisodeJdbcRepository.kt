package com.free.tvtracker.features.tracked.data

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Repository
import java.sql.PreparedStatement

@Repository
class TrackedShowEpisodeJdbcRepository {
    @Autowired
    var jdbcTemplate: JdbcTemplate? = null

    fun saveBatch(episodes: List<TrackedShowEpisodeEntity>) {
        val sql = "insert into tracked_episodes (storedepisode_id, trackedshow_id) values(?, ?)"

        jdbcTemplate!!.batchUpdate(
            sql,
            episodes,
            episodes.size
        ) { ps: PreparedStatement, episode: TrackedShowEpisodeEntity ->
            ps.setInt(1, episode.storedEpisodeId)
            ps.setInt(2, episode.trackedTvShow.id)
        }
    }
}

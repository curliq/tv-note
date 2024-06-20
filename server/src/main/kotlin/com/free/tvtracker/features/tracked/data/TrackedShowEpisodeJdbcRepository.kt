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
        val sql = "insert into tracked_episodes (id, storedepisode_id, trackedshow_id) values(?, ?, ?)"

        jdbcTemplate!!.batchUpdate(
            sql,
            episodes,
            episodes.size
        ) { ps: PreparedStatement, episode: TrackedShowEpisodeEntity ->
            ps.setString(1, "${episode.trackedTvShowId}_${episode.id}")
            ps.setInt(2, episode.storedEpisodeId)
            ps.setInt(3, episode.trackedTvShow.id)
        }
    }
}

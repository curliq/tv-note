package com.free.tvtracker.features.notifications;

import com.free.tvtracker.features.tracked.data.shows.TrackedShowJdbcRepository
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component


@Component
class NotificationTask(
    private val trackedShowJdbcRepository: TrackedShowJdbcRepository,
    private val fcmService: FcmService
) {
    @Scheduled(initialDelay = 0)
    fun setupFirebase() {
        fcmService.init()
    }

    //    @Scheduled(cron = "0 0 18 * * *")
    @Scheduled(initialDelay = 2000)
    fun notifications() {
        trackedShowJdbcRepository.getEpisodesReleasedToday()
            .groupBy { it.showId }
            .forEach {
                val showTitle = it.value.first().showTitle
                val title = if (it.value.size == 1) {
                    "New $showTitle episode"
                } else {
                    "New $showTitle episodes"
                }
                val body = if (it.value.size == 1) {
                    "Watch the new episode now!"
                } else {
                    "Watch ${it.value.count()} new episodes now!"
                }
                fcmService.sendPush(
                    title = title,
                    body = body,
                    fcmTokens = it.value.map { it.userFcmToken }.distinct()
                )
            }
    }
}

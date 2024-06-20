package com.free.tvtracker.ui.watching

import com.free.tvtracker.data.tracked.ShowsData
import com.free.tvtracker.data.tracked.TrackedShowsRepository
import com.free.tvtracker.domain.GetShowsUseCase
import com.free.tvtracker.domain.IsTrackedShowWatchableUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetWatchingShowsUseCase(
    private val getShowsUseCase: GetShowsUseCase,
    private val trackedShowsRepository: TrackedShowsRepository,
    private val isTrackedShowWatchableUseCase: IsTrackedShowWatchableUseCase,
) {
    operator fun invoke(): Flow<ShowsData> = getShowsUseCase.invoke(trackedShowsRepository.watchingShows).map {
        it.copy(data = isTrackedShowWatchableUseCase.watchable(it.data).filterNot { it.watchlisted })
    }
}

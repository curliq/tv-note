package com.free.tvtracker.core.di

import com.free.tvtracker.domain.GetNextUnwatchedEpisodeUseCase
import com.free.tvtracker.domain.IsTrackedShowWatchableUseCase
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.time.Clock

@Configuration
class Beans {
    @Bean
    fun getClock(): Clock = Clock.systemDefaultZone()

    @Bean
    fun getGetNextUnwatchedEpisodeUseCase(): GetNextUnwatchedEpisodeUseCase = GetNextUnwatchedEpisodeUseCase()

    @Bean
    fun getIsTrackedShowWatchableUseCase(): IsTrackedShowWatchableUseCase =
        IsTrackedShowWatchableUseCase(getGetNextUnwatchedEpisodeUseCase())
}

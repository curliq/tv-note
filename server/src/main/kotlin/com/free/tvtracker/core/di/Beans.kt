package com.free.tvtracker.core.di

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.time.Clock

@Configuration
class Beans {

    @Bean
    fun getClock(): Clock = Clock.systemDefaultZone()
}

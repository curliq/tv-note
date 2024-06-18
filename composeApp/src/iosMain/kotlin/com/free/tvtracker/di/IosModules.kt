package com.free.tvtracker.di

import com.free.tvtracker.data.session.SessionRepository
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class IosModules : KoinComponent {
    val sessionRepository: SessionRepository by inject()
}

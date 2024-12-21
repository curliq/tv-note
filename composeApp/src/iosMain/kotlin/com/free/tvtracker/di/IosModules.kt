package com.free.tvtracker.di

import com.free.tvtracker.data.iap.IapRepository
import com.free.tvtracker.data.session.SessionRepository
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class IosModules : KoinComponent {
    // used for fcm token
    val sessionRepository: SessionRepository by inject()
    // used to check purchase is active
    val iapRepository: IapRepository by inject()
}

package com.free.tvtracker.di

import com.free.tvtracker.data.user.UserRepository
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class IosModules : KoinComponent {
    val userRepository: UserRepository by inject()
}

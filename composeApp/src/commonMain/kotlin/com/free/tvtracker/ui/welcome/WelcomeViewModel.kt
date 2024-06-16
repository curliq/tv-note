package com.free.tvtracker.ui.welcome

import com.free.tvtracker.data.common.sql.LocalSqlDataProvider
import com.free.tvtracker.data.session.SessionRepository
import com.free.tvtracker.expect.ui.ViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class WelcomeViewModel(
    private val localDataSource: LocalSqlDataProvider,
    private val sessionRepository: SessionRepository,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO,
) : ViewModel() {

    val status = MutableStateFlow(Status.Initialising)

    init {
        viewModelScope.launch(ioDispatcher) {
            val sessionCreated = sessionRepository.createAnonSession()
            if (sessionCreated) {
                status.update {
                    if (it == Status.Loading) {
                        goHome()
                        Status.GoToHome
                    } else {
                        Status.GreenLight
                    }
                }
            } else {
                status.emit(Status.InitialisationError)
            }
        }
    }

    fun actionOk() {
        status.update {
            when (it) {
                Status.Initialising -> Status.Loading
                Status.Loading -> Status.Loading
                Status.GreenLight -> {
                    goHome()
                    Status.GoToHome
                }

                else -> it
            }
        }
    }

    private fun goHome() {
        localDataSource.setLocalPreferencesWelcomeComplete()
    }

    enum class Status {
        /**
         * Making background http call, but dont show loading until user taps it
         */
        Initialising,

        /**
         * Still making background calls, but user tapped it, so now we show loading
         */
        Loading,

        /**
         * Finished background calls, ready to be tapped
         */
        GreenLight,

        /**
         * Trigger navigation to go to the home screen
         */
        GoToHome,

        /**
         * Error making calls, probably no network
         */
        InitialisationError
    }
}

package com.free.tvtracker.ui.welcome

import com.free.tvtracker.data.common.sql.LocalSqlDataProvider
import com.free.tvtracker.data.iap.IapRepository
import com.free.tvtracker.data.session.SessionRepository
import com.free.tvtracker.expect.ViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class WelcomeViewModel(
    private val localDataSource: LocalSqlDataProvider,
    private val sessionRepository: SessionRepository,
    private val iapRepository: IapRepository,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO,
) : ViewModel() {

    val status = MutableStateFlow(Status.LoadingPrice)
    val price = MutableStateFlow("")

    init {
        refresh()
    }

    private suspend fun loadSession() {
        val sessionAlreadyExists = sessionRepository.loadSession()
        if (sessionAlreadyExists) {
            status.emit(Status.GreenLight)
            return
        }
        val sessionCreated = sessionRepository.createAnonSession()
        if (sessionCreated) {
            status.update {
                if (it == Status.Loading) {
                    updatePreferences()
                    Status.GoToHome
                } else {
                    Status.GreenLight
                }
            }
        } else {
            status.emit(Status.InitialisationError)
        }
    }

    fun refresh() {
        viewModelScope.launch(ioDispatcher) {
            val price = iapRepository.getPrice()
            if (price == null) {
                status.emit(Status.InitialisationError)
            } else {
                this@WelcomeViewModel.price.emit(price)
                status.emit(Status.CreatingSession)
                loadSession()
            }
        }
    }

    fun actionOk() {
        status.update { st ->
            when (st) {
                Status.LoadingPrice -> Status.Loading
                Status.CreatingSession -> Status.Loading
                Status.Loading -> Status.Loading
                Status.GreenLight -> {
                    updatePreferences()
                    Status.GoToHome
                }

                else -> st
            }
        }
    }

    fun buy() {
        viewModelScope.launch(ioDispatcher) {
            val res = iapRepository.purchase()
            if (res) {
                actionOk()
            }
        }
    }

    private fun updatePreferences() {
        val prefs = localDataSource.getLocalPreferences().copy(welcomeComplete = true)
        localDataSource.setLocalPreferences(prefs)
    }

    enum class Status {
        /**
         * Making app store call to check IAP price
         */
        LoadingPrice,

        /**
         * Making background http call, but dont show loading until user taps it
         */
        CreatingSession,

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

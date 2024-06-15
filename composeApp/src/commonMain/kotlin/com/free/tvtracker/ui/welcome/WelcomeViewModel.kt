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
            val anonSession = sessionRepository.createAnonymousSession()
            if (anonSession.isSuccess()) {
                status.emit(Status.GoToHome)
            } else {
                status.emit(Status.InitialisationError)
            }
        }
    }

    fun goToHome() {
        status.update {
            when (it) {
                Status.Initialising -> Status.Loading
                Status.Loading -> Status.Loading
                Status.GreenLight -> Status.GoToHome
                else -> it
            }
        }
    }

    enum class Status { Initialising, Loading, GreenLight, GoToHome, InitialisationError }
}

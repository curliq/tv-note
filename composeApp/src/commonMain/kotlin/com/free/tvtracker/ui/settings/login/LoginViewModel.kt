package com.free.tvtracker.ui.settings.login

import com.free.tvtracker.data.session.SessionRepository
import com.free.tvtracker.data.tracked.TrackedShowsRepository
import com.free.tvtracker.expect.ViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class LoginViewModel(
    private val sessionRepository: SessionRepository,
    private val trackedShowsRepository: TrackedShowsRepository,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : ViewModel() {
    val result = MutableStateFlow(Result.Idle)

    enum class Result { Loading, Error, Success, Idle }

    data class LoginAction(val username: String, val password: String)

    fun login(action: LoginAction) {
        result.value = Result.Loading
        viewModelScope.launch(ioDispatcher) {
            val response = sessionRepository.login(action.username, action.password)
            response.coAsSuccess {
                trackedShowsRepository.updateWatching(forceUpdate = true)
                trackedShowsRepository.updateFinished(forceUpdate = true)
                trackedShowsRepository.updateWatchlisted(forceUpdate = true)
                result.emit(Result.Success)
            }
            response.coAsError {
                result.emit(Result.Error)
            }
        }
    }
}

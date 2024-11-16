package com.free.tvtracker.ui.settings.signup

import com.free.tvtracker.data.session.SessionRepository
import com.free.tvtracker.expect.ui.ViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class SignupViewModel(
    private val sessionRepository: SessionRepository,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : ViewModel() {
    val result = MutableStateFlow(Result.Idle)

    enum class Result { Loading, Error, Success, Idle }

    data class SignupAction(val username: String, val email: String, val password: String)

    fun looksLikeEmail(email: String): Boolean {
        val expression = ".@.".toRegex()
        return expression.containsMatchIn(email)
    }

    fun signup(action: SignupAction) {
        result.value = Result.Loading
        viewModelScope.launch(ioDispatcher) {
            val response =
                sessionRepository.signup(username = action.username, email = action.email, password = action.password)
            response.coAsSuccess {
                result.emit(Result.Success)
            }
            response.coAsError {
                result.emit(Result.Error)
            }
        }
    }
}

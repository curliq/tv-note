package com.free.tvtracker.ui.settings.signup

import com.free.tvtracker.data.session.SessionRepository
import com.free.tvtracker.expect.ViewModel
import com.free.tvtracker.user.response.ErrorAccountAlreadyExists
import com.free.tvtracker.user.response.ErrorMissingCreds
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class SignupViewModel(
    private val sessionRepository: SessionRepository,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : ViewModel() {
    val result = MutableStateFlow<Result>(Result.Idle)

    sealed class Result {
        data object Loading : Result()
        data class Error(val message: String) : Result()
        data object Success : Result()
        data object Idle : Result()
    }

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
                when (it.code) {
                    ErrorMissingCreds.code -> {
                        result.emit(Result.Error("Missing username or password."))
                    }

                    ErrorAccountAlreadyExists.code -> {
                        result.emit(Result.Error("This username is already taken."))
                    }

                    else -> {
                        result.emit(Result.Error("Something went wrong, please try again."))
                    }
                }
            }
        }
    }
}

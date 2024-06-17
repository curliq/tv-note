package com.free.tvtracker.ui.settings

import com.free.tvtracker.data.session.SessionRepository
import com.free.tvtracker.expect.ui.ViewModel
import com.free.tvtracker.data.user.UserRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val userRepo: UserRepository,
    private val sessionRepository: SessionRepository,
    private val settingsUiModelMapper: SettingsUiModelMapper,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO,
) : ViewModel() {

    val data: MutableStateFlow<SettingsUiState> = MutableStateFlow(SettingsUiState.Loading)

    init {
        viewModelScope.launch(ioDispatcher) {
            val session = sessionRepository.getSession()
            if (session != null) {
                data.emit(SettingsUiState.Ok(settingsUiModelMapper.map(session)))
            } else {
                data.emit(SettingsUiState.Error)
            }
        }
    }

    fun action(action: Action) {
        viewModelScope.launch(ioDispatcher) {
            when (action) {
                is Action.TogglePushAllowed -> {
                    data.update {
                        if (it is SettingsUiState.Ok) it.copy(
                            data = it.data.copy(
                                pushNotificationEnabled = action.allowed
                            )
                        ) else it
                    }
                    userRepo.updatePushAllowed(allowed = action.allowed)
                    //todo update data
                }
            }
        }
    }

    sealed class Action {
        data class TogglePushAllowed(val allowed: Boolean) : Action()
    }
}

sealed class SettingsUiState {
    data object Loading : SettingsUiState()
    data object Error : SettingsUiState()
    data class Ok(val data: SettingsUiModel) : SettingsUiState()
}

data class SettingsUiModel(
    val isAnon: Boolean,
    val personalInfo: PersonalInfo?,
    val pushNotificationEnabled: Boolean,
) {
    data class PersonalInfo(val username: String, val email: String?)
}

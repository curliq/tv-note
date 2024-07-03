package com.free.tvtracker.ui.settings

import com.free.tvtracker.data.common.sql.LocalSqlDataProvider
import com.free.tvtracker.data.session.LocalPreferencesClientEntity
import com.free.tvtracker.data.session.SessionRepository
import com.free.tvtracker.expect.ui.ViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val sessionRepository: SessionRepository,
    private val localDataSource: LocalSqlDataProvider,
    private val settingsUiModelMapper: SettingsUiModelMapper,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO,
) : ViewModel() {

    val data: MutableStateFlow<SettingsUiState> = MutableStateFlow(SettingsUiState.Idle)
    val shareCsvFile: MutableStateFlow<String?> = MutableStateFlow(null)
    val theme: Flow<SettingsUiModel.Theme?> = data.map { (it as? SettingsUiState.Ok)?.data?.theme }
    val logout = MutableStateFlow(false)

    init {
        viewModelScope.launch(ioDispatcher) {
            val localPrefs = localDataSource.getLocalPreferences()
            sessionRepository.getSessionFlow().collect { session ->
                if (session != null) {
                    data.emit(SettingsUiState.Ok(settingsUiModelMapper.map(session, localPrefs)))
                } else {
                    data.emit(SettingsUiState.Error)
                }
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
                    sessionRepository.updatePushAllowed(allowed = action.allowed)
                }

                is Action.SetTheme -> {
                    data.update {
                        if (it is SettingsUiState.Ok) it.copy(
                            data = it.data.copy(
                                theme = action.theme,
                            )
                        ) else it
                    }
                    val theme: LocalPreferencesClientEntity.Theme = when (action.theme) {
                        SettingsUiModel.Theme.System -> LocalPreferencesClientEntity.Theme.SystemDefault
                        SettingsUiModel.Theme.Dark -> LocalPreferencesClientEntity.Theme.Dark
                        SettingsUiModel.Theme.Light -> LocalPreferencesClientEntity.Theme.Light
                    }
                    val prefs = localDataSource.getLocalPreferences().copy(theme = theme)
                    localDataSource.setLocalPreferences(prefs)
                }

                Action.Logout -> {
                    logout.emit(true)
                }

                Action.Export -> {
                    sessionRepository.exportData()
                        .coAsSuccess { shareCsvFile.emit(it) }
                }
            }
        }
    }

    sealed class Action {
        data class TogglePushAllowed(val allowed: Boolean) : Action()
        data class SetTheme(val theme: SettingsUiModel.Theme) : Action()
        data object Logout : Action()
        data object Export : Action()
    }
}

sealed class SettingsUiState {
    data object Idle : SettingsUiState()
    data object Error : SettingsUiState()
    data class Ok(val data: SettingsUiModel) : SettingsUiState()
}

data class SettingsUiModel(
    val isAnon: Boolean,
    val personalInfo: PersonalInfo?,
    val pushNotificationEnabled: Boolean,
    val theme: Theme,
) {
    data class PersonalInfo(val username: String, val email: String?)
    enum class Theme { System, Dark, Light }
}

package com.free.tvtracker.screens.details

import com.free.tvtracker.core.ui.ViewModel
import com.free.tvtracker.data.search.SearchRepository
import com.free.tvtracker.discover.response.TmdbShowDetailsApiModel
import com.free.tvtracker.utils.TmdbConfigData
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class DetailsViewModel(
    private val searchRepository: SearchRepository,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO,
) : ViewModel() {

    val result: MutableStateFlow<DetailsUiState> = MutableStateFlow(DetailsUiState.Loading)

    fun setId(showId: Int) {
        result.value = DetailsUiState.Loading
        viewModelScope.launch(ioDispatcher) {
            searchRepository.getShow(showId)
                .asSuccess { data ->
                    result.value = data.toUiModel()
                }.asError {
                    result.value = DetailsUiState.Error
                }
        }
    }
}

sealed class DetailsUiState {
    data object Loading : DetailsUiState()
    data object Error : DetailsUiState()
    data class Ok(val data: DetailsUiModel) : DetailsUiState()
}

data class DetailsUiModel(
    val id: Int,
    val name: String,
    val posterUrl: String,
    val status: String,
    val releaseYear: String?,
    val whereToWatch: List<WhereToWatch>,
    val description: String?,
) {
    data class WhereToWatch(val platformName: String, val platformLogo: String)
}

fun TmdbShowDetailsApiModel.toUiModel(): DetailsUiState.Ok {
    return DetailsUiState.Ok(
        DetailsUiModel(
            id = this.id,
            name = this.name,
            posterUrl = TmdbConfigData.get().getPosterUrl(this.posterPath),
            status = this.status,
            releaseYear = this.firstAirDate,
            whereToWatch = this.networks.map {
                DetailsUiModel.WhereToWatch(it.name, TmdbConfigData.get().getPosterUrl(it.logoPath))
            },
            description = this.overview
        )
    )
}

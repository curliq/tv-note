package com.free.tvtracker.ui.person

import com.free.tvtracker.expect.ui.ViewModel
import com.free.tvtracker.data.search.SearchRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class PersonViewModel(
    private val searchRepository: SearchRepository,
    private val uiModelMapper: PersonUiModelMapper,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO,
) : ViewModel() {
    val result: MutableStateFlow<PersonUiState> = MutableStateFlow(PersonUiState.Loading)

    fun setPersonId(tmdbPersonId: Int) {
        result.value = PersonUiState.Loading
        viewModelScope.launch(ioDispatcher) {
            searchRepository.getPerson(tmdbPersonId)
                .coAsSuccess { data ->
                    result.value = PersonUiState.Ok(uiModelMapper.map(data))
                }.asError {
                    result.value = PersonUiState.Error
                }
        }
    }
}

sealed class PersonUiState {
    data object Loading : PersonUiState()
    data object Error : PersonUiState()
    data class Ok(val data: PersonUiModel) : PersonUiState()
}

data class PersonUiModel(
    val photoUrl: String,
    val name: String,
    val job: String,
    val dob: String,
    val bornIn: String,
    val bio: String,
    val moviesCast: List<Credit>,
    val moviesCrew: List<Credit>,
    val firstTwoMovies: List<Credit>,
    val moviesCount: Int,
    val tvShowsCast: List<Credit>,
    val tvShowsCrew: List<Credit>,
    val tvShowsCount: Int,
    val firstTwoTvShows: List<Credit>,
    val photos: List<String>,
    val firstTwoPhotos: List<String>,
    val instagramUrl: String?,
    val instagramTag: String?
) {
    data class Credit(
        val tmdbId: Int,
        val posterUrl: String,
        val name: String,
        val voteCount: Int,
        val isTvShow: Boolean
    )
}

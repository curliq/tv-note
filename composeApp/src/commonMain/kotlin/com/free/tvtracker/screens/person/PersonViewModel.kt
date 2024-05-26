package com.free.tvtracker.screens.person

import com.free.tvtracker.core.ui.ViewModel
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
    val movies: List<Movie>,
    val firstTwoMovies: List<Movie>,
    val moviesCount: Int,
    val tvShowsCast: List<TvShow>,
    val tvShowsCrew: List<TvShow>,
    val tvShowsCount: Int,
    val firstTwoTvShows: List<TvShow>,
    val photos: List<String>,
    val firstTwoPhotos: List<String>,
    val instagramUrl: String?,
    val instagramTag: String?
) {
    sealed interface Credit {
        val tmdbId: Int
        val posterUrl: String
        val name: String
        val voteCount: Int
    }

    data class TvShow(
        override val posterUrl: String, override val name: String, override val tmdbId: Int,
        override val voteCount: Int
    ) : Credit

    data class Movie(
        override val posterUrl: String, override val name: String, override val tmdbId: Int,
        override val voteCount: Int
    ) : Credit
}

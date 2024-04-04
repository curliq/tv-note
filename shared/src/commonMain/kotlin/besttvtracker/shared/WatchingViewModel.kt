package besttvtracker.shared

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class WatchingViewModel() : ViewModel() {
    val shows = MutableStateFlow("loading")

    init {
        viewModelScope.launch(Dispatchers.IO) {

        }
    }
}

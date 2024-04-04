package besttvtracker.shared

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import androidx.lifecycle.viewModelScope as vms

actual abstract class ViewModel actual constructor(): ViewModel() {
    actual val viewModelScope: CoroutineScope
        get() = vms

    override fun onCleared() {
        super.onCleared()
        onClear()
    }

    protected actual open fun onClear() {
        super.onCleared()
    }
}

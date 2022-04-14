package ua.dimabatyuk.androidstatistic.presentation

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import ua.dimabatyuk.androidstatistic.data.AndroidIdProvider
import ua.dimabatyuk.androidstatistic.data.MemoryUsagePercentageProvider
import ua.dimabatyuk.androidstatistic.data.Provider

class MainViewModel(app: Application) : AndroidViewModel(app) {
    private val androidIdProvider: Provider<Long> by lazy { AndroidIdProvider(app) }
    private val memoryUsedPercentageProvider: Provider<Float> by lazy {
        MemoryUsagePercentageProvider(app)
    }

    private val _state = MutableStateFlow(UIState.EMPTY)
    val state: StateFlow<UIState> = _state

    init {
        viewModelScope.launch {
            while (isActive) {
                _state.update {
                    it.copy(
                        androidId = androidIdProvider.get(),
                        memoryUsagePercentage = memoryUsedPercentageProvider.get()
                    )
                }

                try {
                    delay(1_000L)
                } catch (e: Exception) {
                    //do nothing
                }
            }
        }
    }
}

data class UIState(
    val androidId: Long,
    val memoryUsagePercentage: Float,
) {
    companion object {
        val EMPTY = UIState(-1, 0F)
    }
}
package br.com.mobapps.fisiocontrol.presentation.schedule.list

import br.com.mobapps.fisiocontrol.domain.model.TreatmentSchedule
import br.com.mobapps.fisiocontrol.domain.usecase.schedule.GetSchedulesUseCase
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class ScheduleListUiState(
    val schedules: List<TreatmentSchedule> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

class ScheduleListScreenModel(
    private val playerId: String,
    private val getSchedules: GetSchedulesUseCase
) : ScreenModel {

    private val _uiState = MutableStateFlow(ScheduleListUiState())
    val uiState = _uiState.asStateFlow()

    init { load() }

    fun load() {
        screenModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            getSchedules(playerId)
                .onSuccess  { s -> _uiState.update { it.copy(schedules = s, isLoading = false) } }
                .onFailure  { e -> _uiState.update { it.copy(error = e.message, isLoading = false) } }
        }
    }
}

package br.com.mobapps.fisiocontrol.presentation.schedule.form

import br.com.mobapps.fisiocontrol.domain.model.ScheduleStatus
import br.com.mobapps.fisiocontrol.domain.model.TreatmentSchedule
import br.com.mobapps.fisiocontrol.domain.usecase.schedule.GetScheduleByIdUseCase
import br.com.mobapps.fisiocontrol.domain.usecase.schedule.SaveScheduleUseCase
import kotlinx.datetime.LocalDate
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class ScheduleFormUiState(
    val title: String = "",
    val weeklyPlanning: String = "",
    val startDate: String = "",
    val status: ScheduleStatus = ScheduleStatus.ACTIVE,
    val sessionsPerWeek: Int = 3,
    val weeklyAssessment: String = "",
    val isLoading: Boolean = false,
    val isSaved: Boolean = false,
    val error: String? = null
)

class ScheduleFormScreenModel(
    private val playerId: String,
    private val scheduleId: String,
    private val getScheduleById: GetScheduleByIdUseCase,
    private val saveSchedule: SaveScheduleUseCase
) : ScreenModel {

    private val _uiState = MutableStateFlow(ScheduleFormUiState())
    val uiState = _uiState.asStateFlow()

    val isEditing get() = scheduleId.isNotEmpty()

    init {
        if (isEditing) screenModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            getScheduleById(scheduleId)
                .onSuccess { schedule -> populate(schedule); _uiState.update { it.copy(isLoading = false) } }
                .onFailure { e -> _uiState.update { it.copy(isLoading = false, error = e.message) } }
        }
    }

    private fun populate(schedule: TreatmentSchedule) {
        _uiState.update {
            it.copy(
                title            = schedule.title,
                weeklyPlanning   = schedule.weeklyPlanning ?: "",
                startDate        = "%02d%02d%04d".format(
                    schedule.startDate.dayOfMonth,
                    schedule.startDate.monthNumber,
                    schedule.startDate.year
                ),
                status           = schedule.status,
                sessionsPerWeek  = schedule.sessionsPerWeek,
                weeklyAssessment = schedule.weeklyAssessment ?: ""
            )
        }
    }

    fun onTitleChange(v: String)           = _uiState.update { it.copy(title = v) }
    fun onWeeklyPlanningChange(v: String)  = _uiState.update { it.copy(weeklyPlanning = v) }
    fun onStartDateChange(v: String)       = _uiState.update { it.copy(startDate = v.filter(Char::isDigit).take(8)) }
    fun onStatusChange(v: ScheduleStatus)  = _uiState.update { it.copy(status = v) }
    fun onSessionsChange(v: Int)           = _uiState.update { it.copy(sessionsPerWeek = v.coerceIn(1, 7)) }
    fun onWeeklyAssessmentChange(v: String) = _uiState.update { it.copy(weeklyAssessment = v) }

    fun onSave() {
        val s = _uiState.value
        if (s.title.isBlank()) {
            _uiState.update { it.copy(error = "Título e data de início são obrigatórios") }
            return
        }
        val parsedDate = s.startDate.takeIf { it.length == 8 }?.let {
            runCatching {
                LocalDate(
                    year        = it.substring(4, 8).toInt(),
                    monthNumber = it.substring(2, 4).toInt(),
                    dayOfMonth  = it.substring(0, 2).toInt()
                )
            }.getOrNull()
        }
        if (parsedDate == null) {
            _uiState.update { it.copy(error = "Data de início inválida") }
            return
        }
        screenModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            val schedule = TreatmentSchedule(
                id               = scheduleId,
                playerId         = playerId,
                title            = s.title.trim(),
                weeklyPlanning   = s.weeklyPlanning.ifBlank { null },
                startDate        = parsedDate,
                status           = s.status,
                weeklyAssessment = s.weeklyAssessment.ifBlank { null },
                sessionsPerWeek  = s.sessionsPerWeek,
                updatedAt        = ""
            )
            saveSchedule(schedule)
                .onSuccess { _uiState.update { it.copy(isLoading = false, isSaved = true) } }
                .onFailure { e -> _uiState.update { it.copy(isLoading = false, error = e.message) } }
        }
    }
}

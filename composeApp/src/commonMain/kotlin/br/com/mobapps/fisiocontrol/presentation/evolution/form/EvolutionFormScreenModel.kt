package br.com.mobapps.fisiocontrol.presentation.evolution.form

import br.com.mobapps.fisiocontrol.domain.model.DailyEvolution
import br.com.mobapps.fisiocontrol.domain.usecase.evolution.RecordEvolutionUseCase
import kotlinx.datetime.LocalDate
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class EvolutionFormUiState(
    val sessionDate: String = "",
    val painScale: Int = 0,
    val objectiveNote: String = "",
    val physiotherapyProcedures: String = "",
    val isLoading: Boolean = false,
    val isSaved: Boolean = false,
    val error: String? = null
)

class EvolutionFormScreenModel(
    private val playerId: String,
    private val recordEvolution: RecordEvolutionUseCase
) : ScreenModel {

    private val _uiState = MutableStateFlow(EvolutionFormUiState())
    val uiState = _uiState.asStateFlow()

    fun onDateChange(v: String)             = _uiState.update { it.copy(sessionDate = v.filter(Char::isDigit).take(8)) }
    fun onPainChange(v: Int)                = _uiState.update { it.copy(painScale = v) }
    fun onPhysiotherapyProceduresChange(v: String)   = _uiState.update { it.copy(physiotherapyProcedures = v) }
    fun onObjectiveNoteChange(v: String)    = _uiState.update { it.copy(objectiveNote = v) }

    fun onSave() {
        val s = _uiState.value
        if (s.sessionDate.length < 8) {
            _uiState.update { it.copy(error = "Data da sessão inválida") }
            return
        }
        screenModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            val evolution = DailyEvolution(
                id              = "",
                playerId        = playerId,
                scheduleId      = null,
                sessionDate     = s.sessionDate.let {
                    LocalDate(
                        year        = it.substring(4, 8).toInt(),
                        monthNumber = it.substring(2, 4).toInt(),
                        dayOfMonth  = it.substring(0, 2).toInt()
                    )
                },
                painScale       = s.painScale,
                physiotherapyProcedures  = s.physiotherapyProcedures.ifBlank { null },
                objectiveNote   = s.objectiveNote.ifBlank { null },
                createdAt       = ""
            )
            recordEvolution(evolution)
                .onSuccess { _uiState.update { it.copy(isLoading = false, isSaved = true) } }
                .onFailure { e -> _uiState.update { it.copy(isLoading = false, error = e.message) } }
        }
    }
}

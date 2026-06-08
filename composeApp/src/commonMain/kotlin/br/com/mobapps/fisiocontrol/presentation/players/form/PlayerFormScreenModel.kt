package br.com.mobapps.fisiocontrol.presentation.players.form

import br.com.mobapps.fisiocontrol.domain.model.Player
import br.com.mobapps.fisiocontrol.domain.usecase.player.GetPlayerDetailUseCase
import br.com.mobapps.fisiocontrol.domain.usecase.player.SavePlayerUseCase
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate

data class PlayerFormUiState(
    val fullName: String = "",
    val birthDate: String = "",
    val position: String = "",
    val team: String = "",
    val phone: String = "",
    val notes: String = "",
    val isLoading: Boolean = false,
    val isSaved: Boolean = false,
    val error: String? = null
)

class PlayerFormScreenModel(
    private val playerId: String,
    private val getPlayer: GetPlayerDetailUseCase,
    private val savePlayer: SavePlayerUseCase
) : ScreenModel {

    private val _uiState = MutableStateFlow(PlayerFormUiState())
    val uiState = _uiState.asStateFlow()

    val isEditing get() = playerId.isNotEmpty()

    init {
        if (isEditing) screenModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            getPlayer(playerId)
                .onSuccess { player -> populate(player); _uiState.update { it.copy(isLoading = false) } }
                .onFailure { e -> _uiState.update { it.copy(isLoading = false, error = e.message) } }
        }
    }

    fun onFullNameChange(v: String)  = _uiState.update { it.copy(fullName = v) }
    fun onBirthDateChange(v: String) =
        _uiState.update { it.copy(birthDate = v.filter(Char::isDigit).take(8)) }
    fun onPositionChange(v: String)  = _uiState.update { it.copy(position = v) }
    fun onTeamChange(v: String)      = _uiState.update { it.copy(team = v) }
    fun onPhoneChange(v: String)     = _uiState.update { it.copy(phone = v) }
    fun onNotesChange(v: String)     = _uiState.update { it.copy(notes = v) }

    fun populate(player: Player) {
        _uiState.update {
            it.copy(
                fullName  = player.fullName,
                birthDate = player.birthDate?.let {
                    "%02d%02d%04d".format(it.dayOfMonth, it.monthNumber, it.year)
                } ?: "",
                position  = player.position ?: "",
                team      = player.team ?: "",
                phone     = player.phone ?: "",
                notes     = player.notes ?: ""
            )
        }
    }

    fun onSave() {
        val s = _uiState.value
        if (s.fullName.isBlank()) {
            _uiState.update { it.copy(error = "Nome é obrigatório") }
            return
        }
        screenModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            val parsedBirthDate: LocalDate? = s.birthDate.takeIf { it.length == 8 }?.let {
                runCatching {
                    LocalDate(
                        year        = it.substring(4, 8).toInt(),
                        monthNumber = it.substring(2, 4).toInt(),
                        dayOfMonth  = it.substring(0, 2).toInt()
                    )
                }.getOrNull()
            }
            val player = Player(
                id        = playerId,
                fullName  = s.fullName.trim(),
                birthDate = parsedBirthDate,
                position  = s.position.ifBlank { null },
                team      = s.team.ifBlank { null },
                phone     = s.phone.ifBlank { null },
                photoUrl  = null,
                notes     = s.notes.ifBlank { null },
                isActive  = true,
                updatedAt = ""
            )
            savePlayer(player)
                .onSuccess { _uiState.update { it.copy(isLoading = false, isSaved = true) } }
                .onFailure { e -> _uiState.update { it.copy(isLoading = false, error = e.message) } }
        }
    }
}

package br.com.mobapps.fisiocontrol.presentation.players.detail

import br.com.mobapps.fisiocontrol.domain.model.Player
import br.com.mobapps.fisiocontrol.domain.usecase.player.GetPlayerDetailUseCase
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class PlayerDetailUiState(
    val player: Player? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)

class PlayerDetailScreenModel(
    private val playerId: String,
    private val getPlayerDetail: GetPlayerDetailUseCase
) : ScreenModel {

    private val _uiState = MutableStateFlow(PlayerDetailUiState())
    val uiState = _uiState.asStateFlow()

    init { load() }

    fun load() {
        screenModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            getPlayerDetail(playerId)
                .onSuccess  { p -> _uiState.update { it.copy(player = p, isLoading = false) } }
                .onFailure  { e -> _uiState.update { it.copy(error = e.message, isLoading = false) } }
        }
    }
}

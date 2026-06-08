package br.com.mobapps.fisiocontrol.presentation.players.list

import br.com.mobapps.fisiocontrol.domain.model.Player
import br.com.mobapps.fisiocontrol.domain.usecase.player.GetPlayersUseCase
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class PlayerListUiState(
    val players: List<Player> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val searchQuery: String = ""
) {
    val filtered get() = if (searchQuery.isBlank()) players
    else players.filter { it.fullName.contains(searchQuery, ignoreCase = true) }
}

class PlayerListScreenModel(
    private val getPlayers: GetPlayersUseCase
) : ScreenModel {

    private val _uiState = MutableStateFlow(PlayerListUiState())
    val uiState = _uiState.asStateFlow()

    init { load() }

    fun load() {
        screenModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            getPlayers()
                .onSuccess  { players -> _uiState.update { it.copy(players = players, isLoading = false) } }
                .onFailure  { e      -> _uiState.update { it.copy(error = e.message, isLoading = false) } }
        }
    }

    fun onSearch(q: String) = _uiState.update { it.copy(searchQuery = q) }
}

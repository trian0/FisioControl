package br.com.mobapps.fisiocontrol.presentation.evolution.list

import br.com.mobapps.fisiocontrol.domain.model.DailyEvolution
import br.com.mobapps.fisiocontrol.domain.usecase.evolution.GetEvolutionsUseCase
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class EvolutionListUiState(
    val evolutions: List<DailyEvolution> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

class EvolutionListScreenModel(
    private val playerId: String,
    private val getEvolutions: GetEvolutionsUseCase
) : ScreenModel {

    private val _uiState = MutableStateFlow(EvolutionListUiState())
    val uiState = _uiState.asStateFlow()

    init { load() }

    fun load() {
        screenModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            getEvolutions(playerId)
                .onSuccess  { e -> _uiState.update { it.copy(evolutions = e, isLoading = false) } }
                .onFailure  { e -> _uiState.update { it.copy(error = e.message, isLoading = false) } }
        }
    }
}

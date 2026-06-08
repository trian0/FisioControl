package br.com.mobapps.fisiocontrol.presentation.auth

import br.com.mobapps.fisiocontrol.domain.usecase.auth.LoginUseCase
import br.com.mobapps.fisiocontrol.domain.usecase.auth.LogoutUseCase
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class LoginUiState(
    val email: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val errorDialog: String? = null
)

class LoginScreenModel(
    private val login: LoginUseCase,
    private val logout: LogoutUseCase
) : ScreenModel {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState = _uiState.asStateFlow()

    fun onEmailChange(v: String)    = _uiState.update { it.copy(email = v, errorDialog = null) }
    fun onPasswordChange(v: String) = _uiState.update { it.copy(password = v, errorDialog = null) }
    fun onDismissError()            = _uiState.update { it.copy(errorDialog = null) }

    fun onLogin() {
        val state = _uiState.value
        if (state.email.isBlank() || state.password.isBlank()) {
            _uiState.update { it.copy(errorDialog = "Preencha e-mail e senha") }
            return
        }
        screenModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorDialog = null) }
            login(state.email, state.password)
                .onFailure { e ->
                    _uiState.update { it.copy(isLoading = false, errorDialog = e.message ?: "Erro ao fazer login") }
                }
                .onSuccess {
                    _uiState.update { it.copy(isLoading = false) }
                }
        }
    }
}

package br.com.mobapps.fisiocontrol.domain.usecase.auth

import br.com.mobapps.fisiocontrol.domain.repository.AuthRepository

class LoginUseCase(private val repo: AuthRepository) {
    suspend operator fun invoke(email: String, password: String): Result<Unit> =
        repo.login(email.trim(), password)
}

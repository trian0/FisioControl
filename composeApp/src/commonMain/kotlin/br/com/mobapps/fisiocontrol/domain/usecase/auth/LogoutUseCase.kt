package br.com.mobapps.fisiocontrol.domain.usecase.auth

import br.com.mobapps.fisiocontrol.domain.repository.AuthRepository

class LogoutUseCase(private val repo: AuthRepository) {
    suspend operator fun invoke(): Result<Unit> = repo.logout()
}

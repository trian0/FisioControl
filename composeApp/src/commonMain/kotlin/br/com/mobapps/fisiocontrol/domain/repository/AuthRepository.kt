package br.com.mobapps.fisiocontrol.domain.repository

interface AuthRepository {
    suspend fun login(email: String, password: String): Result<Unit>
    suspend fun logout(): Result<Unit>
    fun isAuthenticated(): Boolean
}

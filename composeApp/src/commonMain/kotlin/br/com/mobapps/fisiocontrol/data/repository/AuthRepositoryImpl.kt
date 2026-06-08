package br.com.mobapps.fisiocontrol.data.repository

import br.com.mobapps.fisiocontrol.domain.repository.AuthRepository
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.builtin.Email

class AuthRepositoryImpl(private val supabase: SupabaseClient) : AuthRepository {

    override suspend fun login(email: String, password: String): Result<Unit> = runCatching {
        supabase.auth.signInWith(Email) {
            this.email    = email
            this.password = password
        }
    }

    override suspend fun logout(): Result<Unit> = runCatching {
        supabase.auth.signOut()
    }

    override fun isAuthenticated(): Boolean =
        supabase.auth.currentSessionOrNull() != null
}

package br.com.mobapps.fisiocontrol

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import br.com.mobapps.fisiocontrol.di.databaseModule
import br.com.mobapps.fisiocontrol.di.networkModule
import br.com.mobapps.fisiocontrol.di.repositoryModule
import br.com.mobapps.fisiocontrol.di.screenModelModule
import br.com.mobapps.fisiocontrol.di.useCaseModule
import br.com.mobapps.fisiocontrol.presentation.auth.LoginScreen
import br.com.mobapps.fisiocontrol.presentation.players.list.PlayerListScreen
import br.com.mobapps.fisiocontrol.presentation.theme.FisioTheme
import cafe.adriel.voyager.navigator.Navigator
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.status.SessionStatus
import org.koin.compose.KoinApplication
import org.koin.compose.koinInject
import org.koin.core.module.Module

@Composable
fun App(platformModule: Module) {
    KoinApplication(application = {
        modules(platformModule, networkModule, databaseModule, repositoryModule, useCaseModule, screenModelModule)
    }) {
        AppContent()
    }
}

@Composable
private fun AppContent() {
    val supabase = koinInject<SupabaseClient>()
    val sessionStatus by supabase.auth.sessionStatus.collectAsState()

    FisioTheme {
        when (sessionStatus) {
            is SessionStatus.Initializing ->
                Box(Modifier.fillMaxSize()) {
                    CircularProgressIndicator(Modifier.align(Alignment.Center))
                }
            is SessionStatus.Authenticated ->
                Navigator(PlayerListScreen())
            else ->
                Navigator(LoginScreen())
        }
    }
}

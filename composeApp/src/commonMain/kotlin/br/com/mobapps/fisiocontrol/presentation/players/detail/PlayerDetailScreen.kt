package br.com.mobapps.fisiocontrol.presentation.players.detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import br.com.mobapps.fisiocontrol.domain.model.Player
import br.com.mobapps.fisiocontrol.presentation.components.ErrorMessage
import br.com.mobapps.fisiocontrol.presentation.components.FisioTopBar
import br.com.mobapps.fisiocontrol.presentation.components.LoadingOverlay
import br.com.mobapps.fisiocontrol.presentation.evolution.list.EvolutionListScreen
import br.com.mobapps.fisiocontrol.presentation.players.form.PlayerFormScreen
import br.com.mobapps.fisiocontrol.presentation.schedule.list.ScheduleListScreen
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import org.koin.core.parameter.parametersOf

data class PlayerDetailScreen(val playerId: String) : Screen {
    @Composable
    override fun Content() {
        val nav   = LocalNavigator.currentOrThrow
        val model = getScreenModel<PlayerDetailScreenModel> { parametersOf(playerId) }
        val state by model.uiState.collectAsState()

        LaunchedEffect(nav.lastItem) {
            if (nav.lastItem is PlayerDetailScreen) model.load()
        }

        Scaffold(
            topBar = {
                FisioTopBar(
                    title = state.player?.fullName ?: "Atleta",
                    onBack = { nav.pop() },
                    actions = {
                        if (state.player != null) {
                            IconButton(onClick = { nav.push(PlayerFormScreen(playerId)) }) {
                                Icon(Icons.Default.Edit, "Editar")
                            }
                        }
                    }
                )
            }
        ) { padding ->
            when {
                state.isLoading -> LoadingOverlay()
                state.error != null -> ErrorMessage(state.error!!, onRetry = model::load)
                state.player != null -> PlayerDetailContent(
                    player = state.player!!,
                    modifier = Modifier.padding(padding),
                    onSchedules = { nav.push(ScheduleListScreen(playerId)) },
                    onEvolutions = { nav.push(EvolutionListScreen(playerId)) }
                )
            }
        }
    }
}

@Composable
private fun PlayerDetailContent(
    player: Player,
    modifier: Modifier,
    onSchedules: () -> Unit,
    onEvolutions: () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
        ) {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                InfoRow("Nome",      player.fullName)
                if (player.birthDate != null) InfoRow(
                    "Nascimento",
                    "%02d/%02d/%04d".format(player.birthDate.dayOfMonth, player.birthDate.monthNumber, player.birthDate.year)
                )
                if (!player.position.isNullOrBlank())   InfoRow("Posição",    player.position)
                if (!player.team.isNullOrBlank())       InfoRow("Equipe",     player.team)
                if (!player.phone.isNullOrBlank())      InfoRow("Telefone",   player.phone)
                if (!player.notes.isNullOrBlank())      InfoRow("Observações", player.notes)
            }
        }

        Spacer(Modifier.height(8.dp))

        Button(onClick = onSchedules, modifier = Modifier.fillMaxWidth()) {
            Text("Cronogramas de Tratamento")
        }
        OutlinedButton(onClick = onEvolutions, modifier = Modifier.fillMaxWidth()) {
            Text("Evolução Diária")
        }
    }
}

@Composable
private fun InfoRow(label: String, value: String) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(
            "$label:",
            style = MaterialTheme.typography.bodySmall,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.weight(0.35f)
        )
        Text(value, style = MaterialTheme.typography.bodyMedium, modifier = Modifier.weight(0.65f))
    }
}

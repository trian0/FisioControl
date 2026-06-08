package br.com.mobapps.fisiocontrol.presentation.evolution.list

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import br.com.mobapps.fisiocontrol.domain.model.DailyEvolution
import br.com.mobapps.fisiocontrol.presentation.components.ErrorMessage
import br.com.mobapps.fisiocontrol.presentation.components.FisioTopBar
import br.com.mobapps.fisiocontrol.presentation.components.LoadingOverlay
import br.com.mobapps.fisiocontrol.presentation.evolution.form.EvolutionFormScreen
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import org.koin.core.parameter.parametersOf

data class EvolutionListScreen(val playerId: String) : Screen {
    @Composable
    override fun Content() {
        val nav = LocalNavigator.currentOrThrow
        val model = getScreenModel<EvolutionListScreenModel> { parametersOf(playerId) }
        val state by model.uiState.collectAsState()

        LaunchedEffect(nav.lastItem) {
            if (nav.lastItem is EvolutionListScreen) model.load()
        }

        Scaffold(
            topBar = { FisioTopBar("Evolução Diária", onBack = { nav.pop() }) },
            floatingActionButton = {
                FloatingActionButton(onClick = { nav.push(EvolutionFormScreen(playerId)) }) {
                    Icon(Icons.Default.Add, "Registrar sessão")
                }
            }
        ) { padding ->
            when {
                state.isLoading -> LoadingOverlay()
                state.error != null -> ErrorMessage(state.error!!, onRetry = model::load)
                state.evolutions.isEmpty() -> Column(
                    Modifier.fillMaxSize().padding(padding),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        "Nenhuma evolução registrada",
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                else -> LazyColumn(
                    modifier = Modifier.fillMaxSize().padding(padding),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(state.evolutions, key = { it.id }) { ev ->
                        EvolutionCard(ev)
                    }
                }
            }
        }
    }
}

@Composable
private fun EvolutionCard(ev: DailyEvolution) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.padding(16.dp).fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                val sessionDate = ev.sessionDate
                Text(
                    "${sessionDate.dayOfMonth.toString().padStart(2, '0')}/${
                        sessionDate.monthNumber.toString().padStart(2, '0')
                    }/${sessionDate.year.toString().padStart(4, '0')}",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
                ev.physiotherapyProcedures?.let {
                    Text(
                        "Condutas: $it",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                ev.objectiveNote?.let {
                    Text(
                        "Objetivo: $it",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            ev.painScale?.let { PainBadge(it) }
        }
    }
}

@Composable
private fun PainBadge(value: Int) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(48.dp)
                .background(MaterialTheme.colorScheme.primary, CircleShape)
        ) {
            Text(
                "$value",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }
        Text(
            "Nível da Dor",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

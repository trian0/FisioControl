package br.com.mobapps.fisiocontrol.presentation.schedule.list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Badge
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import br.com.mobapps.fisiocontrol.domain.model.ScheduleStatus
import br.com.mobapps.fisiocontrol.domain.model.TreatmentSchedule
import br.com.mobapps.fisiocontrol.presentation.components.ErrorMessage
import br.com.mobapps.fisiocontrol.presentation.components.FisioTopBar
import br.com.mobapps.fisiocontrol.presentation.components.LoadingOverlay
import br.com.mobapps.fisiocontrol.presentation.schedule.form.ScheduleFormScreen
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import org.koin.core.parameter.parametersOf

data class ScheduleListScreen(val playerId: String) : Screen {
    @Composable
    override fun Content() {
        val nav = LocalNavigator.currentOrThrow
        val model = getScreenModel<ScheduleListScreenModel> { parametersOf(playerId) }
        val state by model.uiState.collectAsState()

        LaunchedEffect(nav.lastItem) {
            if (nav.lastItem is ScheduleListScreen) model.load()
        }

        Scaffold(
            topBar = { FisioTopBar("Cronogramas", onBack = { nav.pop() }) },
            floatingActionButton = {
                FloatingActionButton(onClick = { nav.push(ScheduleFormScreen(playerId, "")) }) {
                    Icon(Icons.Default.Add, "Novo cronograma")
                }
            }
        ) { padding ->
            when {
                state.isLoading -> LoadingOverlay()
                state.error != null -> ErrorMessage(state.error!!, onRetry = model::load)
                state.schedules.isEmpty() -> Column(
                    Modifier.fillMaxSize().padding(padding),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        "Nenhum cronograma cadastrado",
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                else -> LazyColumn(
                    modifier = Modifier.fillMaxSize().padding(padding),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(state.schedules, key = { it.id }) { schedule ->
                        ScheduleCard(schedule) {
                            nav.push(
                                ScheduleFormScreen(
                                    playerId,
                                    schedule.id
                                )
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ScheduleCard(schedule: TreatmentSchedule, onClick: () -> Unit) {
    Card(modifier = Modifier.fillMaxWidth().clickable(onClick = onClick)) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    schedule.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.weight(1f)
                )
                Badge(
                    containerColor = when (schedule.status) {
                        ScheduleStatus.ACTIVE -> MaterialTheme.colorScheme.primary
                        ScheduleStatus.COMPLETED -> MaterialTheme.colorScheme.secondary
                        ScheduleStatus.PAUSED -> MaterialTheme.colorScheme.error
                    }
                ) { Text(schedule.status.label) }
            }
            val startDate = schedule.startDate
            Text(
                "Início: ${
                    startDate.dayOfMonth.toString().padStart(2, '0')
                }/${startDate.monthNumber.toString().padStart(2, '0')}/${
                    startDate.year.toString().padStart(4, '0')
                }  ·  ${schedule.sessionsPerWeek}x/semana",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
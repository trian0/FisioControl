package br.com.mobapps.fisiocontrol.presentation.schedule.form

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import br.com.mobapps.fisiocontrol.domain.model.ScheduleStatus
import br.com.mobapps.fisiocontrol.domain.utils.DateMaskTransformation
import br.com.mobapps.fisiocontrol.presentation.components.FisioTopBar
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import org.koin.core.parameter.parametersOf

data class ScheduleFormScreen(val playerId: String, val scheduleId: String) : Screen {
    @Composable
    override fun Content() {
        val nav   = LocalNavigator.currentOrThrow
        val model = getScreenModel<ScheduleFormScreenModel> { parametersOf(playerId, scheduleId) }
        val state by model.uiState.collectAsState()

        LaunchedEffect(state.isSaved) { if (state.isSaved) nav.pop() }

        Scaffold(
            topBar = {
                FisioTopBar(
                    title = if (model.isEditing) "Editar Cronograma" else "Novo Cronograma",
                    onBack = { nav.pop() }
                )
            }
        ) { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedTextField(
                    value = state.title, onValueChange = model::onTitleChange,
                    label = { Text("Título *") }, singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = state.weeklyPlanning, onValueChange = model::onWeeklyPlanningChange,
                    label = { Text("Planejamento semanal") }, minLines = 2, maxLines = 4,
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = state.startDate, onValueChange = model::onStartDateChange,
                    label = { Text("Data início *") }, singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    visualTransformation = DateMaskTransformation(),
                    modifier = Modifier.fillMaxWidth()
                )

                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("Sessões/semana:", style = MaterialTheme.typography.bodyMedium)
                    IconButton(onClick = { model.onSessionsChange(state.sessionsPerWeek - 1) }) {
                        Text("-", style = MaterialTheme.typography.titleLarge)
                    }
                    Text("${state.sessionsPerWeek}", style = MaterialTheme.typography.titleMedium)
                    IconButton(onClick = { model.onSessionsChange(state.sessionsPerWeek + 1) }) {
                        Icon(Icons.Default.Add, null)
                    }
                }

                Text("Status", style = MaterialTheme.typography.labelLarge)
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    ScheduleStatus.entries.forEach { status ->
                        FilterChip(
                            selected = state.status == status,
                            onClick = { model.onStatusChange(status) },
                            label = { Text(status.label) }
                        )
                    }
                }

                Text("Avaliação semanal", style = MaterialTheme.typography.labelLarge)
                OutlinedTextField(
                    value = state.weeklyAssessment,
                    onValueChange = model::onWeeklyAssessmentChange,
                    minLines = 3,
                    maxLines = 6,
                    modifier = Modifier.fillMaxWidth()
                )

                if (state.error != null) {
                    Text(state.error!!, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
                }

                Spacer(Modifier.height(8.dp))
                Button(
                    onClick = model::onSave,
                    enabled = !state.isLoading,
                    modifier = Modifier.fillMaxWidth().height(48.dp)
                ) { Text("Salvar") }
            }
        }
    }
}

package br.com.mobapps.fisiocontrol.presentation.evolution.form

import br.com.mobapps.fisiocontrol.domain.utils.DateMaskTransformation
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import br.com.mobapps.fisiocontrol.presentation.components.FisioTopBar
import br.com.mobapps.fisiocontrol.presentation.components.MetricSlider
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import org.koin.core.parameter.parametersOf

data class EvolutionFormScreen(val playerId: String) : Screen {
    @Composable
    override fun Content() {
        val nav   = LocalNavigator.currentOrThrow
        val model = getScreenModel<EvolutionFormScreenModel> { parametersOf(playerId) }
        val state by model.uiState.collectAsState()

        LaunchedEffect(state.isSaved) { if (state.isSaved) nav.pop() }

        Scaffold(
            topBar = { FisioTopBar("Registrar Sessão", onBack = { nav.pop() }) }
        ) { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                OutlinedTextField(
                    value = state.sessionDate,
                    onValueChange = model::onDateChange,
                    label = { Text("Data da sessão *") },
                    placeholder = { Text("DD/MM/AAAA") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    visualTransformation = DateMaskTransformation(),
                    modifier = Modifier.fillMaxWidth()
                )

                HorizontalDivider()
                Text("Métricas da sessão", style = MaterialTheme.typography.titleSmall, color = MaterialTheme.colorScheme.primary)

                MetricSlider(
                    label = "Escala de dor (0 = sem dor, 10 = máxima)",
                    value = state.painScale,
                    onValueChange = model::onPainChange
                )

                HorizontalDivider()
                Text("Condutas Fisioterapeuticas", style = MaterialTheme.typography.titleSmall, color = MaterialTheme.colorScheme.primary)

                OutlinedTextField(
                    value = state.physiotherapyProcedures,
                    onValueChange = model::onPhysiotherapyProceduresChange,
                    minLines = 3,
                    maxLines = 6,
                    modifier = Modifier.fillMaxWidth()
                )

                HorizontalDivider()
                Text("Observações", style = MaterialTheme.typography.titleSmall, color = MaterialTheme.colorScheme.primary)

                OutlinedTextField(
                    value = state.objectiveNote,
                    onValueChange = model::onObjectiveNoteChange,
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
                ) { Text("Registrar Evolução") }
            }
        }
    }
}

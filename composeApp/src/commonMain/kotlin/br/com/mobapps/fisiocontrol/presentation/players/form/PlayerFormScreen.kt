package br.com.mobapps.fisiocontrol.presentation.players.form

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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import br.com.mobapps.fisiocontrol.presentation.components.FisioTopBar
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import org.koin.core.parameter.parametersOf

private class DateMaskTransformation : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        val digits = text.text
        val formatted = buildString {
            digits.forEachIndexed { i, c ->
                if (i == 2 || i == 4) append('/')
                append(c)
            }
        }
        val offsetMap = object : OffsetMapping {
            override fun originalToTransformed(offset: Int) = when {
                offset <= 2 -> offset
                offset <= 4 -> offset + 1
                else        -> minOf(offset + 2, formatted.length)
            }
            override fun transformedToOriginal(offset: Int) = when {
                offset <= 2 -> offset
                offset <= 5 -> offset - 1
                else        -> minOf(offset - 2, digits.length)
            }
        }
        return TransformedText(AnnotatedString(formatted), offsetMap)
    }
}

data class PlayerFormScreen(val playerId: String) : Screen {
    @Composable
    override fun Content() {
        val nav   = LocalNavigator.currentOrThrow
        val model = getScreenModel<PlayerFormScreenModel> { parametersOf(playerId) }
        val state by model.uiState.collectAsState()

        LaunchedEffect(state.isSaved) {
            if (state.isSaved) nav.pop()
        }

        Scaffold(
            topBar = {
                FisioTopBar(
                    title = if (model.isEditing) "Editar Atleta" else "Novo Atleta",
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
                    value = state.fullName,
                    onValueChange = model::onFullNameChange,
                    label = { Text("Nome completo *") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = state.birthDate,
                    onValueChange = model::onBirthDateChange,
                    label = { Text("Data de nascimento") },
                    placeholder = { Text("DD/MM/AAAA") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    visualTransformation = DateMaskTransformation(),
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = state.position,
                    onValueChange = model::onPositionChange,
                    label = { Text("Posição") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = state.team,
                    onValueChange = model::onTeamChange,
                    label = { Text("Equipe") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = state.phone,
                    onValueChange = model::onPhoneChange,
                    label = { Text("Telefone") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = state.notes,
                    onValueChange = model::onNotesChange,
                    label = { Text("Observações") },
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
                ) {
                    if (state.isLoading) CircularProgressIndicator()
                    else Text("Salvar")
                }
            }
        }
    }
}

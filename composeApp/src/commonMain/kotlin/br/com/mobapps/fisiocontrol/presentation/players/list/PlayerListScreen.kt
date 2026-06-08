package br.com.mobapps.fisiocontrol.presentation.players.list

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
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import br.com.mobapps.fisiocontrol.domain.model.Player
import br.com.mobapps.fisiocontrol.presentation.components.ErrorMessage
import br.com.mobapps.fisiocontrol.presentation.components.FisioTopBar
import br.com.mobapps.fisiocontrol.presentation.components.LoadingOverlay
import br.com.mobapps.fisiocontrol.presentation.players.detail.PlayerDetailScreen
import br.com.mobapps.fisiocontrol.presentation.players.form.PlayerFormScreen
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow

class PlayerListScreen : Screen {
    @Composable
    override fun Content() {
        val nav   = LocalNavigator.currentOrThrow
        val model = getScreenModel<PlayerListScreenModel>()
        val state by model.uiState.collectAsState()

        LaunchedEffect(nav.lastItem) {
            if (nav.lastItem is PlayerListScreen) model.load()
        }

        Scaffold(
            topBar = { FisioTopBar("Atletas") },
            floatingActionButton = {
                FloatingActionButton(onClick = { nav.push(PlayerFormScreen("")) }) {
                    Icon(Icons.Default.Add, "Novo atleta")
                }
            }
        ) { padding ->
            Column(modifier = Modifier.fillMaxSize().padding(padding)) {
                OutlinedTextField(
                    value = state.searchQuery,
                    onValueChange = model::onSearch,
                    placeholder = { Text("Buscar atleta...") },
                    leadingIcon = { Icon(Icons.Default.Search, null) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp)
                )
                when {
                    state.isLoading -> LoadingOverlay()
                    state.error != null -> ErrorMessage(state.error!!, onRetry = model::load)
                    state.filtered.isEmpty() -> EmptyPlayers()
                    else -> PlayerList(state.filtered) { nav.push(PlayerDetailScreen(it.id)) }
                }
            }
        }
    }
}

@Composable
private fun PlayerList(players: List<Player>, onTap: (Player) -> Unit) {
    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(players, key = { it.id }) { player ->
            PlayerCard(player, onClick = { onTap(player) })
        }
    }
}

@Composable
private fun PlayerCard(player: Player, onClick: () -> Unit) {
    Card(modifier = Modifier.fillMaxWidth().clickable(onClick = onClick)) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Icon(
                Icons.Default.Person,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )
            Column(modifier = Modifier.weight(1f)) {
                Text(player.fullName, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                val sub = listOfNotNull(player.position, player.team).joinToString(" · ")
                if (sub.isNotEmpty()) {
                    Text(sub, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        }
    }
}

@Composable
private fun EmptyPlayers() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(Icons.Default.Person, null, tint = MaterialTheme.colorScheme.outlineVariant)
        Text("Nenhum atleta cadastrado", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}

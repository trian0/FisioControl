package br.com.mobapps.fisiocontrol.presentation.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LightColors = lightColorScheme(
    primary         = FisioBlue,
    onPrimary       = androidx.compose.ui.graphics.Color.White,
    primaryContainer = FisioBlueLight,
    secondary       = FisioGreen,
    onSecondary     = androidx.compose.ui.graphics.Color.White,
    error           = FisioRed,
    background      = FisioSurface,
    surface         = androidx.compose.ui.graphics.Color.White,
)

@Composable
fun FisioTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = LightColors,
        content     = content
    )
}

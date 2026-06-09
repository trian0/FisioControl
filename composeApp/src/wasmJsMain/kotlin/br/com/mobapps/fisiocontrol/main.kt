package br.com.mobapps.fisiocontrol

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.ComposeViewport
import org.koin.dsl.module

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    ComposeViewport(viewportContainerId = "composeApplication") {
        App(platformModule = module { })
    }
}

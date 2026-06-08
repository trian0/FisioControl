package br.com.mobapps.fisiocontrol

import androidx.compose.ui.window.ComposeUIViewController
import br.com.mobapps.fisiocontrol.data.local.DatabaseDriverFactory
import org.koin.dsl.module

fun MainViewController() = ComposeUIViewController {
    App(
        platformModule = module {
            single { DatabaseDriverFactory() }
        }
    )
}

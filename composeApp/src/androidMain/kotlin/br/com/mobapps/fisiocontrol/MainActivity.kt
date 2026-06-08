package br.com.mobapps.fisiocontrol

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import br.com.mobapps.fisiocontrol.data.local.DatabaseDriverFactory
import org.koin.dsl.module

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            App(
                platformModule = module {
                    single { DatabaseDriverFactory(applicationContext) }
                }
            )
        }
    }
}

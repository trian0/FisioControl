package br.com.mobapps.fisiocontrol

import android.os.Bundle
import android.os.StrictMode
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import br.com.mobapps.fisiocontrol.cache.FisioDatabase
import br.com.mobapps.fisiocontrol.data.local.DatabaseDriverFactory
import org.koin.dsl.module

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (BuildConfig.DEBUG) {
            StrictMode.setThreadPolicy(
                StrictMode.ThreadPolicy.Builder()
                    .detectDiskReads()
                    .detectDiskWrites()
                    .detectNetwork()
                    .penaltyLog()
                    .build()
            )
        }
        enableEdgeToEdge()
        setContent {
            App(
                platformModule = module {
                    single { DatabaseDriverFactory(applicationContext) }
                    single { FisioDatabase(get<DatabaseDriverFactory>().createDriver()) }
                }
            )
        }
    }
}

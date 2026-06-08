package br.com.mobapps.fisiocontrol.data.local

import android.content.Context
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import br.com.mobapps.fisiocontrol.cache.FisioDatabase

actual class DatabaseDriverFactory(private val context: Context) {
    actual fun createDriver(): SqlDriver =
        AndroidSqliteDriver(FisioDatabase.Schema, context, "fisio.db")
}

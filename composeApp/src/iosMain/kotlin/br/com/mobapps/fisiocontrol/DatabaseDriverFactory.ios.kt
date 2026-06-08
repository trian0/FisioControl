package br.com.mobapps.fisiocontrol.data.local

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.native.NativeSqliteDriver
import br.com.mobapps.fisiocontrol.cache.FisioDatabase

actual class DatabaseDriverFactory {
    actual fun createDriver(): SqlDriver =
        NativeSqliteDriver(FisioDatabase.Schema, "fisio.db")
}

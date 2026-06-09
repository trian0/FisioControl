package br.com.mobapps.fisiocontrol.data.local

import app.cash.sqldelight.db.SqlDriver

actual class DatabaseDriverFactory {
    actual fun createDriver(): SqlDriver {
        throw UnsupportedOperationException("Local database cache is not available on Web.")
    }
}

package br.com.mobapps.fisiocontrol.di

import br.com.mobapps.fisiocontrol.cache.FisioDatabase
import br.com.mobapps.fisiocontrol.data.local.DatabaseDriverFactory
import org.koin.dsl.module

val databaseModule = module {
    single { get<DatabaseDriverFactory>().createDriver() }
    single { FisioDatabase(get()) }
}

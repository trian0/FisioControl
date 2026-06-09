package br.com.mobapps.fisiocontrol.di

import br.com.mobapps.fisiocontrol.data.repository.AuthRepositoryImpl
import br.com.mobapps.fisiocontrol.data.repository.EvolutionRepositoryImpl
import br.com.mobapps.fisiocontrol.data.repository.PlayerRepositoryImpl
import br.com.mobapps.fisiocontrol.data.repository.ScheduleRepositoryImpl
import br.com.mobapps.fisiocontrol.domain.repository.AuthRepository
import br.com.mobapps.fisiocontrol.domain.repository.EvolutionRepository
import br.com.mobapps.fisiocontrol.domain.repository.PlayerRepository
import br.com.mobapps.fisiocontrol.domain.repository.ScheduleRepository
import org.koin.dsl.module

val repositoryModule = module {
    single<AuthRepository>      { AuthRepositoryImpl(get()) }
    single<PlayerRepository>    { PlayerRepositoryImpl(get(), getOrNull()) }
    single<ScheduleRepository>  { ScheduleRepositoryImpl(get(), getOrNull()) }
    single<EvolutionRepository> { EvolutionRepositoryImpl(get(), getOrNull()) }
}

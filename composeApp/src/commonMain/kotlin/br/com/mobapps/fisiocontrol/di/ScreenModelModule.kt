package br.com.mobapps.fisiocontrol.di

import br.com.mobapps.fisiocontrol.presentation.auth.LoginScreenModel
import br.com.mobapps.fisiocontrol.presentation.evolution.form.EvolutionFormScreenModel
import br.com.mobapps.fisiocontrol.presentation.evolution.list.EvolutionListScreenModel
import br.com.mobapps.fisiocontrol.presentation.players.detail.PlayerDetailScreenModel
import br.com.mobapps.fisiocontrol.presentation.players.form.PlayerFormScreenModel
import br.com.mobapps.fisiocontrol.presentation.players.list.PlayerListScreenModel
import br.com.mobapps.fisiocontrol.presentation.schedule.form.ScheduleFormScreenModel
import br.com.mobapps.fisiocontrol.presentation.schedule.list.ScheduleListScreenModel
import org.koin.dsl.module

val screenModelModule = module {
    factory { LoginScreenModel(get(), get()) }
    factory { PlayerListScreenModel(get()) }
    factory { (id: String) -> PlayerDetailScreenModel(id, get()) }
    factory { (id: String) -> PlayerFormScreenModel(id, get(), get()) }
    factory { (playerId: String) -> ScheduleListScreenModel(playerId, get()) }
    factory { (playerId: String, id: String) -> ScheduleFormScreenModel(playerId, id, get(), get()) }
    factory { (playerId: String) -> EvolutionListScreenModel(playerId, get()) }
    factory { (playerId: String) -> EvolutionFormScreenModel(playerId, get()) }
}

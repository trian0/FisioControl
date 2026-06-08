package br.com.mobapps.fisiocontrol.di

import br.com.mobapps.fisiocontrol.domain.usecase.auth.LoginUseCase
import br.com.mobapps.fisiocontrol.domain.usecase.auth.LogoutUseCase
import br.com.mobapps.fisiocontrol.domain.usecase.evolution.GetEvolutionsUseCase
import br.com.mobapps.fisiocontrol.domain.usecase.evolution.RecordEvolutionUseCase
import br.com.mobapps.fisiocontrol.domain.usecase.player.GetPlayerDetailUseCase
import br.com.mobapps.fisiocontrol.domain.usecase.player.GetPlayersUseCase
import br.com.mobapps.fisiocontrol.domain.usecase.player.SavePlayerUseCase
import br.com.mobapps.fisiocontrol.domain.usecase.schedule.GetScheduleByIdUseCase
import br.com.mobapps.fisiocontrol.domain.usecase.schedule.GetSchedulesUseCase
import br.com.mobapps.fisiocontrol.domain.usecase.schedule.SaveScheduleUseCase
import org.koin.dsl.module

val useCaseModule = module {
    factory { LoginUseCase(get()) }
    factory { LogoutUseCase(get()) }
    factory { GetPlayersUseCase(get()) }
    factory { GetPlayerDetailUseCase(get()) }
    factory { SavePlayerUseCase(get()) }
    factory { GetSchedulesUseCase(get()) }
    factory { GetScheduleByIdUseCase(get()) }
    factory { SaveScheduleUseCase(get()) }
    factory { GetEvolutionsUseCase(get()) }
    factory { RecordEvolutionUseCase(get()) }
}

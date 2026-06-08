package br.com.mobapps.fisiocontrol.di

import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.realtime.Realtime
import org.koin.dsl.module

val networkModule = module {
    single {
        createSupabaseClient(
            supabaseUrl  = AppConfig.supabaseUrl,
            supabaseKey  = AppConfig.supabaseAnonKey
        ) {
            install(Auth)
            install(Postgrest)
            install(Realtime)
        }
    }
}

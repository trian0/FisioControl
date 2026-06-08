package br.com.mobapps.fisiocontrol.di

expect object AppConfig {
    val supabaseUrl: String
    val supabaseAnonKey: String
}

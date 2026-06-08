package br.com.mobapps.fisiocontrol.di

actual object AppConfig {
    actual val supabaseUrl: String     = br.com.mobapps.fisiocontrol.BuildConfig.SUPABASE_URL
    actual val supabaseAnonKey: String = br.com.mobapps.fisiocontrol.BuildConfig.SUPABASE_ANON_KEY
}

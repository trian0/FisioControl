package br.com.mobapps.fisiocontrol.di

import platform.Foundation.NSBundle

actual object AppConfig {
    actual val supabaseUrl: String
        get() = NSBundle.mainBundle.infoDictionary?.get("SUPABASE_URL") as? String ?: ""
    actual val supabaseAnonKey: String
        get() = NSBundle.mainBundle.infoDictionary?.get("SUPABASE_ANON_KEY") as? String ?: ""
}

package br.com.mobapps.fisiocontrol.di

import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSBundle
import platform.Foundation.NSFileManager
import platform.Foundation.NSPropertyListSerialization

@OptIn(ExperimentalForeignApi::class)
private val secrets: Map<Any?, *>? by lazy {
    val path = NSBundle.mainBundle.pathForResource("Secrets", "plist") ?: return@lazy null
    val data = NSFileManager.defaultManager.contentsAtPath(path) ?: return@lazy null
    @Suppress("UNCHECKED_CAST")
    NSPropertyListSerialization.propertyListWithData(
        data,
        options = 0u,
        format = null,
        error = null,
    ) as? Map<Any?, *>
}

actual object AppConfig {
    actual val supabaseUrl: String
        get() = secrets?.get("SUPABASE_URL") as? String ?: ""
    actual val supabaseAnonKey: String
        get() = secrets?.get("SUPABASE_ANON_KEY") as? String ?: ""
}

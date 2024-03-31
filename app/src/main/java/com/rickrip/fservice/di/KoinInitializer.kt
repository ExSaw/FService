package com.rickrip.fservice.di

/**
 * @author A.Chudov
 */
import android.content.Context
import androidx.startup.Initializer
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.KoinApplication
import org.koin.core.context.GlobalContext.startKoin

class KoinInitializer : Initializer<KoinApplication> {

    override fun dependencies(): MutableList<Class<out Initializer<*>>> = mutableListOf()

    override fun create(context: Context): KoinApplication =
        startKoin {
            androidLogger()
            allowOverride(false)
            androidContext(context)
            modules(appModule)
        }
}
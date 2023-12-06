package com.example.translationproject

import android.app.Application
import android.content.Context
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin

class MainApplication : Application() {
    override fun onCreate(){
        super.onCreate()
        MainApplication.appContext = applicationContext
        startKoin{
            androidContext(this@MainApplication)
            modules(listOf(appModule))
        }
    }
    companion object {
        lateinit  var appContext: Context
    }
}
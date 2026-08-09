package com.example.restaurantflk

import android.app.Application
import com.example.restaurantflk.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class RestaurantApplication: Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@RestaurantApplication)
            modules(appModule)
        }

    }
}
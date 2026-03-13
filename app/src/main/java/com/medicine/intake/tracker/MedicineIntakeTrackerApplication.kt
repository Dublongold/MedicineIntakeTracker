package com.medicine.intake.tracker

import android.app.Application
import com.medicine.intake.tracker.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin


class MedicineIntakeTrackerApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@MedicineIntakeTrackerApplication)
            modules(appModule)
        }
    }
}
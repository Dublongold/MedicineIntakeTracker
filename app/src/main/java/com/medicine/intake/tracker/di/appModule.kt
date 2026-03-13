package com.medicine.intake.tracker.di

import androidx.room.Room
import com.medicine.intake.tracker.MainViewModel
import com.medicine.intake.tracker.data.AppDatabase
import com.medicine.intake.tracker.data.intake.AndroidIntakeRepository
import com.medicine.intake.tracker.data.medicine.AndroidMedicineRepository
import com.medicine.intake.tracker.data.settings.DataStoreSettingsRepository
import com.medicine.intake.tracker.domain.datetime.CurrentLocalDateTimeProvider
import com.medicine.intake.tracker.domain.intake.IntakeRepository
import com.medicine.intake.tracker.domain.medicine.MedicineRepository
import com.medicine.intake.tracker.domain.settings.SettingsRepository
import com.medicine.intake.tracker.ui.main.history.HistoryViewModel
import com.medicine.intake.tracker.domain.intake.usecase.CanIntakeUseCase
import com.medicine.intake.tracker.ui.main.home.HomeViewModel
import com.medicine.intake.tracker.domain.intake.usecase.RecentIntakesUseCase
import com.medicine.intake.tracker.ui.main.history.statistics.StatisticsViewModel
import com.medicine.intake.tracker.ui.main.medicine.MedicineViewModel
import com.medicine.intake.tracker.ui.main.medicine.edit.MedicineEditViewModel
import com.medicine.intake.tracker.ui.main.settings.SettingsViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.binds
import org.koin.dsl.module

val appModule = module {
    single {
        Room.databaseBuilder(
            androidContext(),
            AppDatabase::class.java,
            "medicine_intake_tracker.db"
        ).build()
    }
    single { get<AppDatabase>().intakeDao() }
    single { get<AppDatabase>().medicineDao() }

    singleOf(::DataStoreSettingsRepository) binds SettingsRepository.classesForBind
    singleOf(::AndroidMedicineRepository) binds MedicineRepository.classesForBind
    singleOf(::AndroidIntakeRepository) binds IntakeRepository.classesForBind

    singleOf(::CurrentLocalDateTimeProvider)
    singleOf(::CanIntakeUseCase)
    singleOf(::RecentIntakesUseCase)

    viewModelOf(::MainViewModel)
    viewModelOf(::HomeViewModel)
    viewModelOf(::HistoryViewModel)
    viewModelOf(::SettingsViewModel)
    viewModelOf(::MedicineViewModel)
    viewModelOf(::MedicineEditViewModel)
    viewModelOf(::StatisticsViewModel)
}
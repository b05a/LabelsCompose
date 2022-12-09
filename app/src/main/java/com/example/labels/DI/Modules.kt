package com.example.labels.DI

import android.os.Handler
import android.os.Looper
import com.example.labels.Cases.ChangeColor
import com.example.labels.Cases.DisplayType
import com.example.labels.Cases.GenerationId
import com.example.labels.Cases.ToastMessage
import com.example.labels.Repository.MainDb
import com.example.labels.Repository.RepositoryRoom
import com.example.labels.ViewModel.MainViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    viewModel { MainViewModel(get(), get(), get(), get(), get(), get()) }
    single<DisplayType> { DisplayType() }
    single<RepositoryRoom> { RepositoryRoom(get()) }
    single<MainDb> { MainDb.getDb(androidContext()) }
    single<ChangeColor> { ChangeColor() }
    single<Handler> { Handler(Looper.getMainLooper()) }
    single<ToastMessage> { ToastMessage(androidContext()) }
    single<GenerationId> { GenerationId() }

}
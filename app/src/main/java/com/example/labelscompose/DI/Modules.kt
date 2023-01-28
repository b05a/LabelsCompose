package com.example.labelscompose.DI

import android.os.Handler
import android.os.Looper
import com.example.labelscompose.Cases.ChangeColor
import com.example.labelscompose.Cases.DisplayType
import com.example.labelscompose.Cases.GenerationId
import com.example.labelscompose.Cases.ToastMessage
import com.example.labelscompose.Repository.MainDb
import com.example.labelscompose.Repository.RepositoryRoom
import com.example.labelscompose.ViewModel.MainViewModel
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
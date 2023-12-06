package com.example.translationproject

import CameraPageViewModel
import com.example.translationproject.AccountPage.AccountPageViewModel
import com.example.translationproject.StartPage.StartPageViewModel
import com.example.translationproject.Util.TextHolder
import com.example.translationproject.Util.SpeechToText
import com.example.translationproject.Util.SessionHolder
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    viewModel { StartPageViewModel(get(), get(), get()) }
    viewModel { CameraPageViewModel(get()) }
    viewModel { AccountPageViewModel(get()) }
    factory { SpeechToText(androidContext()) }
    single { TextHolder() }
    single { SessionHolder() }
}
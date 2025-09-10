package com.example.ktranslate

import com.example.data.dataModule
import com.example.domain.use_cases.DeleteTranslationFromHistoryUseCase
import com.example.domain.use_cases.FavouriteTranslationUseCase
import com.example.domain.use_cases.GetFavouritesUseCase
import com.example.domain.use_cases.GetHistoryUseCase
import com.example.domain.use_cases.TranslateUseCase
import com.example.ktranslate.favourites_screen.FavouritesViewModel
import com.example.ktranslate.translate_screen.TranslateViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val domainModule = module {
    factory<TranslateUseCase> { TranslateUseCase(get(), get()) }
    factory<FavouriteTranslationUseCase> { FavouriteTranslationUseCase(get()) }
    factory<GetHistoryUseCase> { GetHistoryUseCase(get()) }
    factory<DeleteTranslationFromHistoryUseCase> { DeleteTranslationFromHistoryUseCase(get()) }
    factory<GetFavouritesUseCase> { GetFavouritesUseCase(get()) }
}

val appModule = module {
    single<ConnectivityObserver> { NetworkConnectivityObserver(androidContext()) }
    viewModel<TranslateViewModel> { TranslateViewModel(get(), get(), get(), get(), get()) }
    viewModel<FavouritesViewModel> { FavouritesViewModel(get(), get()) }
}

val koinModules = listOf(appModule, domainModule, dataModule)
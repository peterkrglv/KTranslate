package com.example.ktranslate

import com.example.data.dataModule
import com.example.domain.use_cases.DeleteHistoryItemUseCase
import com.example.domain.use_cases.FavouriteTranslationUseCase
import com.example.domain.use_cases.GetHistoryUseCase
import com.example.domain.use_cases.TranslateUseCase
import com.example.ktranslate.translate_screen.TranslateViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val domainModule = module {
    factory<TranslateUseCase> { TranslateUseCase(get(), get()) }
    factory<FavouriteTranslationUseCase> { FavouriteTranslationUseCase(get()) }
    factory<GetHistoryUseCase> { GetHistoryUseCase(get()) }
    factory<DeleteHistoryItemUseCase> { DeleteHistoryItemUseCase(get()) }
}

val appModule = module {
    viewModel<TranslateViewModel> { TranslateViewModel(get(), get(), get(), get()) }
}

val koinModules = listOf(appModule, domainModule, dataModule)
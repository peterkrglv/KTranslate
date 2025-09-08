package com.example.ktranslate

import com.example.data.dataModule
import com.example.domain.use_cases.DeleteHistoryItemUseCase
import com.example.domain.use_cases.FavouriteTranslationUseCase
import com.example.domain.use_cases.GetSearchHistoryUseCase
import com.example.domain.use_cases.SearchUseCase
import com.example.ktranslate.translate_screen.TranslateViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val domainModule = module {
    factory<SearchUseCase> { SearchUseCase() }
    factory<FavouriteTranslationUseCase> { FavouriteTranslationUseCase() }
    factory<GetSearchHistoryUseCase> { GetSearchHistoryUseCase() }
    factory<DeleteHistoryItemUseCase> { DeleteHistoryItemUseCase() }
}

val appModule = module {
    viewModel<TranslateViewModel> { TranslateViewModel(get(), get(), get(), get()) }

}

val koinModules = listOf(appModule, domainModule, dataModule)
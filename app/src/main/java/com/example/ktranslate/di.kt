package com.example.ktranslate

import com.example.data.dataModule
import org.koin.dsl.module

val domainModule = module {

}

val appModule = module {

}

val koinModules = listOf(appModule, domainModule, dataModule)
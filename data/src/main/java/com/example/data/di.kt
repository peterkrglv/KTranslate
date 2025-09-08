package com.example.data

import com.example.data.api.TranslationApi
import com.example.data.api.TranslationRepositoryImpl
import com.example.data.local.CacheRepositoryTest
import com.example.domain.repositories.CacheRepository
import com.example.domain.repositories.TranslationRepository
import okhttp3.OkHttpClient
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

const val baseUrl = "https://dictionary.skyeng.ru/api/public/v1/words/"

val dataModule = module {
    single<OkHttpClient> { OkHttpClient() }
    single<Retrofit> {
        Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .client(get())
            .build()
    }

    single<TranslationApi> { get<Retrofit>().create(TranslationApi::class.java) }

    single<TranslationRepository> { TranslationRepositoryImpl(get()) }

    single<CacheRepository> { CacheRepositoryTest() }
}
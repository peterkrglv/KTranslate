package com.example.data

import androidx.room.Room
import com.example.data.api.TranslationApi
import com.example.data.api.TranslationRepositoryImpl
import com.example.data.local.AppDatabase
import com.example.data.local.CacheRepositoryImpl
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
    single {
        Room.databaseBuilder(
            get(),
            AppDatabase::class.java,
            "word_translation_database"
        )
            .build()
    }

    single { get<AppDatabase>().wordTranslationDao() }
    
    single<CacheRepository> { CacheRepositoryImpl(get()) }
}
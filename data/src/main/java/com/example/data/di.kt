package com.example.data

import okhttp3.OkHttpClient
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val dataModule = module {
    single<OkHttpClient> { OkHttpClient() }
    single<Retrofit> {
        Retrofit.Builder()
            .baseUrl("https://dictionary.skyeng.ru/api/public/v1")
            .addConverterFactory(GsonConverterFactory.create())
            .client(get())
            .build()
    }
}
package com.example.data.api

import retrofit2.http.GET
import retrofit2.http.Query

interface TranslationApi {
    @GET("search")
    suspend fun translate(
        @Query("search") word: String,
        @Query("pageSize") pageSize: Int = 1
    ): List<TranslationResponse>
}
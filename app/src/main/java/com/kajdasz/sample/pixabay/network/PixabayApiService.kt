package com.kajdasz.sample.pixabay.network

import com.kajdasz.sample.pixabay.BuildConfig
import com.kajdasz.sample.pixabay.network.model.ImageSearchResult
import retrofit2.http.GET
import retrofit2.http.Query

interface PixabayApiService {
    @GET("api/")
    suspend fun searchImages(
        @Query("q") title: String,
        @Query("page") page: Int = 1,
        @Query("key") key: String = BuildConfig.PIXABAY_KEY,
    ): ImageSearchResult
}

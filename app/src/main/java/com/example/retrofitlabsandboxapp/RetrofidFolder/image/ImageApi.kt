package com.example.retrofitlabsandboxapp.RetrofidFolder.image


import com.example.retrofitlabsandboxapp.BuildConfig
import retrofit2.Response

import retrofit2.http.GET
import retrofit2.http.Query


interface ImageApi {
    @GET("api/")
    suspend fun searchImages(
        @Query("q") query: String,
        @Query("key") key: String = BuildConfig.PIXABAY_API_KEY,
        @Query("image_type") imageType: String = "photo"
    ): Response<PixabayWrapper>
}
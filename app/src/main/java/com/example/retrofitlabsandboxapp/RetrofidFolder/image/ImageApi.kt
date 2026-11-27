package com.example.retrofitlabsandboxapp.RetrofidFolder.image


import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query


interface ImageApi {
    @GET("api/")
    suspend fun searchImages(
        @Query("q") query: String,
        @Query("image_type") imageType: String = "photo",
        @Query("page") pageNum: Int = 1
    ): Response<PixabayWrapper>
}
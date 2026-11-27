package com.example.retrofitlabsandboxapp.RetrofidFolder


import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header

interface HttpBinApi {
    @GET("headers")
    suspend fun httpBinApi(@Header("X-Custom-Header") myHeader: String): Response<DTO>
}
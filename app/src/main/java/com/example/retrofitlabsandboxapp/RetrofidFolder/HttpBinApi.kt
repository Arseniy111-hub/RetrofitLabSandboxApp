package com.example.retrofitlabsandboxapp.RetrofidFolder


import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header

interface HttpBinApi {
    @GET("headers")
    suspend fun getHeader(@Header("X-Custom-Header") myHeader: String): Response<HeaderData>
}
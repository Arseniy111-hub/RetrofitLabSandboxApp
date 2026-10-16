package com.example.retrofitlabsandboxapp.RetrofidFolder.post


import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface PostApi {
    @GET("posts")
    suspend fun getAllPosts(): Response<List<PostObject>>

    @GET("posts")
    suspend fun searchPost(@Query("userId") userId: Int = -1): Response<List<PostObject>>
}

package com.example.retrofitlabsandboxapp.RetrofidFolder.post


import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface PostApi {
//    @GET("posts")
//    suspend fun getAllPosts(): Response<List<PostObject>>

    @GET("posts")
    suspend fun searchPost(@Query("userId") userId: Int?): Response<List<PostObject>>

    @GET("posts/{postId}")
    suspend fun openPost(@Path("postId") postId: Int): Response<PostObject>
}

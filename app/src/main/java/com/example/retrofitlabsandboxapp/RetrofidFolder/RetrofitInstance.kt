package com.example.retrofitlabsandboxapp.RetrofidFolder

import com.example.retrofitlabsandboxapp.RetrofidFolder.image.ImageApi
import com.example.retrofitlabsandboxapp.RetrofidFolder.post.PostApi
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }
    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .addNetworkInterceptor(PixabayKeyInterceptor())
        .build()

    val httpBinApi: HttpBinApi by lazy {
        Retrofit.Builder()
            .baseUrl("https://httpbin.org/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(HttpBinApi::class.java)
    }

    val postApi: PostApi by lazy {
        Retrofit.Builder()
            .baseUrl("https://jsonplaceholder.typicode.com/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(PostApi::class.java)
    }
    val imageApi: ImageApi by lazy {
        Retrofit.Builder()
            .baseUrl("https://pixabay.com/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ImageApi::class.java)

    }

}
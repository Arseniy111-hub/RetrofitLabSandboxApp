package com.example.retrofitlabsandboxapp.RetrofidFolder

import com.example.retrofitlabsandboxapp.BuildConfig
import okhttp3.Interceptor
import okhttp3.Response

class PixabayKeyInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()
        val originalHttpUrl = original.url
        if ((originalHttpUrl.host.contains("pixabay.com"))) {
            val requestBuilder = original.newBuilder()
                .url(
                    originalHttpUrl.newBuilder()
                        .addQueryParameter("key", BuildConfig.PIXABAY_API_KEY)
                        .build()
                )
            return chain.proceed(requestBuilder.build())
        } else {
            return chain.proceed(original)
        }

    }
}
package com.example.retrofitlabsandboxapp.RetrofidFolder.image

data class PixabayWrapper(
    val total: Int,
    val totalHits: Int,
    val hits: List<PixabayImage>
)

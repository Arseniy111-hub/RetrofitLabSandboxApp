package com.example.retrofitlabsandboxapp.mvi.image

import com.example.retrofitlabsandboxapp.RetrofidFolder.image.PixabayImage

sealed interface ImageIntent {
    data class UpdateQuery(val query: String) : ImageIntent
    data class OnSearchQuery(val query: String) : ImageIntent
    data class SelectImage(val image: PixabayImage) : ImageIntent
    object CloseImage : ImageIntent
}

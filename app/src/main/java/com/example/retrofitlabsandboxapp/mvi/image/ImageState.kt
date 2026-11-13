package com.example.retrofitlabsandboxapp.mvi.image


import com.example.retrofitlabsandboxapp.RetrofidFolder.image.PixabayImage

data class ImageState(
    val image: PixabayImage? = null,
    val query: String = "",
    val isLoading: Boolean = false,
    val error: String = "",
    val images: List<PixabayImage> = emptyList(),
    val isEmptySearch: Boolean = false,

)

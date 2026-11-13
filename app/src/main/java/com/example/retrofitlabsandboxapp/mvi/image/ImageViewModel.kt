package com.example.retrofitlabsandboxapp.mvi.image

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.retrofitlabsandboxapp.Logs.Tags
import com.example.retrofitlabsandboxapp.Logs.writeLog
import com.example.retrofitlabsandboxapp.RetrofidFolder.RetrofitInstance
import com.example.retrofitlabsandboxapp.RetrofidFolder.image.PixabayImage
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ImageViewModel : ViewModel() {
    private var job: Job? = null
    private val _state = MutableStateFlow(ImageState())
    val state = _state.asStateFlow()
    private fun updateSearchResult(images: List<PixabayImage>) {
        _state.update {
            it.copy(
                images = images,
                isEmptySearch = images.isEmpty(),
                isLoading = false
            )
        }
    }

    fun onIntent(intent: ImageIntent) {

        when (intent) {

            ImageIntent.CloseImage -> {
                _state.update {
                    it.copy(
                        image = null
                    )
                }
            }

            is ImageIntent.OnSearchQuery -> {
                // Пустой запрос — ничего не делаем (пункт 4 ТЗ)
                if (intent.query.isBlank()) return

                job?.cancel()
                _state.update {
                    it.copy(
                        query = intent.query,
                        images = emptyList(),
                        isLoading = true,
                        isEmptySearch = false,
                        error = ""
                    )
                }
                job = viewModelScope.launch {
                    try {
                        val response = RetrofitInstance.imageApi.searchImages(intent.query)
                        if (response.isSuccessful) {
                            updateSearchResult(response.body()?.hits ?: emptyList())
                        } else {
                            writeLog(response.code().toString(), Tags.IMAGE_VIEWMODEL)
                            _state.update {
                                it.copy(
                                    error = response.code().toString(),
                                    isLoading = false
                                )
                            }
                        }
                    } catch (e: CancellationException) {
                        writeLog(e.message.toString(), Tags.IMAGE_VIEWMODEL)
                        throw e

                    } catch (e: Exception) {
                        writeLog(e.message.toString(), Tags.IMAGE_VIEWMODEL)
                        _state.update {
                            it.copy(
                                error = e.message.toString(),
                                isLoading = false
                            )
                        }
                    }
                }

            }

            is ImageIntent.SelectImage -> {
                _state.update {
                    it.copy(
                        image = intent.image
                    )
                }
            }

            is ImageIntent.UpdateQuery -> {
                _state.update {
                    it.copy(
                        query = intent.query
                    )
                }
            }
        }
    }


}

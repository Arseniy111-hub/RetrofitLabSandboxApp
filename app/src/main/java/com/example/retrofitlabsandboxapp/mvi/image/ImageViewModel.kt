package com.example.retrofitlabsandboxapp.mvi.image

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.retrofitlabsandboxapp.Logs.Tags
import com.example.retrofitlabsandboxapp.Logs.writeLog
import com.example.retrofitlabsandboxapp.RetrofidFolder.RetrofitInstance
import com.example.retrofitlabsandboxapp.RetrofidFolder.image.PixabayImage
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ImageViewModel : ViewModel() {
    private var job: Job? = null
    private val _state = MutableStateFlow(ImageState())
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            try {
                val response = RetrofitInstance.httpBinApi.getHeader("Test_header_sending")
                if (response.isSuccessful) {
                    val answer = response.body()?.headers?.get("X-Custom-Header")
                    writeLog("httpbin вернул заголовок: $answer", Tags.IMAGE_VIEWMODEL)
                } else {
                    writeLog(response.code().toString(), Tags.IMAGE_VIEWMODEL)
                }
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                writeLog(e.message.toString(), Tags.IMAGE_VIEWMODEL)
            }

        }

    }
    private fun updateSearchResult(images: List<PixabayImage>) {
        _state.update {
            it.copy(
                images = images,
                isEmptySearch = images.isEmpty(),
                isLoading = false
            )
        }
    }
    private fun fetchImages(query: String, page: Int) {
        if (query.isBlank()) return


        job?.cancel()
        _state.update {
            it.copy(
                query = query,
                images = emptyList(),
                isLoading = true,
                isEmptySearch = false,
                error = ""
            )
        }
        job = viewModelScope.launch {
            delay(1000L)
            try {
                val response = RetrofitInstance.imageApi.searchImages(query = query, pageNum = page)
                if (response.isSuccessful) {
                    if (response.body()?.hits == emptyList<PixabayImage>()) {
                        _state.update {
                            it.copy(
                                error = "Nothing not find",
                                isLoading = false

                            )
                        }
                    } else {
                        updateSearchResult(response.body()?.hits ?: emptyList())
                        writeLog(response.isSuccessful.toString(), Tags.IMAGE_VIEWMODEL)
                        writeLog(
                            "Counter of fetched images equals ${response.body()?.hits?.count()}",
                            Tags.IMAGE_VIEWMODEL
                        )
                    }

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
                _state.update {
                    it.copy(
                        currentPage = 1
                    )
                }
                fetchImages(
                    intent.query,
                    _state.value.currentPage
                )

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

            is ImageIntent.NextPage -> {
                _state.update {
                    it.copy(
                        currentPage = intent.currentPage + 1
                    )
                }

                fetchImages(
                    _state.value.query,
                    intent.currentPage + 1
                )

            }

            is ImageIntent.PrevPage -> {
                if (_state.value.currentPage > 1) {
                    _state.update {
                        it.copy(
                            currentPage = intent.currentPage - 1
                        )
                    }

                    fetchImages(
                        _state.value.query,
                        intent.currentPage - 1
                    )

                }

            }

            is ImageIntent.ManualPageInput -> {
                _state.update {
                    it.copy(
                        currentPage = intent.currentPage,

                        )
                }
                viewModelScope.launch {
                    RetrofitInstance.imageApi.searchImages(
                        query = _state.value.query,
                        pageNum = _state.value.currentPage
                    )
                }
            }
        }
    }


}

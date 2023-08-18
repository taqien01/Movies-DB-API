package com.example.movieapi.ui.detail

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movieapi.model.Review
import com.example.movieapi.service.AppRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class DetailViewModel : ViewModel() {
    private val appRepository = AppRepository()
    private val _uiDetailState = MutableStateFlow(DetailState())
    val uiDetailState: StateFlow<DetailState> = _uiDetailState.asStateFlow()

    fun newReview(moviesId: String) {
        _uiDetailState.update { c -> c.copy(movieId = moviesId) }
        getReview(false)

    }

    fun nextReview() {
        getReview(true)
    }

    private fun getReview(isNextPage: Boolean) {
        _uiDetailState.update { currentState ->
            currentState.copy(
                loadingReview = true,
            )
        }
        viewModelScope.launch {
            try {
                if (!isNextPage) {
                    _uiDetailState.update { currentState ->
                        currentState.copy(
                            page = 1,
                        )
                    }
                }
                val resp = appRepository.getUserReview(
                    movieId = _uiDetailState.value.movieId,
                    page = _uiDetailState.value.page,
                )

                var listReview = mutableListOf<Review>()

                if (_uiDetailState.value.page == 1) {
                    listReview.addAll(resp.body()!!.results)
                } else {
                    listReview.addAll(_uiDetailState.value.listReview)
                    listReview.addAll(resp.body()!!.results)
                }

                if (resp.isSuccessful) {
                    Log.e("detail ViewModel", "size review: ${resp.body()!!.results.size}")
                    if (resp.body()!!.results.size != 0) {
                        _uiDetailState.update { currentState ->
                            currentState.copy(
                                listReview = listReview,
                                loadingMoreReview = false,
                                page = _uiDetailState.value.page.plus(1),
                                loadingReview = false,
                            )
                        }
                    }else{
                        _uiDetailState.update { currentState ->
                            currentState.copy(
                                loadingMoreReview = false,
                                loadingReview = false,
                            )
                        }
                    }
                } else {
                    _uiDetailState.update { currentState ->
                        currentState.copy(
                            loadingMoreReview = false,
                            loadingReview = false,
                        )
                    }
                }
            } catch (e: Exception) {
                Log.d("detail ViewModel", "getdetailreview: ${e.toString()}")
                _uiDetailState.update { currentState ->
                    currentState.copy(
                        loadingMoreReview = false,
                        loadingReview = false,
                    )
                }
            }
        }
    }

}
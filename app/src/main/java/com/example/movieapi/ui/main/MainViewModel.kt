package com.example.movieapi.ui.main

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movieapi.model.Movies
import com.example.movieapi.service.AppRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.lang.Exception

class MainViewModel : ViewModel() {
    private val appRepository = AppRepository()
    private val _uiState = MutableStateFlow(MainState())
    val uiState: StateFlow<MainState> = _uiState.asStateFlow()

    init {
        getGenres()
    }

    private fun getGenres() {
        viewModelScope.launch {
            try {
                val listGenre = appRepository.getGenres()
                Log.d("Main ViewModel", "getGenres list: ${listGenre.body()}")
//                Log.d("Main ViewModel", "getGenres list: ${listGenre.code()}")

                _uiState.update { currentState ->
                    currentState.copy(
                        listGenres = listGenre.body()!!.listGenres.toMutableList(),
                        loadingGenre = false,
                        loadingMovie = true,
                        listMovies = mutableListOf(),
                        page = 1,
                        selectedGenre = listGenre.body()!!.listGenres[0].id.toString()
                    )
                }
                getMovies()

            } catch (e: Exception) {
                //
                Log.d("Main ViewModel", "getGenres: ${e.toString()}")
                _uiState.update { currentState ->
                    currentState.copy(
                        loadingGenre = false,
                        loadingMovie = false,
                    )
                }
            }
        }
    }

    fun setMovies(movies: Movies) {
        _uiState.update { currentState ->
            currentState.copy(movies = movies)
        }
    }

    fun getNextPage() {
        _uiState.update { currentState ->
            currentState.copy(
                page = _uiState.value.page.plus(1),
                loadingMore = true,
                )
        }
        getMovies()
    }

    fun changeGenres(changedGenres: String) {
        if (changedGenres != _uiState.value.selectedGenre) {
            _uiState.update { currentState ->
                currentState.copy(
                    page = 1,
                    loadingMovie = true,
                    selectedGenre = changedGenres,
                    listMovies = mutableListOf(),
                )
            }
            getMovies()
        }
    }

    private fun getMovies() {
//        _uiState.update { currentState ->
//            currentState.copy(
//                loadingMovie = true,
//            )
//        }
        viewModelScope.launch {
            try {
                val getMovies = appRepository.getMovies(
                    page = _uiState.value.page,
                    genres = _uiState.value.selectedGenre,
                )
                var listMovies: MutableList<Movies> = mutableListOf()
                if (_uiState.value.page == 1) {
                    listMovies.addAll(getMovies.body()!!.results)
                } else {
                    listMovies.addAll(_uiState.value.listMovies)
                    listMovies.addAll(getMovies.body()!!.results)
                }
                var newPage = _uiState.value.page.plus(1)
                _uiState.update { currentState ->
                    currentState.copy(
                        loadingMovie = false,
                        listMovies = listMovies,
                        page = newPage,
                        loadingMore = false,
                    )
                }
            } catch (e: Exception) {
                _uiState.update { currentState ->
                    currentState.copy(
                        loadingMovie = false,
                        loadingMore = false,
                    )
                }
            }
        }
    }
}
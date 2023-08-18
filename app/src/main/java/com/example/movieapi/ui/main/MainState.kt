package com.example.movieapi.ui.main

import com.example.movieapi.model.Genre
import com.example.movieapi.model.ListMovies
import com.example.movieapi.model.Movies

data class MainState(
    val page: Int = 1,
    val listGenres: MutableList<Genre> = mutableListOf(),
    val listMovies: MutableList<Movies> = mutableListOf(),
    val selectedGenre: String = "",
    val loadingGenre: Boolean = true,
    val loadingMovie: Boolean = true,
    val loadingMore: Boolean = true,
    val movies: Movies? = null,
)
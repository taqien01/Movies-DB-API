package com.example.movieapi.model

import com.google.gson.annotations.SerializedName

data class ListMovies(
    @SerializedName("results") var results: List<Movies> = listOf(),
    @SerializedName("page") var page: Int? = null,
    @SerializedName("total_pages") var totalPages: Int? = null,
    @SerializedName("total_results") var totalResults: Int? = null

)
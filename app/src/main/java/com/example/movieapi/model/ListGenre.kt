package com.example.movieapi.model

import com.google.gson.annotations.SerializedName

data class ListGenre(
    @SerializedName("genres") var listGenres: List<Genre> = listOf<Genre>()
)
package com.example.movieapi.model

import com.google.gson.annotations.SerializedName

class ListReview(
    @SerializedName("id") var id : Int? = null,
    @SerializedName("page") var page : Int? = null,
    @SerializedName("total_pages") var totalPages : Int? = null,
    @SerializedName("total_results") var totalResults : Int? = null,
    @SerializedName("results") var results : ArrayList<Review> = arrayListOf(),
)
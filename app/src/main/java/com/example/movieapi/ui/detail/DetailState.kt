package com.example.movieapi.ui.detail

import com.example.movieapi.model.Review

data class DetailState (
    val data: String = "",
    val movieId: String = "",
    val page: Int = 0,
    val loadingReview: Boolean = false,
    val loadingMoreReview: Boolean = false,
    val listReview: MutableList<Review> = mutableListOf(),
)
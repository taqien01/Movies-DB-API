package com.example.movieapi.model

import com.google.gson.annotations.SerializedName

data class AuthorReview (
    @SerializedName("name") var name : String? = null,
    @SerializedName("username") var username : String? = null,
    @SerializedName("avatar_path") var avatarPath : String? = null,
    @SerializedName("rating") var rating : String? = null
)
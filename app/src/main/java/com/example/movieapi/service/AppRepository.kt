package com.example.movieapi.service

import com.example.movieapi.BuildConfig
import com.example.movieapi.model.ListGenre
import com.example.movieapi.model.ListMovies
import com.example.movieapi.model.ListReview
import retrofit2.Response
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

class AppRepository() {
    private val appService = AppService.appInterface

    suspend fun getGenres(
        token: String = "Bearer ${BuildConfig.ACCESS_TOKEN}",
        language: String = "en",
    ): Response<ListGenre> =
        appService.getGenres(token, language)

    suspend fun getMovies(
        token: String = "Bearer ${BuildConfig.ACCESS_TOKEN}",
        page: Int = 1,
        genres: String,
    ): Response<ListMovies> =
        appService.getMoviesList(token, page, genres)

    suspend fun getUserReview(
        token: String = "Bearer ${BuildConfig.ACCESS_TOKEN}",
        page: Int = 1,
        movieId: String = "",
        language: String = "en-US"
    ): Response<ListReview> =
        appService.getUserReview(token, movieId, page, language)

}
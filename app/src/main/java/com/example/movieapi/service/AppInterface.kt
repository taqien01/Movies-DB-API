package com.example.movieapi.service

import com.example.movieapi.model.ListGenre
import com.example.movieapi.model.ListMovies
import com.example.movieapi.model.ListReview
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

interface AppInterface {

    //get genres
    @GET("genre/movie/list")
    suspend fun getGenres(
        @Header("Authorization") token: String,
        @Query("language") language: String,
    ): Response<ListGenre>

    @GET("discover/movie")
    suspend fun getMoviesList(
        @Header("Authorization") token: String,
        @Query("page") page: Int,
        @Query("with_genres") genres: String,
    ): Response<ListMovies>

    @GET("movie/{movie_id}/reviews")
    suspend fun getUserReview(
        @Header("Authorization") token: String,
        @Path("movie_id") movieId: String,
        @Query("page") page: Int,
        @Query("language") language: String,
    ): Response<ListReview>
}
package com.example.movieexplorerapp.data.api

import com.example.movieexplorerapp.model.Movie
import retrofit2.http.GET

interface MovieApi {

    @GET("Movies")
    suspend fun getAllMovies(): List<Movie>
}
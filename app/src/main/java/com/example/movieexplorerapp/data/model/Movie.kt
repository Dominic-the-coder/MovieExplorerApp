package com.example.movieexplorerapp.model

import com.google.gson.annotations.SerializedName

data class Movie(
    val id: Int,
    val title: String,
    val director: String,
    val overview: String,
    val length: Int,
    val rating: Double,

    @SerializedName("release_date")
    val releaseDate: String
)

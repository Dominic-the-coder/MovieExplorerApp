package com.example.movieexplorerapp.data.api

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {
    private const val BASE_URL = "https://fcapi-1y70.onrender.com/"
    private val logger = Logger()
    private val client = OkHttpClient().newBuilder().addInterceptor(logger).build()

    val api: MovieApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
            .create(MovieApi::class.java)
    }
}
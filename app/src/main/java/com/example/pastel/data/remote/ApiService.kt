package com.example.pastel.data.remote

import com.example.pastel.data.remote.model.NewsResponse
import com.example.pastel.util.Constants.API_KEY
import retrofit2.http.GET
import retrofit2.http.Headers

interface ApiService {

    @Headers("X-Api-Key: $API_KEY")
    @GET("top-headlines?country=us")
    suspend fun getNews(): NewsResponse
}
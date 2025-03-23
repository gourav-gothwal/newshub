package com.example.testapp.api


import com.example.testapp.models.NewsResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApiService {
    @GET("news")
    fun getNews(
        @Query("apikey") apiKey: String,
        @Query("q") query: String,
        @Query("language") language: String = "en"
    ): Call<NewsResponse>
}

package com.example.testapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.testapp.api.RetrofitClient
import com.example.testapp.models.Article
import com.example.testapp.models.NewsResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NewsViewModel : ViewModel() {
    private val _news = MutableLiveData<List<Article>>()
    val news: LiveData<List<Article>> = _news

    private val _breakingNews = MutableLiveData<List<Article>>()
    val breakingNews: LiveData<List<Article>> = _breakingNews

    private val errorMessage = MutableLiveData<String>()
    val errorMessageLiveData: LiveData<String> = errorMessage

    fun fetchNews(category: String) {
        val apiKey = "pub_75164204e6fdb5da7323b4d81b4797a6a0306"
        val categoryQuery = if (category == "All") "" else category.lowercase()

        RetrofitClient.instance.getNews(apiKey, categoryQuery, "en")
            .enqueue(object : Callback<NewsResponse> {
                override fun onResponse(call: Call<NewsResponse>, response: Response<NewsResponse>) {
                    if (response.isSuccessful) {
                        _news.value = response.body()?.results ?: emptyList()
                    } else {
                        errorMessage.value = "Failed to load news: ${response.message()}"
                    }
                }

                override fun onFailure(call: Call<NewsResponse>, t: Throwable) {
                    errorMessage.value = "Network error: ${t.message}"
                    _news.value = emptyList()
                }
            })
    }

    fun fetchBreakingNews() {
        val apiKey = "pub_75164204e6fdb5da7323b4d81b4797a6a0306"

        RetrofitClient.instance.getNews(apiKey, "en")
            .enqueue(object : Callback<NewsResponse> {
                override fun onResponse(call: Call<NewsResponse>, response: Response<NewsResponse>) {
                    if (response.isSuccessful) {
                        _breakingNews.value = response.body()?.results ?: emptyList()
                    }
                }

                override fun onFailure(call: Call<NewsResponse>, t: Throwable) {
                    errorMessage.value = "Network error: ${t.message}"
                    _breakingNews.value = emptyList()
                }

            })
    }

    fun searchNews(query: String) {
        val apiKey = "pub_75164204e6fdb5da7323b4d81b4797a6a0306"

        RetrofitClient.instance.searchNews(apiKey, query, "en")
            .enqueue(object : Callback<NewsResponse> {
                override fun onResponse(call: Call<NewsResponse>, response: Response<NewsResponse>) {
                    if (response.isSuccessful) {
                        _news.value = response.body()?.results ?: emptyList()
                    }
                }

                override fun onFailure(call: Call<NewsResponse>, t: Throwable) {
                    errorMessage.value = "Search failed: ${t.message}"
                    _news.value = emptyList()
                }
            })
    }
}

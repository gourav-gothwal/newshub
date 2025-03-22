package com.example.testapp.models

data class NewsResponse(
    val status: String,
    val totalResults: Int,
    val results: List<Article> // "results" instead of "articles"
)
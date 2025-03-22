package com.example.testapp.models

data class Article(
    val title: String?,
    val description: String?,
    val link: String?, // URL of the news
    val image_url: String?, // Image link
    val source_id: String?, // News source
    val pubDate: String? // Published date
)
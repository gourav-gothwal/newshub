package com.example.testapp.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "bookmarked_articles")
data class BookmarkedArticle(
    @PrimaryKey val id: String,
    val title: String,
    val url: String,
    val imageUrl: String?,
    val publishedAt: String,
    val sourceId: String?,
    val description: String?
)


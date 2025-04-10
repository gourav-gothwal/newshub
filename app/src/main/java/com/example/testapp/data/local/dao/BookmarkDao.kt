package com.example.testapp.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.testapp.data.local.entities.BookmarkedArticle
import kotlinx.coroutines.flow.Flow

@Dao
interface BookmarkDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBookmark(article: BookmarkedArticle)

    @Delete
    suspend fun deleteBookmark(article: BookmarkedArticle)

    @Query("SELECT * FROM bookmarked_articles")
    fun getAllBookmarks(): Flow<List<BookmarkedArticle>>  // Return Flow

    @Query("SELECT * FROM bookmarked_articles WHERE id = :articleId LIMIT 1")
    suspend fun getBookmarkById(articleId: String): BookmarkedArticle?
}

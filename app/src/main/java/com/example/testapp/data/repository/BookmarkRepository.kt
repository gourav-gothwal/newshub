package com.example.testapp.data.repository

import com.example.testapp.data.local.dao.BookmarkDao
import com.example.testapp.data.local.entities.BookmarkedArticle
import kotlinx.coroutines.flow.Flow

class BookmarkRepository(private val bookmarkDao: BookmarkDao) {

    suspend fun addBookmark(article: BookmarkedArticle) {
        bookmarkDao.insertBookmark(article)
    }

    suspend fun removeBookmark(article: BookmarkedArticle) {
        bookmarkDao.deleteBookmark(article)
    }

    fun getBookmarks(): Flow<List<BookmarkedArticle>> {  // Return Flow instead of List
        return bookmarkDao.getAllBookmarks()
    }

    suspend fun isArticleBookmarked(articleId: String): BookmarkedArticle? {
        return bookmarkDao.getBookmarkById(articleId)
    }
}


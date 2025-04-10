package com.example.testapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.testapp.data.local.entities.BookmarkedArticle
import com.example.testapp.data.repository.BookmarkRepository
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.Flow

class BookmarkViewModel(private val repository: BookmarkRepository) : ViewModel() {

    fun addBookmark(article: BookmarkedArticle) {
        viewModelScope.launch {
            repository.addBookmark(article)
        }
    }

    fun removeBookmark(article: BookmarkedArticle) {
        viewModelScope.launch {
            repository.removeBookmark(article)
        }
    }

    fun getBookmarks(): Flow<List<BookmarkedArticle>> {
        return repository.getBookmarks()
    }

    suspend fun isArticleBookmarked(articleId: String): BookmarkedArticle? {
        return repository.isArticleBookmarked(articleId)
    }
}

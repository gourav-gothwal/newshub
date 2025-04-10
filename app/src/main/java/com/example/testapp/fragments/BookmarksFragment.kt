package com.example.testapp.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.testapp.ArticlePage
import com.example.testapp.R
import com.example.testapp.adapters.BookmarkAdapter
import com.example.testapp.data.local.BookmarkDatabase
import com.example.testapp.data.local.entities.BookmarkedArticle
import com.example.testapp.data.repository.BookmarkRepository
import com.example.testapp.viewmodel.BookmarkViewModel
import kotlinx.coroutines.launch

class BookmarksFragment : Fragment() {

    private lateinit var bookmarkViewModel: BookmarkViewModel
    private lateinit var bookmarksAdapter: BookmarkAdapter
    private val bookmarkedArticles = mutableListOf<BookmarkedArticle>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_bookmarks, container, false)

        // Initialize Room Database and ViewModel
        val bookmarkDatabase = BookmarkDatabase.getDatabase(requireContext())
        val repository = BookmarkRepository(bookmarkDatabase.bookmarkDao())
        bookmarkViewModel = BookmarkViewModel(repository)

        // Setup RecyclerView
        val recyclerView = view.findViewById<RecyclerView>(R.id.bookmarks_recycler_view)
        bookmarksAdapter = BookmarkAdapter(
            bookmarkedArticles,
            onItemClick = { article -> openArticlePage(article) },
            onRemoveClick = { article -> removeBookmark(article) }
        )

        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = bookmarksAdapter

        // Fetch and observe bookmarks
        fetchBookmarks()

        return view
    }

    private fun fetchBookmarks() {
        lifecycleScope.launch {
            bookmarkViewModel.getBookmarks().collect { bookmarks ->
                bookmarkedArticles.clear()
                bookmarkedArticles.addAll(bookmarks)
                bookmarksAdapter.notifyDataSetChanged()
            }
        }
    }

    private fun openArticlePage(article: BookmarkedArticle) {
        val intent = Intent(requireContext(), ArticlePage::class.java).apply {
            putExtra("ARTICLE_ID", article.id)
            putExtra("TITLE", article.title)
            putExtra("IMAGE_URL", article.imageUrl ?: "")
            putExtra("AUTHOR", article.sourceId ?: "Unknown")
            putExtra("CONTENT", article.description ?: "No Content Available")
            putExtra("TIME", article.publishedAt ?: "Not Available")
            putExtra("URL", article.url)
        }
        startActivity(intent)
    }


    private fun removeBookmark(article: BookmarkedArticle) {
        lifecycleScope.launch {
            bookmarkViewModel.removeBookmark(article)
        }
    }
}

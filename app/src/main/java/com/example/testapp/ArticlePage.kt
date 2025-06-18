package com.example.testapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.testapp.data.local.BookmarkDatabase
import com.example.testapp.data.local.entities.BookmarkedArticle
import com.example.testapp.data.repository.BookmarkRepository
import com.example.testapp.viewmodel.BookmarkViewModel
import com.example.testapp.viewmodel.BookmarkViewModelFactory
import kotlinx.coroutines.launch

class ArticlePage : AppCompatActivity() {

    private lateinit var articleImage: ImageView
    private lateinit var articleHeadline: TextView
    private lateinit var articleSource: TextView
    private lateinit var articleTime: TextView
    private lateinit var articleContent: TextView
    private lateinit var backButton: ImageView
    private lateinit var bookmarkButton: ImageView
    private lateinit var shareButton: ImageView

    private var currentArticle: BookmarkedArticle? = null
    private var isBookmarked = false

    // ViewModel initialization using ViewModelFactory
    private val bookmarkViewModel: BookmarkViewModel by viewModels {
        BookmarkViewModelFactory(BookmarkRepository(BookmarkDatabase.getDatabase(this).bookmarkDao()))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_article_page)

        // Initialize views
        articleImage = findViewById(R.id.imageView3)
        articleHeadline = findViewById(R.id.textView20)
        articleSource = findViewById(R.id.textView22)
        articleTime = findViewById(R.id.textView21)
        articleContent = findViewById(R.id.articleContent)
        backButton = findViewById(R.id.imageView)
        bookmarkButton = findViewById(R.id.imageView4)
        shareButton = findViewById(R.id.imageView7)

        // ✅ Corrected intent data retrieval
        val articleId = intent.getStringExtra("ARTICLE_ID") ?: return
        val title = intent.getStringExtra("TITLE") ?: "No Title"
        val content = intent.getStringExtra("CONTENT") ?: "No Content Available"
        val imageUrl = intent.getStringExtra("IMAGE_URL") ?: ""
        val url = intent.getStringExtra("URL") ?: ""
        val sourceId = intent.getStringExtra("AUTHOR") ?: "Unknown"
        val pubDate = intent.getStringExtra("TIME") ?: "Not Available"

        // ✅ Debugging log to check received data
        Log.d("ArticlePage", "Received - ID: $articleId, Title: $title, Source: $sourceId, Date: $pubDate, Content: $content, Image: $imageUrl")

        // ✅ Create `BookmarkedArticle` object correctly
        currentArticle = BookmarkedArticle(
            id = articleId,
            title = title,
            url = url,
            imageUrl = imageUrl,
            publishedAt = pubDate,
            sourceId = sourceId,
            description = content
        )

        // Set data to views
        articleHeadline.text = title
        articleSource.text = "By $sourceId"
        articleTime.text = pubDate
        articleContent.text = content

        // Load image using Glide
        Glide.with(this).load(imageUrl).into(articleImage)

        // ✅ Check if article is already bookmarked
        lifecycleScope.launch {
            val bookmarkedArticle = bookmarkViewModel.isArticleBookmarked(articleId)
            isBookmarked = bookmarkedArticle != null
            Log.d("ArticlePage", "Is bookmarked: $isBookmarked")

            runOnUiThread { updateBookmarkIcon() } // Ensure UI updates inside coroutine
        }

        // Handle bookmark button click
        bookmarkButton.setOnClickListener {
            toggleBookmark()
        }

        shareButton.setOnClickListener{
            shareArticle()
        }

        // Handle back button click
        backButton.setOnClickListener {
            finish()
        }
    }

    private fun toggleBookmark() {
        currentArticle?.let { article ->
            lifecycleScope.launch {
                if (isBookmarked) {
                    bookmarkViewModel.removeBookmark(article)
                    isBookmarked = false
                } else {
                    bookmarkViewModel.addBookmark(article)
                    isBookmarked = true
                }
                Log.d("ArticlePage", "Bookmark Toggled: $isBookmarked")

                runOnUiThread { updateBookmarkIcon() }
            }
        }
    }

    private fun updateBookmarkIcon() {
        bookmarkButton.setImageResource(
            if (isBookmarked) R.drawable.baseline_bookmark_24
            else R.drawable.outline_bookmark_border_24
        )
    }

    private fun shareArticle() {
        currentArticle?.let { article ->
            val shareText = "${article.title}\n\nRead more at: ${article.url}"
            val shareIntent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, shareText)
                type = "text/plain"
            }
            startActivity(Intent.createChooser(shareIntent, "Share article via"))
        }
    }
}

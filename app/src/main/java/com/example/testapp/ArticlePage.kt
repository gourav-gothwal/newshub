package com.example.testapp

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide


class ArticlePage : AppCompatActivity() {
    private lateinit var articleImage: ImageView
    private lateinit var articleHeadline: TextView
    private lateinit var articleAuthor: TextView
    private lateinit var articleTime: TextView
    private lateinit var articleContent: TextView
    private lateinit var backButton: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_article_page)

        articleImage = findViewById(R.id.imageView3)
        articleHeadline = findViewById(R.id.textView20)
        articleAuthor = findViewById(R.id.textView22)
        articleTime = findViewById(R.id.textView21)
        articleContent = findViewById(R.id.articleContent)
        backButton = findViewById(R.id.imageView)

        // Get intent data
        val headline = intent.getStringExtra("headline") ?: "No Title"
        val imageUrl = intent.getStringExtra("image") ?: ""
        val author = intent.getStringExtra("author") ?: "Unknown"
        val time = intent.getStringExtra("time") ?: ""
        val content = intent.getStringExtra("content") ?: "No Content Available"

        // Set data to views
        articleHeadline.text = headline
        articleAuthor.text = "By $author"
        articleTime.text = time
        articleContent.text = content

        // Load image using Glide
        Glide.with(this).load(imageUrl).into(articleImage)

        // Handle back button click
        backButton.setOnClickListener {
            finish()
        }
    }
}

package com.example.testapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.testapp.R
import com.example.testapp.models.Article

class NewsAdapter(
    private var newsList: List<Article>,
    private val onItemClick: (Article) -> Unit
) : RecyclerView.Adapter<NewsAdapter.NewsViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.news_item, parent, false)
        return NewsViewHolder(view)
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        holder.bind(newsList[position], onItemClick)
    }

    override fun getItemCount(): Int = newsList.size

    fun updateData(newNewsList: List<Article>) {
        newsList = newNewsList
        notifyDataSetChanged()
    }

    class NewsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val title: TextView = itemView.findViewById(R.id.newsTitle)
        private val author: TextView = itemView.findViewById(R.id.newsAuthor)
        private val imageView: ImageView = itemView.findViewById(R.id.newsImage)

        fun bind(article: Article, onItemClick: (Article) -> Unit) {
            // Set title
            title.text = article.title

            // Set author with fallback
            author.text = article.source_id?.takeIf { it.isNotBlank() } ?: "Unknown Author"

            // Handle image loading
            if (article.image_url.isNullOrEmpty()) {
                imageView.visibility = View.GONE
            } else {
                imageView.visibility = View.VISIBLE
                Glide.with(itemView.context)
                    .load(article.image_url)
                    .placeholder(R.drawable.placeholder_image) // Optional: add a placeholder
                    .error(R.drawable.error_image) // Optional: add an error image
                    .into(imageView)
            }

            // Set click listener
            itemView.setOnClickListener { onItemClick(article) }
        }
    }
}
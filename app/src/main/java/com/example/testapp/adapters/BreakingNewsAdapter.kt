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

class BreakingNewsAdapter(
    private val articles: List<Article>,
    private val onItemClick: (Article) -> Unit
) : RecyclerView.Adapter<BreakingNewsAdapter.NewsViewHolder>() {

    inner class NewsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val newsImage: ImageView = itemView.findViewById(R.id.newsImage)
        private val newsTitle: TextView = itemView.findViewById(R.id.newsTitle)

        fun bind(article: Article) {
            newsTitle.text = article.title

            // Glide with improved error and fallback handling
            Glide.with(itemView.context)
                .load(article.image_url)
                .placeholder(R.drawable.sample_image) // Placeholder while loading
                .error(R.drawable.sample_image) // Error image in case of failure
                .centerCrop() // Ensures image scales properly
                .into(newsImage)

            // Handle item click to open the article
            itemView.setOnClickListener { onItemClick(article) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_breaking_news_pager, parent, false)
        return NewsViewHolder(view)
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        holder.bind(articles[position])
    }

    override fun getItemCount() = articles.size
}

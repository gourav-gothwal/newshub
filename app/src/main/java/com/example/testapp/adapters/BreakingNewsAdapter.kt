package com.example.testapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.testapp.R
import com.example.testapp.models.Article

class BreakingNewsAdapter(
    private val onItemClick: (Article) -> Unit
) : ListAdapter<Article, BreakingNewsAdapter.BreakingNewsViewHolder>(BreakingNewsDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BreakingNewsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_breaking_news_card, parent, false)
        return BreakingNewsViewHolder(view)
    }

    override fun onBindViewHolder(holder: BreakingNewsViewHolder, position: Int) {
        holder.bind(getItem(position), onItemClick)
    }

    class BreakingNewsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val newsImage: ImageView = itemView.findViewById(R.id.newsImage)
        private val newsTitle: TextView = itemView.findViewById(R.id.newsTitle)
        private val newsSource: TextView = itemView.findViewById(R.id.newsSource)

        fun bind(article: Article, onItemClick: (Article) -> Unit) {
            newsTitle.text = article.title
            newsSource.text = article.source_id ?: "Unknown Source"

            // Load image with Glide
            Glide.with(itemView.context)
                .load(article.image_url)
                .placeholder(R.drawable.sample_image)
                .error(R.drawable.sample_image)
                .centerCrop()
                .into(newsImage)

            // Set click listener
            itemView.setOnClickListener { onItemClick(article) }
        }
    }
}

class BreakingNewsDiffCallback : DiffUtil.ItemCallback<Article>() {
    override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
        return oldItem == newItem
    }
}

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

class NewsAdapter(private var articles: List<Article>) :
    RecyclerView.Adapter<NewsAdapter.NewsViewHolder>() {

    class NewsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val newsImage: ImageView = itemView.findViewById(R.id.newsImage)
        val newsTitle: TextView = itemView.findViewById(R.id.newsTitle)
        val newsDescription: TextView = itemView.findViewById(R.id.newsDescription)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.news_item, parent, false)
        return NewsViewHolder(view)
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        val article = articles[position]
        holder.newsTitle.text = article.title ?: "No title available"
        holder.newsDescription.text = article.description ?: "No description available"

        // Check if the image URL is empty or null
        if (!article.image_url.isNullOrEmpty()) {
            holder.newsImage.visibility = View.VISIBLE
            Glide.with(holder.itemView.context)
                .load(article.image_url) // Updated from urlToImage to image_url
                .into(holder.newsImage)
        } else {
            holder.newsImage.visibility = View.GONE // Hide ImageView if no image available
        }
    }

    override fun getItemCount(): Int = articles.size

    // Function to update the data dynamically
    fun updateData(newArticles: List<Article>) {
        articles = newArticles
        notifyDataSetChanged()
    }
}

package com.example.testapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.testapp.R
import com.example.testapp.models.Article
import java.text.SimpleDateFormat
import java.util.Locale

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
        newsList = newNewsList.toMutableList() // Use mutable list for more efficient updates
        val diffCallback = NewsDiffCallback(newsList, newNewsList)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        diffResult.dispatchUpdatesTo(this)
    }

    class NewsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val title: TextView = itemView.findViewById(R.id.newsTitle)
        private val author: TextView = itemView.findViewById(R.id.newsAuthor)
        private val imageCard: CardView = itemView.findViewById(R.id.imageCard)
        private val imageView: ImageView = itemView.findViewById(R.id.newsImage)
        private val publishDate: TextView = itemView.findViewById(R.id.publishDate)

        private val dateFormatter = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())

        fun bind(article: Article, onItemClick: (Article) -> Unit) {
            title.text = article.title
            author.text = article.source_id?.takeIf { it.isNotBlank() } ?: "Unknown Author"

            // Handle publish date safely
            val formattedDate = try {
                val date = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault()).parse(article.pubDate)
                date?.let { dateFormatter.format(it) } ?: article.pubDate
            } catch (e: Exception) {
                article.pubDate
            }
            publishDate.apply {
                text = formattedDate
                visibility = if (formattedDate.isNullOrEmpty()) View.GONE else View.VISIBLE
            }

            // Handle image loading
            imageCard.visibility = if (!article.image_url.isNullOrEmpty()) View.VISIBLE else View.GONE
            if (imageCard.visibility == View.VISIBLE) {
                Glide.with(itemView.context)
                    .load(article.image_url)
                    .placeholder(R.drawable.placeholder_image)
                    .error(R.drawable.placeholder_image)
                    .centerCrop()
                    .into(imageView)
            }

            // Handle item click
            itemView.setOnClickListener { onItemClick(article) }
        }
    }

    class NewsDiffCallback(
        private val oldList: List<Article>,
        private val newList: List<Article>
    ) : DiffUtil.Callback() {

        override fun getOldListSize(): Int = oldList.size
        override fun getNewListSize(): Int = newList.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition].id == newList[newItemPosition].id
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition] == newList[newItemPosition]
        }
    }
}

// RecyclerView setup for horizontal scrolling
fun setupHorizontalRecyclerView(recyclerView: RecyclerView, adapter: NewsAdapter) {
    recyclerView.layoutManager = LinearLayoutManager(recyclerView.context, LinearLayoutManager.HORIZONTAL, false)
    recyclerView.adapter = adapter
}

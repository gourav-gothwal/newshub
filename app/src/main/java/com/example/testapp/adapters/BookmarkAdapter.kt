package com.example.testapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.testapp.R
import com.example.testapp.data.local.entities.BookmarkedArticle

class BookmarkAdapter(
    private val bookmarkedArticles: List<BookmarkedArticle>,
    private val onItemClick: (BookmarkedArticle) -> Unit,  // Click listener
    private val onRemoveClick: (BookmarkedArticle) -> Unit // Remove bookmark listener
) : RecyclerView.Adapter<BookmarkAdapter.BookmarkViewHolder>() {

    inner class BookmarkViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val titleTextView: TextView = itemView.findViewById(R.id.bookmark_title)
        private val imageView: ImageView = itemView.findViewById(R.id.bookmark_image)
        private val removeButton: ImageView = itemView.findViewById(R.id.remove_bookmark)

        fun bind(article: BookmarkedArticle) {
            titleTextView.text = article.title
            Glide.with(itemView.context).load(article.imageUrl).into(imageView)

            itemView.setOnClickListener { onItemClick(article) }
            removeButton.setOnClickListener { onRemoveClick(article) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookmarkViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_bookmark, parent, false)
        return BookmarkViewHolder(view)
    }

    override fun onBindViewHolder(holder: BookmarkViewHolder, position: Int) {
        holder.bind(bookmarkedArticles[position])
    }

    override fun getItemCount(): Int = bookmarkedArticles.size
}

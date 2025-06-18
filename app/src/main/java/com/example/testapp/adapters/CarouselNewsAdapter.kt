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

class CarouselNewsAdapter(
    private val articles: List<Article>,
    private val onItemClick: (Article) -> Unit
) : RecyclerView.Adapter<CarouselNewsAdapter.CarouselViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarouselViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_carousel_news, parent, false)
        return CarouselViewHolder(view)
    }

    override fun onBindViewHolder(holder: CarouselViewHolder, position: Int) {
        holder.bind(articles[position])
    }

    override fun getItemCount(): Int = articles.size

    inner class CarouselViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val image: ImageView = itemView.findViewById(R.id.carouselImage)
        private val title: TextView = itemView.findViewById(R.id.carouselTitle)

        fun bind(article: Article) {
            title.text = article.title

            // Check if image_url is valid and load image using Glide
            if (!article.image_url.isNullOrEmpty()) {
                Glide.with(itemView.context)
                    .load(article.image_url)
                    .placeholder(R.drawable.sample_image) // Placeholder when image is loading or missing
                    .error(R.drawable.sample_image) // Error image if loading fails
                    .centerCrop() // Ensures image maintains a good aspect ratio
                    .into(image)
            } else {
                // Optionally handle case when there is no image URL
                image.setImageResource(R.drawable.sample_image) // Use a default placeholder
            }

            // Set click listener to handle item click
            itemView.setOnClickListener { onItemClick(article) }
        }
    }
}

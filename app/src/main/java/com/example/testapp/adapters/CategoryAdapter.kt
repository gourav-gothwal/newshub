package com.example.testapp.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import com.example.testapp.R

class CategoryAdapter(
    private val categories: List<String>,
    private val listener: OnCategorySelectedListener?
) : RecyclerView.Adapter<CategoryAdapter.ViewHolder>() {

    private var selectedPosition = 0 // Default selected position

    interface OnCategorySelectedListener {
        fun onCategorySelected(category: String, position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_category_button, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val category = categories[position]
        holder.button.text = category

        // Set selected state
        holder.button.isSelected = position == selectedPosition

        holder.button.setOnClickListener {
            if (selectedPosition != position) {
                val previousSelected = selectedPosition
                selectedPosition = position

                // Update the previous selected and new selected items
                notifyItemChanged(previousSelected)
                notifyItemChanged(selectedPosition)

                listener?.onCategorySelected(category, position)
            }
        }
    }

    override fun getItemCount(): Int = categories.size

    // Method to set the selected position programmatically
    fun setSelectedPosition(position: Int) {
        if (position >= 0 && position < categories.size && position != selectedPosition) {
            val previousSelected = selectedPosition
            selectedPosition = position
            notifyItemChanged(previousSelected)
            notifyItemChanged(selectedPosition)
        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val button: Button = itemView.findViewById(R.id.btnCategory)
    }
}
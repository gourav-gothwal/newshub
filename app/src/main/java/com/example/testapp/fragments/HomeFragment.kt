package com.example.testapp.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.testapp.ArticlePage
import com.example.testapp.R
import com.example.testapp.adapters.NewsAdapter
import com.example.testapp.adapters.CategoryAdapter
import com.example.testapp.api.RetrofitClient
import com.example.testapp.models.Article
import com.example.testapp.models.NewsResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var recyclerViewCategories: RecyclerView
    private lateinit var newsAdapter: NewsAdapter
    private lateinit var categoryAdapter: CategoryAdapter
    private lateinit var progressBar: ProgressBar
    private var newsList: List<Article> = listOf()
    private val categories = listOf("All", "Technology", "Sports", "Business", "Health", "Entertainment", "Science")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.recyclerView)
        recyclerViewCategories = view.findViewById(R.id.recyclerViewCategories)
        progressBar = view.findViewById(R.id.progressBar)

        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // Initialize NewsAdapter with empty list
        newsAdapter = NewsAdapter(newsList) { newsItem ->
            openArticlePage(newsItem)
        }
        recyclerView.adapter = newsAdapter

        // Initialize CategoryAdapter with the OnCategorySelectedListener interface
        recyclerViewCategories.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        categoryAdapter = CategoryAdapter(categories, object : CategoryAdapter.OnCategorySelectedListener {
            override fun onCategorySelected(category: String, position: Int) {
                fetchNews(category)
                Toast.makeText(requireContext(), "Selected: $category", Toast.LENGTH_SHORT).show()
            }
        })

        recyclerViewCategories.adapter = categoryAdapter

        // Fetch default news (All or Technology)
        fetchNews("Technology")
    }

    private fun fetchNews(category: String) {
        progressBar.visibility = View.VISIBLE

        val apiKey = getString(R.string.api_key)
        val categoryQuery = if (category == "All") "" else category.lowercase()

        RetrofitClient.instance.getNews(apiKey, categoryQuery, "en")
            .enqueue(object : Callback<NewsResponse> {
                override fun onResponse(call: Call<NewsResponse>, response: Response<NewsResponse>) {
                    progressBar.visibility = View.GONE
                    if (response.isSuccessful) {
                        newsList = response.body()?.results ?: emptyList()
                        newsAdapter.updateData(newsList)
                        recyclerView.visibility = View.VISIBLE
                    } else {
                        Toast.makeText(requireContext(), "Failed to load news", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<NewsResponse>, t: Throwable) {
                    progressBar.visibility = View.GONE
                    Toast.makeText(requireContext(), "Network error: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
    }

    private fun openArticlePage(newsItem: Article) {
        val intent = Intent(requireContext(), ArticlePage::class.java).apply {
            putExtra("headline", newsItem.title)
            putExtra("image", newsItem.image_url)
            putExtra("author", newsItem.source_id)
            putExtra("content", newsItem.description)
            putExtra("time", newsItem.pubDate)
        }
        startActivity(intent)
    }
}
package com.example.testapp.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
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
import com.google.firebase.auth.FirebaseAuth
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var recyclerViewCategories: RecyclerView
    private lateinit var newsAdapter: NewsAdapter
    private lateinit var categoryAdapter: CategoryAdapter
    private lateinit var progressBar: ProgressBar
    private lateinit var welcomeTextView: TextView
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

        // Initialize Views
        welcomeTextView = view.findViewById(R.id.textViewWelcome)
        recyclerView = view.findViewById(R.id.recyclerView)
        recyclerViewCategories = view.findViewById(R.id.recyclerViewCategories)
        progressBar = view.findViewById(R.id.progressBar)

        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // Display Welcome Message with User's Name
        val user = FirebaseAuth.getInstance().currentUser
        val userName = user?.displayName ?: "User"
        welcomeTextView.text = "Welcome, $userName 👋"

        // Initialize NewsAdapter with empty list
        newsAdapter = NewsAdapter(newsList) { newsItem ->
            openArticlePage(newsItem)
        }
        recyclerView.adapter = newsAdapter

        // Initialize CategoryAdapter without toast
        recyclerViewCategories.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        categoryAdapter = CategoryAdapter(categories, object : CategoryAdapter.OnCategorySelectedListener {
            override fun onCategorySelected(category: String, position: Int) {
                fetchNews(category)
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
                        val articles = response.body()?.results ?: emptyList()

                        // Ensure `source_id` is not null and filter unique articles by title + source
                        val uniqueArticles = articles.distinctBy { "${it.title}-${it.source_id ?: "unknown"}" }

                        if (uniqueArticles.isNotEmpty()) {
                            newsAdapter.updateData(uniqueArticles)
                            recyclerView.visibility = View.VISIBLE
                        } else {
                            recyclerView.visibility = View.GONE
                        }
                    }
                }

                override fun onFailure(call: Call<NewsResponse>, t: Throwable) {
                    progressBar.visibility = View.GONE
                }
            })
    }

    private fun openArticlePage(newsItem: Article) {
        val intent = Intent(requireContext(), ArticlePage::class.java).apply {
            putExtra("ARTICLE_ID", newsItem.source_id)
            putExtra("TITLE", newsItem.title)
            putExtra("IMAGE_URL", newsItem.image_url)
            putExtra("AUTHOR", newsItem.source_id)
            putExtra("CONTENT", newsItem.description)
            putExtra("TIME", newsItem.pubDate)
            putExtra("URL", newsItem.link)
        }
        startActivity(intent)
    }
}

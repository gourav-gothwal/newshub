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
import androidx.viewpager2.widget.ViewPager2
import com.example.testapp.ArticlePage
import com.example.testapp.R
import com.example.testapp.adapters.NewsAdapter
import com.example.testapp.adapters.CategoryAdapter
import com.example.testapp.adapters.CarouselNewsAdapter
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
    private lateinit var viewPager: ViewPager2
    private lateinit var newsAdapter: NewsAdapter
    private lateinit var categoryAdapter: CategoryAdapter
    private lateinit var progressBar: ProgressBar
    private lateinit var welcomeTextView: TextView

    private var newsList: List<Article> = listOf()
    private val categories = listOf("All", "Technology", "Sports", "Business", "Health", "Entertainment", "Science")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.fragment_home, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        welcomeTextView = view.findViewById(R.id.textViewWelcome)
        recyclerView = view.findViewById(R.id.recyclerView)
        recyclerViewCategories = view.findViewById(R.id.recyclerViewCategories)
        viewPager = view.findViewById(R.id.viewPagerBreakingNews)
        progressBar = view.findViewById(R.id.progressBar)

        // Setup Carousel Effect
        viewPager.clipToPadding = false
        viewPager.clipChildren = false
        viewPager.offscreenPageLimit = 3
        viewPager.getChildAt(0).overScrollMode = RecyclerView.OVER_SCROLL_NEVER

        val compositePageTransformer = androidx.viewpager2.widget.CompositePageTransformer()
        compositePageTransformer.addTransformer(androidx.viewpager2.widget.MarginPageTransformer(30))
        compositePageTransformer.addTransformer { page, position ->
            val r = 1 - kotlin.math.abs(position)
            page.scaleY = 0.85f + r * 0.15f
        }
        viewPager.setPageTransformer(compositePageTransformer)

        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerViewCategories.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        // Show user's name
        val userName = FirebaseAuth.getInstance().currentUser?.displayName ?: "User"
        welcomeTextView.text = "Welcome, $userName 👋"

        // Setup Adapters
        newsAdapter = NewsAdapter(newsList) { openArticlePage(it) }
        recyclerView.adapter = newsAdapter

        categoryAdapter = CategoryAdapter(categories, object : CategoryAdapter.OnCategorySelectedListener {
            override fun onCategorySelected(category: String, position: Int) {
                fetchNews(category)
            }
        })
        recyclerViewCategories.adapter = categoryAdapter

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
                        val uniqueArticles = articles.distinctBy { "${it.title}-${it.source_id ?: "unknown"}" }

                        // Setup carousel with first 5 random items
                        val carouselList = uniqueArticles.take(5)
                        val carouselAdapter = CarouselNewsAdapter(carouselList) { openArticlePage(it) }
                        viewPager.adapter = carouselAdapter

                        // Setup main list
                        val recentNewsList = if (uniqueArticles.size > 5) uniqueArticles.drop(5) else emptyList()
                        newsAdapter.updateData(recentNewsList)
                        
                        // Toggle visibility of Recent News section
                        val recentVisibility = if (recentNewsList.isNotEmpty()) View.VISIBLE else View.GONE
                        recyclerView.visibility = recentVisibility
                        view?.findViewById<View>(R.id.recommendedHeader)?.visibility = recentVisibility

                        if (uniqueArticles.isEmpty()) {
                            // Show placeholder text or empty state message (optional)
                            recyclerView.visibility = View.GONE
                            view?.findViewById<View>(R.id.recommendedHeader)?.visibility = View.GONE
                        }
                    }
                }

                override fun onFailure(call: Call<NewsResponse>, t: Throwable) {
                    progressBar.visibility = View.GONE
                    // Show error message or retry button
                }
            })
    }

    private fun openArticlePage(article: Article) {
        val intent = Intent(requireContext(), ArticlePage::class.java).apply {
            putExtra("ARTICLE_ID", article.source_id)
            putExtra("TITLE", article.title)
            putExtra("IMAGE_URL", article.image_url)
            putExtra("AUTHOR", article.source_id)
            putExtra("CONTENT", article.description)
            putExtra("TIME", article.pubDate)
            putExtra("URL", article.link)
        }
        startActivity(intent)
    }
}

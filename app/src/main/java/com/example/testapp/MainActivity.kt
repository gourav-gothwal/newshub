package com.example.testapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.testapp.adapters.NewsAdapter
import com.example.testapp.api.RetrofitClient
import com.example.testapp.models.NewsResponse
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var recyclerView: RecyclerView
    private lateinit var newsAdapter: NewsAdapter
    private lateinit var progressBar: ProgressBar
    private var currentUser: FirebaseUser? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        auth = FirebaseAuth.getInstance()
        currentUser = auth.currentUser

        // Check if user is logged in, otherwise redirect to login
        if (currentUser == null) {
            redirectToLogin()
            return
        }

        Toast.makeText(this, "Welcome ${currentUser?.displayName ?: currentUser?.email}", Toast.LENGTH_SHORT).show()

        // Initialize Views
        recyclerView = findViewById(R.id.recyclerView)
        progressBar = findViewById(R.id.progressBar)

        recyclerView.layoutManager = LinearLayoutManager(this)
        newsAdapter = NewsAdapter(emptyList())
        recyclerView.adapter = newsAdapter

        fetchNews()
    }

    private fun fetchNews() {
        progressBar.visibility = View.VISIBLE

        val apiKey = getString(R.string.api_key) // Fetch API Key from strings.xml

        RetrofitClient.instance.getNews(apiKey, "technology", "en")
            .enqueue(object : Callback<NewsResponse> {
                override fun onResponse(call: Call<NewsResponse>, response: Response<NewsResponse>) {
                    progressBar.visibility = View.GONE
                    if (response.isSuccessful) {
                        val articles = response.body()?.results ?: emptyList()
                        newsAdapter.updateData(articles)
                    } else {
                        showToast("Failed to load news: ${response.message()}")
                    }
                }

                override fun onFailure(call: Call<NewsResponse>, t: Throwable) {
                    progressBar.visibility = View.GONE
                    Log.e("NewsApp", "Error: ${t.message}")
                    showToast("Network error: ${t.message}")
                }
            })
    }

    private fun redirectToLogin() {
        startActivity(Intent(this, loginpage::class.java))
        finish()
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}

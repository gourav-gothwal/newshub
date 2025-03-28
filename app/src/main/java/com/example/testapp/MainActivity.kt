package com.example.testapp

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import com.example.testapp.fragments.BookmarksFragment
import com.example.testapp.fragments.HomeFragment
import com.example.testapp.fragments.ProfileFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class MainActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var bottomNavigationView: BottomNavigationView
    private var currentUser: FirebaseUser? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        super.onCreate(savedInstanceState)

        // Check for current user immediately
        auth = FirebaseAuth.getInstance()
        currentUser = auth.currentUser

        // Redirect to login if no user is logged in
        if (currentUser == null) {
            redirectToLogin()
            return
        }

        // Set up the main activity layout
        setContentView(R.layout.activity_main)

        // Welcome toast with user's display name or email
        Toast.makeText(
            this,
            "Welcome ${currentUser?.displayName ?: currentUser?.email ?: "User"}",
            Toast.LENGTH_SHORT
        ).show()

        bottomNavigationView = findViewById(R.id.bottomNavigationView)

        // Load Home fragment by default
        if (savedInstanceState == null) {
            loadFragment(HomeFragment())
        }

        // Handle bottom navigation clicks
        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.home -> {
                    loadFragment(HomeFragment())
                    true
                }
                R.id.bookmarks -> {
                    loadFragment(BookmarksFragment())
                    true
                }
                R.id.profile -> {
                    loadFragment(ProfileFragment())
                    true
                }
                else -> false
            }
        }
    }

    // Logout method to be used from Profile Fragment or Settings
    fun logout() {
        auth.signOut()
        redirectToLogin()
    }

    private fun redirectToLogin() {
        val intent = Intent(this, loginpage::class.java)
        // Clear the back stack to prevent returning to MainActivity
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, fragment)
            .commit()
    }

    override fun onBackPressed() {
        val homeFragment = supportFragmentManager.findFragmentByTag(HomeFragment::class.java.simpleName)
        if (homeFragment == null || !homeFragment.isVisible) {
            loadFragment(HomeFragment())
            bottomNavigationView.selectedItemId = R.id.home
        } else {
            super.onBackPressed()
        }
    }

    // Optional: Check if user is still authenticated
    override fun onResume() {
        super.onResume()
        currentUser = auth.currentUser
        if (currentUser == null) {
            redirectToLogin()
        }
    }
}
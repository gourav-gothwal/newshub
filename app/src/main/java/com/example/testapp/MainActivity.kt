package com.example.testapp

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
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
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        auth = FirebaseAuth.getInstance()
        currentUser = auth.currentUser

        if (currentUser == null) {
            redirectToLogin()
            return
        }

        Toast.makeText(this, "Welcome ${currentUser?.displayName ?: currentUser?.email}", Toast.LENGTH_SHORT).show()

        bottomNavigationView = findViewById(R.id.bottomNavigationView)

        // Load HomeFragment by default
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

    private fun redirectToLogin() {
        startActivity(Intent(this, loginpage::class.java))  // Ensure class name is correct
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
}

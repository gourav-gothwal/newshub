package com.example.testapp

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class SplashScreen : AppCompatActivity() {
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        sharedPreferences = getSharedPreferences("AppPrefs", MODE_PRIVATE)

        val isUserLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false)
        val isFirstTime = sharedPreferences.getBoolean("isFirstTime", true)

        if (isUserLoggedIn) {
            // If the user is already logged in, go to MainActivity immediately
            startActivity(Intent(this, MainActivity::class.java))
            finish()
            return
        }

        val getStartedButton: Button = findViewById(R.id.button)

        // If it's the first time, wait for user input; otherwise, go to login page
        if (isFirstTime) {
            getStartedButton.setOnClickListener {
                sharedPreferences.edit().apply {
                    putBoolean("isFirstTime", false)
                    apply()
                }
                startActivity(Intent(this, LoginPage::class.java))
                finish()
            }
        } else {
            startActivity(Intent(this, LoginPage::class.java))
            finish()
        }
    }
}

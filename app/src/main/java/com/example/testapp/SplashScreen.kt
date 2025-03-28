package com.example.testapp

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class SplashScreen : AppCompatActivity() {
    private val splashTime: Long = 3000 // 3 seconds delay
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

        // If it's the first time, show SplashScreen. Otherwise, move to login/signup.
        if (isFirstTime) {
            val handler = Handler(Looper.getMainLooper())
            val runnable = Runnable {
                startActivity(Intent(this, loginpage::class.java))
                finish()
            }
            handler.postDelayed(runnable, splashTime)

            // Skip splash if user clicks "Get Started"
            getStartedButton.setOnClickListener {
                handler.removeCallbacks(runnable)
                val editor = sharedPreferences.edit()
                editor.putBoolean("isFirstTime", false)
                editor.apply()
                startActivity(Intent(this, loginpage::class.java))
                finish()
            }
        } else {
            startActivity(Intent(this, loginpage::class.java))
            finish()
        }
    }
}

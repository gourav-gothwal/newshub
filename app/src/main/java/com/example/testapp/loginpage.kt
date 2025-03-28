package com.example.testapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

class loginpage : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: SignInClient
    private val REQ_ONE_TAP = 9001

    override fun onCreate(savedInstanceState: Bundle?) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        super.onCreate(savedInstanceState)

        // Check if user is already logged in
        auth = FirebaseAuth.getInstance()
        if (auth.currentUser != null) {
            // User is already logged in, navigate to main activity
            navigateToMainActivity()
            return
        }

        // If not logged in, show the login layout
        setContentView(R.layout.activity_loginpage)

        val loginButton = findViewById<Button>(R.id.buttonSignIn)
        val googleSignInButton = findViewById<Button>(R.id.buttonGoogle)
        val emailEditText = findViewById<EditText>(R.id.textInputEditTextEmail)
        val passwordEditText = findViewById<EditText>(R.id.textInputEditTextPassword)
        val createAccountText = findViewById<TextView>(R.id.textViewCreateAccount)
        val forgotPasswordText = findViewById<TextView>(R.id.textView5)

        // Email-Password Login
        loginButton.setOnClickListener {
            val email = emailEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            navigateToMainActivity()
                        } else {
                            Toast.makeText(this, "Error: ${task.exception?.message}", Toast.LENGTH_LONG).show()
                        }
                    }
            } else {
                Toast.makeText(this, "Please enter email and password", Toast.LENGTH_SHORT).show()
            }
        }

        // Navigate to Account Creation
        createAccountText.setOnClickListener {
            startActivity(Intent(this, accountcreation::class.java))
        }

        // Forgot Password
        forgotPasswordText.setOnClickListener {
            resetPassword(emailEditText)
        }

        // Google Sign-In Configuration
        googleSignInClient = Identity.getSignInClient(this)

        val signInRequest = BeginSignInRequest.builder()
            .setGoogleIdTokenRequestOptions(
                BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                    .setSupported(true)
                    .setServerClientId(getString(R.string.default_web_client_id))
                    .setFilterByAuthorizedAccounts(false)
                    .build()
            )
            .build()

        // Google Sign-In Button Click
        googleSignInButton.setOnClickListener {
            googleSignInClient.beginSignIn(signInRequest)
                .addOnSuccessListener { result ->
                    startIntentSenderForResult(result.pendingIntent.intentSender, REQ_ONE_TAP, null, 0, 0, 0)
                }
                .addOnFailureListener { e ->
                    Log.e("Google Sign-In", "Failed: ${e.message}")
                    Toast.makeText(this, "Google Sign-In Failed", Toast.LENGTH_SHORT).show()
                }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQ_ONE_TAP) {
            try {
                val credential = googleSignInClient.getSignInCredentialFromIntent(data)
                val idToken = credential.googleIdToken
                if (idToken != null) {
                    val firebaseCredential = GoogleAuthProvider.getCredential(idToken, null)
                    auth.signInWithCredential(firebaseCredential)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                navigateToMainActivity()
                            } else {
                                Toast.makeText(this, "Google Sign-In Failed", Toast.LENGTH_SHORT).show()
                            }
                        }
                }
            } catch (e: ApiException) {
                Log.e("Google Sign-In", "Error: ${e.message}")
                Toast.makeText(this, "Google Sign-In Error", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Method to navigate to main activity
    private fun navigateToMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        // Clear the back stack so user can't go back to login screen
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }

    // Forgot Password Functionality
    private fun resetPassword(emailEditText: EditText) {
        val email = emailEditText.text.toString().trim()
        if (email.isNotEmpty()) {
            auth.sendPasswordResetEmail(email)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this, "Password reset email sent", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this, "Error: ${task.exception?.message}", Toast.LENGTH_LONG).show()
                    }
                }
        } else {
            Toast.makeText(this, "Please enter your email", Toast.LENGTH_SHORT).show()
        }
    }
}
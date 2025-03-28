package com.example.testapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

class accountcreation : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var oneTapClient: SignInClient
    private lateinit var signInRequest: BeginSignInRequest

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
        setContentView(R.layout.activity_account_creation)

        oneTapClient = Identity.getSignInClient(this)

        // Configure Google Sign-In
        signInRequest = BeginSignInRequest.builder()
            .setGoogleIdTokenRequestOptions(
                BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                    .setSupported(true)
                    .setServerClientId(getString(R.string.default_web_client_id))
                    .setFilterByAuthorizedAccounts(false)
                    .build()
            )
            .build()

        val button2 = findViewById<Button>(R.id.button2)
        val googleSignInButton = findViewById<Button>(R.id.button3)
        val textInputEditTextEmail = findViewById<EditText>(R.id.textInputEditTextEmail)
        val textInputEditTextPassword = findViewById<EditText>(R.id.textInputEditTextPassword)

        // Email & Password Registration
        button2.setOnClickListener {
            val email = textInputEditTextEmail.text.toString().trim()
            val password = textInputEditTextPassword.text.toString().trim()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(this, "Registration Successful", Toast.LENGTH_SHORT).show()
                            navigateToMainActivity()
                        } else {
                            Toast.makeText(this, "Error: ${task.exception?.message}", Toast.LENGTH_LONG).show()
                        }
                    }
            } else {
                Toast.makeText(this, "Email and Password cannot be empty", Toast.LENGTH_SHORT).show()
            }
        }

        // Google Sign-In Button
        googleSignInButton.setOnClickListener {
            oneTapClient.beginSignIn(signInRequest)
                .addOnSuccessListener { result ->
                    googleSignInLauncher.launch(IntentSenderRequest.Builder(result.pendingIntent).build())
                }
                .addOnFailureListener { e ->
                    Toast.makeText(this, "Google Sign-In Failed: ${e.localizedMessage}", Toast.LENGTH_LONG).show()
                }
        }
    }

    // Handle Google Sign-In result
    private val googleSignInLauncher = registerForActivityResult(
        ActivityResultContracts.StartIntentSenderForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            try {
                val credential = oneTapClient.getSignInCredentialFromIntent(result.data)
                val idToken = credential.googleIdToken
                if (idToken != null) {
                    val firebaseCredential = GoogleAuthProvider.getCredential(idToken, null)
                    auth.signInWithCredential(firebaseCredential)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                Toast.makeText(this, "Google Sign-In Successful", Toast.LENGTH_SHORT).show()
                                navigateToMainActivity()
                            } else {
                                Toast.makeText(this, "Authentication Failed: ${task.exception?.message}", Toast.LENGTH_LONG).show()
                            }
                        }
                }
            } catch (e: Exception) {
                Log.e("GoogleSignIn", "Error: ${e.localizedMessage}")
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

    // Optional: Add a logout method in your MainActivity
    fun logout() {
        auth.signOut()
        oneTapClient.signOut()

        // Redirect to login screen
        val intent = Intent(this, accountcreation::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }
}
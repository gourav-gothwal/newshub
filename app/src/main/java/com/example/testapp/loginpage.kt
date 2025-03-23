package com.example.testapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.testapp.R
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
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loginpage)

        auth = FirebaseAuth.getInstance()

        val loginButton = findViewById<Button>(R.id.buttonSignIn)
        val googleSignInButton = findViewById<Button>(R.id.buttonGoogle) // Ensure this ID exists in XML
        val emailEditText = findViewById<EditText>(R.id.textInputEditTextEmail)
        val passwordEditText = findViewById<EditText>(R.id.textInputEditTextPassword)

        // Email-Password Login
        loginButton.setOnClickListener {
            val email = emailEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            startActivity(Intent(this, MainActivity::class.java))
                            finish()
                        } else {
                            Toast.makeText(this, "Error: ${task.exception?.message}", Toast.LENGTH_LONG).show()
                        }
                    }
            } else {
                Toast.makeText(this, "Please enter email and password", Toast.LENGTH_SHORT).show()
            }
        }

        // Google Sign-In Configuration
        googleSignInClient = Identity.getSignInClient(this)

        val signInRequest = BeginSignInRequest.builder()
            .setGoogleIdTokenRequestOptions(
                BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                    .setSupported(true)
                    .setServerClientId(getString(R.string.default_web_client_id)) // Replace with your Web Client ID
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
                                startActivity(Intent(this, MainActivity::class.java))
                                finish()
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
}

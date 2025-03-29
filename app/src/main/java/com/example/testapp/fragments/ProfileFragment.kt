package com.example.testapp.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.testapp.R
import com.example.testapp.loginpage
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth

class ProfileFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        val backButton: ImageView = view.findViewById(R.id.backButton)
        val editProfileOption: View = view.findViewById(R.id.editProfileOption)
        val appearanceOption: View = view.findViewById(R.id.appearanceOption)
        val changePasswordOption: View = view.findViewById(R.id.changePasswordOption)
        val termsOption: View = view.findViewById(R.id.termsOption)
        val privacyOption: View = view.findViewById(R.id.privacyOption)
        val logoutOption: View = view.findViewById(R.id.logoutOption)
        val profileName: TextView = view.findViewById(R.id.profileName)
        val profileEmail: TextView = view.findViewById(R.id.profileEmail)
        val profileImage: ImageView = view.findViewById(R.id.profileAvatar)

        val sharedPreferences = requireActivity().getSharedPreferences("UserPrefs", 0)
        profileName.text = sharedPreferences.getString("user_name", "Name Surname")
        profileEmail.text = sharedPreferences.getString("user_email", "Email Address")

        val firebaseUser = FirebaseAuth.getInstance().currentUser
        if (firebaseUser != null) {
            profileName.text = firebaseUser.displayName
            profileEmail.text = firebaseUser.email

            val photoUrl = firebaseUser.photoUrl
            if (photoUrl != null) {
                Glide.with(this)
                    .load(photoUrl)
                    .placeholder(R.drawable.default_profile)
                    .into(profileImage)
            } else {
                profileImage.setImageResource(R.drawable.default_profile)
            }
        }

        backButton.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        editProfileOption.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, EditProfileFragment())
                .addToBackStack(null)
                .commit()
        }

        termsOption.setOnClickListener {
            showDialog("Terms and Conditions", "These are the terms and conditions of using this application...")
        }

        privacyOption.setOnClickListener {
            showDialog("Privacy Policy", "This is the privacy policy of this application...")
        }

        appearanceOption.setOnClickListener {
            // Open appearance settings
        }

        changePasswordOption.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, ChangePasswordFragment())
                .addToBackStack(null)
                .commit()
        }

        logoutOption.setOnClickListener {
            showLogoutDialog()
        }

        return view
    }

    private fun showDialog(title: String, content: String) {
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_terms_conditions, null)
        val dialog = AlertDialog.Builder(requireContext())
            .setView(dialogView)
            .setCancelable(false)
            .create()

        val tvTitle: TextView = dialogView.findViewById(R.id.tvTermsTitle)
        val tvContent: TextView = dialogView.findViewById(R.id.tvTermsContent)
        val btnClose: Button = dialogView.findViewById(R.id.btnClose)

        tvTitle.text = title
        tvContent.text = content

        btnClose.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun showLogoutDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle("Logout")
            .setMessage("Are you sure you want to logout?")
            .setPositiveButton("Yes") { _, _ ->
                logoutUser()
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun logoutUser() {
        val firebaseAuth = FirebaseAuth.getInstance()
        val googleSignInClient = GoogleSignIn.getClient(requireActivity(), GoogleSignInOptions.DEFAULT_SIGN_IN)

        firebaseAuth.signOut()
        googleSignInClient.signOut()

        val intent = Intent(requireActivity(), loginpage::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        requireActivity().finish() // Close the current activity
    }
}

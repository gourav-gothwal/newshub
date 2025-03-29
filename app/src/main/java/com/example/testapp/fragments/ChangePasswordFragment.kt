package com.example.testapp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.testapp.R
import com.google.firebase.auth.FirebaseAuth

class ChangePasswordFragment : Fragment() {

    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_change_password, container, false)

        auth = FirebaseAuth.getInstance()

        val newPasswordInput: EditText = view.findViewById(R.id.editTextNewPassword)
        val confirmPasswordInput: EditText = view.findViewById(R.id.editTextConfirmPassword)
        val changePasswordButton: Button = view.findViewById(R.id.btnChangePassword)

        changePasswordButton.setOnClickListener {
            val newPassword = newPasswordInput.text.toString().trim()
            val confirmPassword = confirmPasswordInput.text.toString().trim()

            if (newPassword.isEmpty() || confirmPassword.isEmpty()) {
                Toast.makeText(requireContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (newPassword != confirmPassword) {
                Toast.makeText(requireContext(), "Passwords do not match", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (newPassword.length < 6) {
                Toast.makeText(requireContext(), "Password must be at least 6 characters", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            changeUserPassword(newPassword)
        }

        return view
    }

    private fun changeUserPassword(newPassword: String) {
        val user = auth.currentUser
        user?.updatePassword(newPassword)
            ?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(requireContext(), "Password changed successfully", Toast.LENGTH_SHORT).show()
                    requireActivity().supportFragmentManager.popBackStack()
                } else {
                    Toast.makeText(requireContext(), "Failed to change password: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }
}

package com.example.testapp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.testapp.R

class EditProfileFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_edit_profile, container, false)

        val nameInput: EditText = view.findViewById(R.id.editTextName)
        val emailInput: EditText = view.findViewById(R.id.editTextEmail)
        val saveButton: Button = view.findViewById(R.id.btnSave)
        val backButton: ImageView = view.findViewById(R.id.imageView5)

        //populate edit texts with existing data.
        val sharedPreferences = requireActivity().getSharedPreferences("UserPrefs", 0)
        val savedName = sharedPreferences.getString("user_name", "")
        val savedEmail = sharedPreferences.getString("user_email", "")

        nameInput.setText(savedName)
        emailInput.setText(savedEmail)

        saveButton.setOnClickListener {
            val newName = nameInput.text.toString()
            val newEmail = emailInput.text.toString()

            if (newEmail.isNotEmpty() && android.util.Patterns.EMAIL_ADDRESS.matcher(newEmail).matches()) {
                val editor = sharedPreferences.edit()
                editor.putString("user_name", newName)
                editor.putString("user_email", newEmail)
                editor.apply()

                Toast.makeText(requireContext(), "Profile saved", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(requireContext(), "Invalid email format", Toast.LENGTH_SHORT).show()
            }
        }

        backButton.setOnClickListener {
            requireActivity().onBackPressed()
        }

        return view
    }
}
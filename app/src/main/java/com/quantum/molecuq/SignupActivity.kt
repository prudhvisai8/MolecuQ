package com.quantum.molecuq // Replace with your package name

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class SignupActivity : AppCompatActivity() {

    private lateinit var logoImageView: ImageView
    private lateinit var titleTextView: TextView
    private lateinit var nameEditText: EditText
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var signUpButton: Button
    private lateinit var loginTextView: TextView
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup) // Ensure this is the correct layout file name

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()

        // Initialize UI elements using findViewById
        logoImageView = findViewById(R.id.imageView)
        titleTextView = findViewById(R.id.tvTitle)
        nameEditText = findViewById(R.id.etName)
        emailEditText = findViewById(R.id.etEmail)
        passwordEditText = findViewById(R.id.etPassword)
        signUpButton = findViewById(R.id.btnSignup)
        loginTextView = findViewById(R.id.tvLogin)

        // Set click listener for the sign-up button
        signUpButton.setOnClickListener {
            handleSignUpWithFirebase()
        }

        // Set click listener for the login text
        loginTextView.setOnClickListener {
            navigateToLogin()
        }
    }

    private fun handleSignUpWithFirebase() {
        val name = nameEditText.text.toString().trim()
        val email = emailEditText.text.toString().trim()
        val password = passwordEditText.text.toString().trim()

        if (name.isEmpty()) {
            nameEditText.error = "Name is required"
            nameEditText.requestFocus()
            return
        }

        if (email.isEmpty()) {
            emailEditText.error = "Email is required"
            emailEditText.requestFocus()
            return
        }

        if (password.isEmpty()) {
            passwordEditText.error = "Password is required"
            passwordEditText.requestFocus()
            return
        }

        if (password.length < 6) {
            passwordEditText.error = "Password should be at least 6 characters long"
            passwordEditText.requestFocus()
            return
        }

        // Create user with email and password using Firebase
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    val user = auth.currentUser
                    // You might want to save the user's name in Firebase Realtime Database or Firestore
                    showToast("Sign up successful!")
                    navigateToDashboard()
                } else {
                    // If sign in fails, display a message to the user.
                    showToast("Sign up failed: ${task.exception?.message}")
                }
            }
    }

    private fun navigateToLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish() // Optional: Close the current activity
    }

    private fun navigateToDashboard() {
        val intent = Intent(this, Dashboard::class.java) // Change to your actual dashboard activity
        startActivity(intent)
        finish()
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
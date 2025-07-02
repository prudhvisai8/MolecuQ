package com.quantum.molecuq

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.quantum.molecuq.databinding.ActivityForgotPasswordBinding

class ForgotPasswordActivity : AppCompatActivity() {
    private lateinit var binding: ActivityForgotPasswordBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForgotPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()

        // Handle the main button click (now sends reset email)
        binding.btnChangePassword.setOnClickListener {
            val email = binding.etEmail.text.toString().trim()
            val newPassword = binding.etNewPassword.text.toString().trim()
            val confirmPassword = binding.etConfirmPassword.text.toString().trim()

            // Validate email
            if (email.isEmpty()) {
                binding.etEmail.error = "Email is required"
                binding.etEmail.requestFocus()
                return@setOnClickListener
            }
            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                binding.etEmail.error = "Enter a valid email address"
                binding.etEmail.requestFocus()
                return@setOnClickListener
            }

            // Validate new password
            if (newPassword.isEmpty()) {
                binding.etNewPassword.error = "New password is required"
                binding.etNewPassword.requestFocus()
                return@setOnClickListener
            }
            if (newPassword.length < 6) {
                binding.etNewPassword.error = "Password must be at least 6 characters"
                binding.etNewPassword.requestFocus()
                return@setOnClickListener
            }

            // Validate confirm password
            if (confirmPassword.isEmpty()) {
                binding.etConfirmPassword.error = "Please confirm your password"
                binding.etConfirmPassword.requestFocus()
                return@setOnClickListener
            }
            if (newPassword != confirmPassword) {
                binding.etConfirmPassword.error = "Passwords do not match"
                binding.etConfirmPassword.requestFocus()
                return@setOnClickListener
            }

            // At this point, all fields are validated.
            // Now, send the password reset email.
            // The actual password change using `confirmPasswordReset`
            // can ONLY happen once you have the `resetCode` from the email link.
            sendPasswordResetEmail(email)

            // IMPORTANT: The `confirmPasswordReset` call below is for demonstration
            // and will NOT work without a valid `resetCode`.
            // In a real scenario, you'd only call this if your app was launched
            // via a deep link containing the resetCode from the email.
            // If you intend to use the new and confirm password fields directly
            // here, you'd need to establish a very custom and complex backend process
            // that is outside of Firebase's direct client-side methods.
            // For Firebase, the flow is: Send email -> User clicks link -> (App gets resetCode) -> Confirm password.

            // Example of how confirmPasswordReset would be used IF you had a resetCode:
            // val receivedResetCode = "YOUR_RESET_CODE_FROM_DEEPLINK" // This needs to come from intent/deeplink
            // if (receivedResetCode.isNotEmpty()) {
            //     changePasswordWithFirebase(receivedResetCode, newPassword)
            // } else {
            //     // This else block would typically be where you are after sending the email,
            //     // waiting for the user to click the link.
            //     Toast.makeText(this, "Please check your email to complete the password reset.", Toast.LENGTH_LONG).show()
            // }
        }

        // Navigate back to LoginActivity
        binding.tvBackToLogin.setOnClickListener {
            finish()
        }
    }

    // Function to send the password reset email
    private fun sendPasswordResetEmail(email: String) {
        auth.sendPasswordResetEmail(email)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Toast.makeText(
                        this,
                        "Password reset email sent to $email. Please check your inbox to complete the process.",
                        Toast.LENGTH_LONG
                    ).show()
                    // You might want to finish the activity here, or guide the user
                    // on what to do next (check email).
                    finish()
                } else {
                    Toast.makeText(
                        this,
                        "Failed to send reset email: ${task.exception?.message}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
    }

    // This function is for changing the password *after* a reset code is obtained.
    // It should ideally be called when the app receives a deep link with the resetCode.
    private fun changePasswordWithFirebase(resetCode: String, newPassword: String) {
        auth.confirmPasswordReset(resetCode, newPassword)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Toast.makeText(
                        this,
                        "Password changed successfully!",
                        Toast.LENGTH_LONG
                    ).show()
                    finish() // Return to LoginActivity
                } else {
                    Toast.makeText(
                        this,
                        "Failed to change password: ${task.exception?.message}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
    }
}
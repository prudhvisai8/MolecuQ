package com.quantum.molecuq

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthRecentLoginRequiredException
import com.quantum.molecuq.databinding.ActivityChangePasswordBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

/**
 * Activity for handling user password changes using Firebase Authentication.
 */
class ChangePasswordActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChangePasswordBinding
    private val TAG = "ChangePasswordActivity"
    // Coroutine scope for managing asynchronous operations, ensuring they are cancelled when the activity is destroyed.
    private val coroutineScope = CoroutineScope(Dispatchers.Main + SupervisorJob())
    private lateinit var auth: FirebaseAuth

    // Define a constant for minimum password length for better maintainability.
    private val MIN_PASSWORD_LENGTH = 8

    /**
     * Called when the activity is first created.
     * Initializes the view binding, Firebase Auth, and sets up UI components.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate")
        binding = ActivityChangePasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize Firebase Authentication instance.
        auth = FirebaseAuth.getInstance()

        // Setup UI components and listeners.
        setupToolbar()
        setupInputValidation()
        setupClickListeners()
    }

    /**
     * Called after onCreate() or onRestart().
     * This is where the activity becomes visible to the user.
     */
    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart")
    }

    /**
     * Called after onStart().
     * This is where the activity begins interacting with the user.
     */
    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume")
    }

    /**
     * Called when the activity is going into the background.
     * Another activity is taking focus.
     */
    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause")
    }

    /**
     * Called when the activity is no longer visible to the user.
     */
    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop")
    }

    /**
     * Called before the activity is destroyed.
     * Cancels the coroutine scope to prevent memory leaks.
     */
    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy")
        coroutineScope.cancel() // Cancel the coroutine scope to prevent leaks
    }

    /**
     * Sets up the toolbar's back button click listener.
     */
    private fun setupToolbar() {
        binding.backButton.setOnClickListener {
            // Navigate back to the previous activity in the stack.
            onBackPressedDispatcher.onBackPressed()
        }
    }

    /**
     * Sets up input validation listeners for text changes.
     * Clears error messages as the user types.
     */
    private fun setupInputValidation() {
        binding.apply {
            currentPasswordEditText.doOnTextChanged { _, _, _, _ ->
                currentPasswordInputLayout.error = null // Clear specific input error
                errorMessage.visibility = View.GONE // Clear general error message
            }
            newPasswordEditText.doOnTextChanged { _, _, _, _ ->
                newPasswordInputLayout.error = null // Clear specific input error
                errorMessage.visibility = View.GONE // Clear general error message
            }
            confirmPasswordEditText.doOnTextChanged { _, _, _, _ ->
                confirmPasswordInputLayout.error = null // Clear specific input error
                errorMessage.visibility = View.GONE // Clear general error message
            }
        }
    }

    /**
     * Sets up click listeners for the save and cancel buttons.
     */
    private fun setupClickListeners() {
        binding.apply {
            saveButton.setOnClickListener {
                // Validate inputs before attempting to change the password.
                if (validateInputs()) {
                    changePassword()
                }
            }
            cancelButton.setOnClickListener {
                // Navigate back to the previous activity.
                onBackPressedDispatcher.onBackPressed()
            }
        }
    }

    /**
     * Validates the user's input for current, new, and confirmed passwords.
     * Displays specific error messages for invalid inputs.
     * @return true if all inputs are valid, false otherwise.
     */
    @SuppressLint("StringFormatInvalid")
    private fun validateInputs(): Boolean {
        binding.apply {
            // Clear any previous general error message before validating.
            errorMessage.visibility = View.GONE
            errorMessage.text = ""

            val currentPassword = currentPasswordEditText.text?.toString()?.trim() ?: ""
            val newPassword = newPasswordEditText.text?.toString()?.trim() ?: ""
            val confirmPassword = confirmPasswordEditText.text?.toString()?.trim() ?: ""

            if (currentPassword.isEmpty()) {
                currentPasswordInputLayout.error = getString(R.string.error_current_password_required)
                return false
            }

            if (newPassword.isEmpty()) {
                newPasswordInputLayout.error = getString(R.string.error_new_password_required)
                return false
            } else if (newPassword.length < MIN_PASSWORD_LENGTH) {
                newPasswordInputLayout.error = getString(R.string.error_new_password_length, MIN_PASSWORD_LENGTH)
                return false
            }

            if (confirmPassword.isEmpty()) {
                confirmPasswordInputLayout.error = getString(R.string.error_confirm_password_required)
                return false
            }

            if (newPassword != confirmPassword) {
                newPasswordInputLayout.error = getString(R.string.error_password_mismatch)
                confirmPasswordInputLayout.error = getString(R.string.error_password_mismatch)
                return false
            }
            return true
        }
    }

    /**
     * Initiates the password change process with Firebase Authentication.
     * Reauthenticates the user first, then updates the password.
     */
    private fun changePassword() {
        // Show progress bar and disable save button during the operation.
        binding.progressBar.visibility = View.VISIBLE
        binding.saveButton.isEnabled = false

        val currentPassword = binding.currentPasswordEditText.text?.toString()?.trim() ?: ""
        val newPassword = binding.newPasswordEditText.text?.toString()?.trim() ?: ""
        val user = auth.currentUser

        // Check if a user is currently authenticated.
        if (user == null) {
            binding.progressBar.visibility = View.GONE
            binding.saveButton.isEnabled = true
            showToast(getString(R.string.error_user_not_authenticated))
            return
        }

        // Reauthenticate the user before changing the password.
        // This is a security measure required by Firebase.
        coroutineScope.launch(Dispatchers.IO) {
            try {
                val email = user.email ?: throw IllegalStateException("User email is null, cannot reauthenticate.")
                val credential = EmailAuthProvider.getCredential(email, currentPassword)
                user.reauthenticate(credential).await() // Await reauthentication completion.

                // If reauthentication is successful, proceed to update the password.
                user.updatePassword(newPassword).await() // Await password update completion.

                withContext(Dispatchers.Main) {
                    // Show success message and navigate back on successful password change.
                    Toast.makeText(
                        this@ChangePasswordActivity,
                        getString(R.string.password_changed_successfully),
                        Toast.LENGTH_SHORT
                    ).show()
                    onBackPressedDispatcher.onBackPressed()
                }
            } catch (e: FirebaseAuthInvalidCredentialsException) {
                // Handle incorrect current password error.
                withContext(Dispatchers.Main) {
                    binding.errorMessage.apply {
                        text = getString(R.string.error_incorrect_current_password)
                        visibility = View.VISIBLE
                    }
                }
            } catch (e: FirebaseAuthRecentLoginRequiredException) {
                // Handle cases where user's last sign-in was too long ago.
                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        this@ChangePasswordActivity,
                        getString(R.string.error_reauthentication_required),
                        Toast.LENGTH_LONG // Use LONG for more critical messages
                    ).show()
                    // Optionally, you might want to redirect to a re-login screen here.
                }
            } catch (e: Exception) {
                // Catch any other unexpected errors during the process.
                Log.e(TAG, "Error changing password: ${e.message}", e)
                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        this@ChangePasswordActivity,
                        getString(R.string.error_password_change_failed),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } finally {
                // Ensure UI state is reset regardless of success or failure.
                withContext(Dispatchers.Main) {
                    binding.progressBar.visibility = View.GONE
                    binding.saveButton.isEnabled = true // Re-enable the save button
                }
            }
        }
    }

    /**
     * Helper function to display a short Toast message.
     * @param message The string message to display.
     */
    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
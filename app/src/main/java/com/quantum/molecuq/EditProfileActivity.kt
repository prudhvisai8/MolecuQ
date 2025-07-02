package com.quantum.molecuq

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import java.lang.Exception

class EditProfileActivity : AppCompatActivity() {

    private lateinit var usernameEditText: TextInputEditText
    private lateinit var phoneEditText: TextInputEditText
    private lateinit var emailEditText: TextInputEditText
    private lateinit var profileImage: ImageView
    private lateinit var filePickerLauncher: ActivityResultLauncher<Intent>
    private var profileImageUri: Uri? = null

    // Constants
    private val USER_PREFS_NAME = "UserPrefs"
    private val USERNAME_KEY = "username"
    private val PHONE_KEY = "phone"
    private val EMAIL_KEY = "email"
    private val PROFILE_IMAGE_URI_KEY = "profile_image_uri"
    private val REQUEST_IMAGE_PICK = 1000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)

        // Initialize views
        setupViews()
        // Initialize ActivityResultLauncher for image picking
        prepareImagePicker()
        // Set up click listeners
        setupClickListeners()
    }

    private fun setupViews() {
        // Find views using Kotlin's findViewById
        val titleText = findViewById<TextView>(R.id.titleText)
        usernameEditText = findViewById(R.id.usernameEditText)
        phoneEditText = findViewById(R.id.phoneEditText)
        emailEditText = findViewById(R.id.emailEditText)
        profileImage = findViewById(R.id.profileImage)

        // Optional: Apply styling
        titleText.isAllCaps = false

        loadExistingProfileData()
    }

    private fun prepareImagePicker() {
        filePickerLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == RESULT_OK) {
                    val data: Intent? = result.data
                    data?.data?.let { uri ->
                        profileImageUri = uri
                        // Load the image using a library like Glide or Coil
                        // Glide.with(this).load(uri).into(profileImage)
                        profileImage.setImageURI(uri) // Basic way to set
                        Log.d("EditProfileActivity", "Image URI: $uri")
                    }
                } else {
                    Log.d("EditProfileActivity", "Image picking cancelled")
                }
            }
    }

    private fun loadExistingProfileData() {
        try {
            val sharedPrefs = getSharedPreferences(USER_PREFS_NAME, Context.MODE_PRIVATE)
            usernameEditText.setText(sharedPrefs.getString(USERNAME_KEY, ""))
            phoneEditText.setText(sharedPrefs.getString(PHONE_KEY, ""))
            emailEditText.setText(sharedPrefs.getString(EMAIL_KEY, ""))
            // Load profile image if available
            sharedPrefs.getString(PROFILE_IMAGE_URI_KEY, null)?.let { uriString ->
                profileImageUri = Uri.parse(uriString) // Parse back to Uri
                // Use Glide/Coil for efficient image loading
                // Glide.with(this).load(profileImageUri).into(profileImage)
                profileImage.setImageURI(profileImageUri)
            }
        } catch (e: Exception) {
            Log.e("EditProfileActivity", "Error loading profile data", e)
            showToast(getString(R.string.error_loading_profile_data))
        }
    }

    private fun setupClickListeners() {
        val backButton = findViewById<ImageButton>(R.id.backButton)
        val changeProfileImageButton = findViewById<Button>(R.id.changeProfileImageButton)
        val saveButton = findViewById<Button>(R.id.saveButton)
        val cancelButton = findViewById<Button>(R.id.cancelButton)

        backButton.setOnClickListener {
            handleBackButtonClick()
        }

        changeProfileImageButton.setOnClickListener {
            handleChangeProfileImageButtonClick()
        }

        saveButton.setOnClickListener {
            saveProfileData()
        }

        cancelButton.setOnClickListener {
            handleCancelButtonClick()
        }
    }

    private fun handleBackButtonClick() {
        try {
            finish()
        } catch (e: Exception) {
            Log.e("EditProfileActivity", "Error finishing activity", e)
        }
    }

    private fun handleCancelButtonClick() {
        showConfirmationDialog()
    }

    private fun showConfirmationDialog() {
        MaterialAlertDialogBuilder(this)
            .setTitle("Discard Changes?")
            .setMessage("Are you sure you want to discard your changes?")
            .setPositiveButton("Discard") { _, _ ->
                finish() // Discard changes and close
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss() // Stay on the edit screen
            }
            .show()
    }


    private fun handleChangeProfileImageButtonClick() {
        // Use ActivityResultLauncher to start the image picker intent
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        filePickerLauncher.launch(intent)
    }

    private fun saveProfileData() {
        val username = usernameEditText.text.toString().trim()
        val phone = phoneEditText.text.toString().trim()
        val email = emailEditText.text.toString().trim()

        // Basic validation
        if (!validateInput(username, phone, email)) return

        try {
            // Save to SharedPreferences using the constants
            val sharedPrefs = getSharedPreferences(USER_PREFS_NAME, Context.MODE_PRIVATE)
            with(sharedPrefs.edit()) {
                putString(USERNAME_KEY, username)
                putString(PHONE_KEY, phone)
                putString(EMAIL_KEY, email)
                // Save the image URI as a string
                putString(PROFILE_IMAGE_URI_KEY, profileImageUri?.toString())
                apply()
            }
            showToast("Profile updated successfully")
            finish()
        } catch (e: Exception) {
            Log.e("EditProfileActivity", "Error saving profile data", e)
            showToast(getString(R.string.error_saving_profile))
        }
    }

    private fun validateInput(username: String, phone: String, email: String): Boolean {
        if (username.isEmpty()) {
            showToast("Username cannot be empty")
            return false
        }
        if (email.isNotEmpty() && !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            showToast("Invalid email address")
            return false
        }
        if (phone.isNotEmpty() && !android.util.Patterns.PHONE.matcher(phone).matches()) {
            showToast("Invalid Phone Number")
            return false
        }
        return true
    }

    private fun showToast(message: String) {
        // Use ContextCompat.getMainExecutor()
        ContextCompat.getMainExecutor(this).execute {
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        }
    }
}

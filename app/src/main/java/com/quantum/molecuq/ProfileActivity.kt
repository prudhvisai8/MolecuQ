package com.quantum.molecuq

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class ProfileActivity : AppCompatActivity() {

    private lateinit var tvUsername: TextView
    private lateinit var tvPhone: TextView
    private lateinit var tvEmail: TextView
    private lateinit var btnEditProfile: Button
    private lateinit var changePasswordOption: TextView
    private lateinit var accountSettingsOption: TextView
    private lateinit var notificationsOption: TextView
    private lateinit var savedOption: TextView
    private lateinit var privacyOption: TextView
    private lateinit var aboutOption: TextView
    private lateinit var logoutOption: TextView

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        auth = FirebaseAuth.getInstance()

        initViews()
        setClickListeners()
        loadUserData()
    }

    private fun initViews() {
        tvUsername = findViewById(R.id.tvUsername)
        tvPhone = findViewById(R.id.tvPhone)
        tvEmail = findViewById(R.id.tvEmail)
        btnEditProfile = findViewById(R.id.btnEditProfile)
        changePasswordOption = findViewById(R.id.changePasswordOption)
        accountSettingsOption = findViewById(R.id.accountSettingsOption)
        notificationsOption = findViewById(R.id.notificationsOption)
        savedOption = findViewById(R.id.savedOption)
        privacyOption = findViewById(R.id.privacyOption)
        aboutOption = findViewById(R.id.aboutOption)
        logoutOption = findViewById(R.id.logoutOption)
    }

    private fun setClickListeners() {
        btnEditProfile.setOnClickListener {
            startActivity(Intent(this, EditProfileActivity::class.java))
        }

        changePasswordOption.setOnClickListener {
            startActivity(Intent(this, ChangePasswordActivity::class.java))
        }

        accountSettingsOption.setOnClickListener {
            startActivity(Intent(this, AccountSettingsActivity::class.java))
        }

        notificationsOption.setOnClickListener {
            startActivity(Intent(this, NotificationsActivity::class.java))
        }

        savedOption.setOnClickListener {
            startActivity(Intent(this, SavedItemsActivity::class.java))
        }

        privacyOption.setOnClickListener {
            startActivity(Intent(this, PrivacyPolicyActivity::class.java))
        }

        aboutOption.setOnClickListener {
            startActivity(Intent(this, AboutActivity::class.java))
        }

        logoutOption.setOnClickListener {
            performLogout()
        }
    }

    private fun loadUserData() {
        val prefs = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        tvUsername.text = prefs.getString("username", getString(R.string.default_username))
        tvPhone.text = prefs.getString("phone", getString(R.string.default_phone))
        tvEmail.text = prefs.getString("email", getString(R.string.default_email))
    }

    private fun performLogout() {
        try {
            auth.signOut()
            getSharedPreferences("UserPrefs", Context.MODE_PRIVATE).edit().clear().apply()
            val intent = Intent(this, LoginActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            startActivity(intent)
            Toast.makeText(this, getString(R.string.logout_successful), Toast.LENGTH_SHORT).show()
            finish()
        } catch (e: Exception) {
            Toast.makeText(this, getString(R.string.error_logout), Toast.LENGTH_SHORT).show()
        }
    }
}

package com.quantum.molecuq

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.quantum.molecuq.databinding.ActivityAccountSettingsBinding // Import your View Binding class

class AccountSettingsActivity : AppCompatActivity() {

    // Use View Binding to access views
    private lateinit var binding: ActivityAccountSettingsBinding

    // Tag for logging messages
    private val TAG = "AccountSettingsActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Inflate the layout using View Binding
        binding = ActivityAccountSettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Log.d(TAG, "AccountSettingsActivity created.")

        setupToolbar()
        setupClickListeners()
        loadAccountSettings() // Load initial settings (e.g., 2FA status)
    }

    /**
     * Sets up the toolbar, including the back button and title.
     */
    private fun setupToolbar() {
        // Set the title of the toolbar if you're using one in activity_account_settings.xml
        // If your layout has a Toolbar with an ID like 'toolbar_account_settings', you'd do:
        // setSupportActionBar(binding.toolbarAccountSettings)
        // supportActionBar?.title = getString(R.string.account_settings_title)

        binding.backButton.setOnClickListener {
            Log.d(TAG, "Back button clicked.")
            onBackPressedDispatcher.onBackPressed() // Use onBackPressedDispatcher for modern Android
        }
        // Assuming titleText is part of a custom toolbar or header, set its text
        binding.titleText.text = getString(R.string.account_settings_title)
    }

    /**
     * Sets up click listeners for all interactive elements in the activity.
     */
    private fun setupClickListeners() {
        binding.twoFactorAuthLayout.setOnClickListener {
            // Toggle the switch state when the entire layout is clicked
            binding.twoFactorSwitch.isChecked = !binding.twoFactorSwitch.isChecked
            handleTwoFactorToggle(binding.twoFactorSwitch.isChecked)
        }

        // It's good practice to also have a listener on the switch itself
        // in case the user precisely taps the switch thumb.
        binding.twoFactorSwitch.setOnCheckedChangeListener { _, isChecked ->
            handleTwoFactorToggle(isChecked)
        }

        binding.exportDataOption.setOnClickListener {
            Log.d(TAG, "Export Data option clicked.")
            handleExportData()
        }

        binding.deleteAccountOption.setOnClickListener {
            Log.d(TAG, "Delete Account option clicked.")
            showDeleteAccountConfirmationDialog()
        }
    }

    /**
     * Handles the logic for toggling Two-Factor Authentication.
     * @param isEnabled True if 2FA is now enabled, false otherwise.
     */
    private fun handleTwoFactorToggle(isEnabled: Boolean) {
        val message = if (isEnabled) {
            getString(R.string.two_factor_enabled_toast)
        } else {
            getString(R.string.two_factor_disabled_toast)
        }
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        Log.d(TAG, "Two-Factor Authentication: ${if (isEnabled) "Enabled" else "Disabled"}")
        // TODO: Implement actual logic to enable/disable 2FA on your backend/Firebase
        // e.g., saveUserPreference(KEY_TWO_FACTOR_ENABLED, isEnabled)
        // or call Firebase Auth method for 2FA enrollment.
    }

    /**
     * Handles the logic for exporting user data.
     */
    private fun handleExportData() {
        Toast.makeText(this, getString(R.string.export_data_toast), Toast.LENGTH_LONG).show()
        // TODO: Implement actual data export logic. This might involve:
        // 1. Fetching data from your database/server.
        // 2. Formatting the data (e.g., JSON, CSV).
        // 3. Asking for storage permissions if saving to device.
        // 4. Saving the file or initiating a share intent.
        Log.i(TAG, "Data export initiated.")
    }

    /**
     * Displays a confirmation dialog before proceeding with account deletion.
     */
    private fun showDeleteAccountConfirmationDialog() {
        AlertDialog.Builder(this)
            .setTitle(getString(R.string.delete_account_dialog_title))
            .setMessage(getString(R.string.delete_account_dialog_message))
            .setPositiveButton(getString(R.string.delete_account_dialog_positive_button)) { dialog, _ ->
                // User confirmed deletion
                handleDeleteAccount()
                dialog.dismiss()
            }
            .setNegativeButton(getString(R.string.delete_account_dialog_negative_button)) { dialog, _ ->
                // User cancelled deletion
                Log.d(TAG, "Delete account cancelled by user.")
                dialog.dismiss()
            }
            .setIcon(android.R.drawable.ic_dialog_alert) // Standard warning icon
            .show()
    }

    /**
     * Handles the logic for deleting the user's account.
     * This is a critical operation and should be handled with extreme care.
     */
    private fun handleDeleteAccount() {
        Toast.makeText(this, getString(R.string.delete_account_toast), Toast.LENGTH_LONG).show()
        // TODO: Implement actual account deletion logic. This typically involves:
        // 1. Re-authenticating the user for security.
        // 2. Deleting user data from your database/server.
        // 3. Deleting the user from Firebase Authentication (if used).
        // 4. Navigating the user back to a login/onboarding screen.
        Log.w(TAG, "Account deletion initiated. Critical operation.")
    }

    /**
     * Loads the initial state of account settings, e.g., 2FA status.
     * This would typically fetch from SharedPreferences, a local database, or a remote server.
     */
    private fun loadAccountSettings() {
        // TODO: Fetch the current 2FA status from your data source
        // For demonstration, let's assume 2FA is initially off.
        val isTwoFactorInitiallyEnabled = false // Replace with actual loaded state
        binding.twoFactorSwitch.isChecked = isTwoFactorInitiallyEnabled
        Log.d(TAG, "Initial 2FA status loaded: $isTwoFactorInitiallyEnabled")
    }
}
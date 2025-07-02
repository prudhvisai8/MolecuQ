package com.quantum.molecuq

import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.quantum.molecuq.databinding.ActivityPrivacyPolicyBinding

class PrivacyPolicyActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPrivacyPolicyBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPrivacyPolicyBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupViews()
        setupClickListeners()
    }

    private fun setupViews() {
        // Enable clickable links for email and web URLs in the Contact section
        binding.contactText.movementMethod = LinkMovementMethod.getInstance()

        // Optional: Apply styling to improve visual hierarchy
        binding.titleText.isAllCaps = false
        binding.effectiveDateText.alpha = 0.8f
    }

    private fun setupClickListeners() {
        binding.backButton.setOnClickListener {
            try {
                finish()
            } catch (e: Exception) {
                Log.e("PrivacyPolicyActivity", "Error closing activity", e)
            }
        }
    }
}
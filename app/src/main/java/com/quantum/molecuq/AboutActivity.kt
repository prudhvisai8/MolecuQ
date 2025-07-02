package com.quantum.molecuq

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class AboutActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)

        val btnBack = findViewById<ImageView>(R.id.btnBack)
        val emailText = findViewById<TextView>(R.id.emailTextView) // Update this ID if needed
        val btnPrivacyPolicy = findViewById<Button>(R.id.btnPrivacyPolicy)
        val btnTermsOfService = findViewById<Button>(R.id.btnTermsOfService)

        // Handle back navigation
        btnBack.setOnClickListener {
            onBackPressed()
        }

        // Clickable email opens email client
        emailText.setOnClickListener {
            val intent = Intent(Intent.ACTION_SENDTO).apply {
                data = Uri.parse("mailto:support@molecuq.com")
                putExtra(Intent.EXTRA_SUBJECT, "Support Request")
            }
            startActivity(Intent.createChooser(intent, "Send Email"))
        }

        // Open Privacy Policy URL
        btnPrivacyPolicy.setOnClickListener {
            val intent = Intent(this, PrivacyPolicyActivity::class.java)
            startActivity(intent)
        }


        // Open Terms of Service URL
        btnTermsOfService.setOnClickListener {
            val intent = Intent(this, PrivacyPolicyActivity::class.java)
            startActivity(intent)
        }

    }
}

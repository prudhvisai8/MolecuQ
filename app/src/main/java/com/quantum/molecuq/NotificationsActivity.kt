package com.quantum.molecuq

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class NotificationsActivity : AppCompatActivity() {

    private lateinit var notificationIcon: ImageView
    private lateinit var notificationText: TextView
    private lateinit var notificationTime: TextView
    private lateinit var notificationIndicator: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notifications)

        // Initialize views
        notificationIcon = findViewById(R.id.notificationIcon)
        notificationText = findViewById(R.id.notificationText)
        notificationTime = findViewById(R.id.notificationTime)
        notificationIndicator = findViewById(R.id.notificationIndicator)

        // Set your notification content
        notificationText.text = "New molecule analysis completed."
        notificationTime.text = "5 minutes ago"

        // Show or hide red dot indicator (unread)
        val isUnread = true // this could come from your data model
        notificationIndicator.visibility = if (isUnread) View.VISIBLE else View.GONE
    }
}

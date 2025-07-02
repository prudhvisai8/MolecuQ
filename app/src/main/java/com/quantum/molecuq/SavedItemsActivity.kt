package com.quantum.molecuq

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class SavedItemsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_saved_items)

        setupViews()
        setupClickListeners()
    }

    private fun setupViews() {
        // Find views
        val titleText = findViewById<TextView>(R.id.titleText)
        val noSavedItemsText = findViewById<TextView>(R.id.noSavedItemsText)
        val savedItemsRecyclerView = findViewById<RecyclerView>(R.id.savedItemsRecyclerView)

        // Optional: Apply styling
        titleText.isAllCaps = false

        // Setup RecyclerView
        savedItemsRecyclerView.layoutManager = LinearLayoutManager(this)
        val savedItems = listOf(
            SavedItem("Molecular Simulation 001", "Saved on 2025-04-21: Acetylcholinesterase simulation results"),
            SavedItem("Protein Analysis 002", "Saved on 2025-04-20: Protein folding data")
        )
        savedItemsRecyclerView.adapter = SavedItemsAdapter(savedItems)

        // Show/hide no saved items message
        noSavedItemsText.visibility = if (savedItems.isEmpty()) View.VISIBLE else View.GONE
    }

    private fun setupClickListeners() {
        val backButton = findViewById<ImageButton>(R.id.backButton)
        backButton.setOnClickListener {
            try {
                finish()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}

// Data class for saved items
data class SavedItem(val title: String, val description: String)

// RecyclerView Adapter
class SavedItemsAdapter(private val savedItems: List<SavedItem>) :
    RecyclerView.Adapter<SavedItemsAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.savedItemTitle)
        val description: TextView = itemView.findViewById(R.id.savedItemDescription)

        fun bind(savedItem: SavedItem) {
            title.text = savedItem.title
            description.text = savedItem.description
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_saved_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(savedItems[position])
    }

    override fun getItemCount(): Int = savedItems.size
}


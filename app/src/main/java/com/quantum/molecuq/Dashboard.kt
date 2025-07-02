// app/src/main/java/com/quantum/molecuq/Dashboard.kt

package com.quantum.molecuq

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import com.quantum.molecuq.databinding.ActivityDashboardBinding

class Dashboard : AppCompatActivity() {

    private lateinit var binding: ActivityDashboardBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Inflate layout using View Binding
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Setup UI interactions
        setupClickListeners()
        setupSearchFunctionality()
        setupTooltips()
    }

    /**
     * Sets up click listeners for all interactive UI elements.
     */
    private fun setupClickListeners() {
        // notification card click listener
        binding.notificationCard.setOnClickListener {
            startActivity(NotificationsActivity::class.java)
        }

        // Profile card click listener
        binding.profileimagecard.setOnClickListener {
            startActivity(ProfileActivity::class.java)
        }

        // Quick action button click listeners (these already navigate to specific activities)
        binding.searchMoleculeButton.setOnClickListener {
            startActivity(MoleculesActivity::class.java)
        }

        binding.drugSimulationContainer.setOnClickListener {
            startActivity(Drug_simulationActivity::class.java)
        }

        binding.proteinsQuickButton.setOnClickListener {
            startActivity(ProteinsActivity::class.java)
        }

        binding.viewer3dButton.setOnClickListener {
            startActivity(Viewer3DActivity::class.java)
        }

        binding.runQSimulationButton.setOnClickListener {
            startActivity(RunQSimulationActivity::class.java)
        }

        binding.qmlPredictionButton.setOnClickListener {
            startActivity(QMLPredictionActivity::class.java)
        }

        binding.analyzePropertiesButton.setOnClickListener {
            startActivity(AnalysisActivity::class.java)
        }

        binding.medicationsQuickButton.setOnClickListener {
            startActivity(MedicinesActivity::class.java)
        }

        binding.toxicityCheckButton.setOnClickListener {
            startActivity(ToxicityCheckActivity::class.java)
        }

        binding.quantumEncodingButton.setOnClickListener {
            startActivity(QuantumEncodingActivity::class.java)
        }

        // Search container click to focus on EditText
        binding.searchContainer.setOnClickListener {
            binding.searchInput.requestFocus()
            // Optionally show keyboard (handled by system)
        }
    }

    /**
     * Sets up search functionality, including handling the Enter key.
     * This now determines the search category and starts SearchResultsActivity.
     */
    private fun setupSearchFunctionality() {
        binding.searchInput.setOnKeyListener { _, keyCode, event ->
            if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                val query = binding.searchInput.text.toString().trim()
                if (query.isNotEmpty()) {
                    val searchCategory = determineSearchCategory(query)
                    val intent = Intent(this, SearchActivity::class.java).apply {
                        putExtra("QUERY_EXTRA", query)
                        putExtra("SEARCH_CATEGORY_EXTRA", searchCategory)
                    }
                    startActivity(intent)
                    binding.searchInput.text.clear() // Clear search input after searching
                } else {
                    Toast.makeText(this, "Please enter a search query", Toast.LENGTH_SHORT).show()
                }
                true
            } else {
                false
            }
        }
    }

    /**
     * Determines the likely search category based on keywords in the query.
     * This is a simple example; a more robust solution might use a database lookup
     * or a more sophisticated NLP approach.
     */
    private fun determineSearchCategory(query: String): String {
        val lowerCaseQuery = query.lowercase()
        return when {
            lowerCaseQuery.contains("molecule") || lowerCaseQuery.contains("compound") || lowerCaseQuery.contains("chemical") ||
                    lowerCaseQuery.contains("h2o") || lowerCaseQuery.contains("ethanol") ||
                    lowerCaseQuery.contains("glucose") || lowerCaseQuery.contains("methane") ||
                    lowerCaseQuery.contains("acid") -> "molecules"
            lowerCaseQuery.contains("protein") || lowerCaseQuery.contains("enzyme") || lowerCaseQuery.contains("peptide") ||
                    lowerCaseQuery.contains("hemoglobin") || lowerCaseQuery.contains("insulin") ||
                    lowerCaseQuery.contains("amylase") || lowerCaseQuery.contains("collagen") ||
                    lowerCaseQuery.contains("antibody") -> "proteins"
            lowerCaseQuery.contains("medication") || lowerCaseQuery.contains("drug") || lowerCaseQuery.contains("medicine") ||
                    lowerCaseQuery.contains("pharmacy") || lowerCaseQuery.contains("paracetamol") ||
                    lowerCaseQuery.contains("amoxicillin") || lowerCaseQuery.contains("aspirin") ||
                    lowerCaseQuery.contains("metformin") || lowerCaseQuery.contains("ibuprofen") -> "medications"
            lowerCaseQuery.contains("toxic") || lowerCaseQuery.contains("poison") || lowerCaseQuery.contains("toxicity") ||
                    lowerCaseQuery.contains("cyanide") || lowerCaseQuery.contains("arsenic") ||
                    lowerCaseQuery.contains("lead") || lowerCaseQuery.contains("methanol") ||
                    lowerCaseQuery.contains("botulinum") -> "toxicity"
            else -> "general" // Default or if no specific category is found
        }
    }

    /**
     * Sets up tooltips for quick action buttons, ensuring compatibility with APIs below 26.
     */
    private fun setupTooltips() {
        // Map of buttons to their string resource IDs
        val tooltipMap = mapOf(
            binding.searchMoleculeButton to R.string.search_molecule,
            binding.proteinsQuickButton to R.string.Proteins,
            binding.viewer3dButton to R.string.molecule_3d_viewer,
            binding.runQSimulationButton to R.string.run_q_simulation,
            binding.qmlPredictionButton to R.string.qml_prediction,
            binding.analyzePropertiesButton to R.string.analyze_properties,
            binding.medicationsQuickButton to R.string.medications,
            binding.toxicityCheckButton to R.string.toxicity_check,
            binding.quantumEncodingButton to R.string.quantum_encoding
        )

        tooltipMap.forEach { (view, stringResId) ->
            ViewCompat.setTooltipText(view, getString(stringResId))
        }
    }

    /**
     * Helper method to start activities with error handling.
     */
    private fun startActivity(activityClass: Class<*>) {
        try {
            val intent = Intent(this, activityClass)
            startActivity(intent)
        } catch (e: Exception) {
            Log.e("Dashboard", "Error starting activity: ${activityClass.simpleName}", e)
            Toast.makeText(this, "Failed to open ${activityClass.simpleName}", Toast.LENGTH_SHORT).show()
        }
    }
}
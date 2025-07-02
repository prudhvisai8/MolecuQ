// app/src/main/java/com/quantum/molecuq/SearchResultsActivity.kt

package com.quantum.molecuq

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class SearchActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        val query = intent.getStringExtra("QUERY_EXTRA")
        val searchCategory = intent.getStringExtra("SEARCH_CATEGORY_EXTRA")

        val resultsTextView: TextView = findViewById(R.id.resultsTextView)

        if (query != null && searchCategory != null) {
            resultsTextView.text = "Search results for: '$query' (Category: $searchCategory)\n\n"
            displaySearchResults(query, searchCategory, resultsTextView)
        } else {
            resultsTextView.text = "No search query provided or category missing."
        }
    }

    /**
     * Displays sample search results based on the query and category.
     * In a real application, this would involve fetching data from a database/API.
     */
    private fun displaySearchResults(query: String, category: String, textView: TextView) {
        val resultsBuilder = StringBuilder()
        val lowerCaseQuery = query.lowercase()

        when (category) {
            "molecules" -> {
                val molecules = getSampleMolecules()
                val filtered = molecules.filter {
                    it.name.lowercase().contains(lowerCaseQuery) ||
                            it.formula.lowercase().contains(lowerCaseQuery) ||
                            it.description.lowercase().contains(lowerCaseQuery)
                }
                if (filtered.isNotEmpty()) {
                    resultsBuilder.append("Found Molecules:\n")
                    filtered.forEach {
                        resultsBuilder.append("- ${it.name} (${it.formula}): ${it.description}\n")
                    }
                } else {
                    resultsBuilder.append("No molecules found matching '$query'.\n")
                }
            }
            "proteins" -> {
                val proteins = getSampleProteins()
                val filtered = proteins.filter {
                    it.name.lowercase().contains(lowerCaseQuery) ||
                            it.function.lowercase().contains(lowerCaseQuery)
                }
                if (filtered.isNotEmpty()) {
                    resultsBuilder.append("Found Proteins:\n")
                    filtered.forEach {
                        resultsBuilder.append("- ${it.name}: ${it.function}\n")
                    }
                } else {
                    resultsBuilder.append("No proteins found matching '$query'.\n")
                }
            }
            "medications" -> {
                val medications = getSampleMedications()
                val filtered = medications.filter {
                    it.name.lowercase().contains(lowerCaseQuery) ||
                            it.uses.lowercase().contains(lowerCaseQuery) ||
                            it.sideEffects.lowercase().contains(lowerCaseQuery)
                }
                if (filtered.isNotEmpty()) {
                    resultsBuilder.append("Found Medications:\n")
                    filtered.forEach {
                        resultsBuilder.append("- ${it.name}: Used for ${it.uses}. Side effects: ${it.sideEffects}\n")
                    }
                } else {
                    resultsBuilder.append("No medications found matching '$query'.\n")
                }
            }
            "toxicity" -> {
                val toxicSubstances = getSampleToxicityInfo()
                val filtered = toxicSubstances.filter {
                    it.name.lowercase().contains(lowerCaseQuery) ||
                            it.symptoms.lowercase().contains(lowerCaseQuery) ||
                            it.treatment.lowercase().contains(lowerCaseQuery)
                }
                if (filtered.isNotEmpty()) {
                    resultsBuilder.append("Found Toxicity Information:\n")
                    filtered.forEach {
                        resultsBuilder.append("- ${it.name}: Symptoms - ${it.symptoms}. Treatment - ${it.treatment}\n")
                    }
                } else {
                    resultsBuilder.append("No toxicity information found matching '$query'.\n")
                }
            }
            else -> { // General search, could search across all or none
                resultsBuilder.append("General search results for '$query'. Please try a more specific query for detailed results.\n")
                // You could optionally combine filters from all categories here for a general search
            }
        }
        if (resultsBuilder.isEmpty()) {
            resultsBuilder.append("No specific results found for '$query' in category '$category'.")
        }
        textView.append(resultsBuilder.toString())
    }

    // --- Sample Data Classes ---
    data class Molecule(val name: String, val formula: String, val description: String)
    data class Protein(val name: String, val function: String)
    data class Medication(val name: String, val uses: String, val sideEffects: String)
    data class ToxicityInfo(val name: String, val symptoms: String, val treatment: String)

    // --- Sample Data Lists ---
    private fun getSampleMolecules(): List<Molecule> {
        return listOf(
            Molecule("Water", "H2O", "Essential for life, universal solvent."),
            Molecule("Ethanol", "C2H5OH", "An alcohol used in beverages and as a solvent."),
            Molecule("Glucose", "C6H12O6", "A simple sugar, important energy source."),
            Molecule("Methane", "CH4", "Simplest hydrocarbon, a potent greenhouse gas."),
            Molecule("Sulfuric Acid", "H2SO4", "Highly corrosive mineral acid.")
        )
    }

    private fun getSampleProteins(): List<Protein> {
        return listOf(
            Protein("Hemoglobin", "Transports oxygen in blood."),
            Protein("Insulin", "Regulates blood sugar levels."),
            Protein("Amylase", "Breaks down starches into sugars."),
            Protein("Collagen", "Provides structural support to tissues."),
            Protein("Antibody", "Identifies and neutralizes foreign objects.")
        )
    }

    private fun getSampleMedications(): List<Medication> {
        return listOf(
            Medication("Paracetamol", "Pain relief, fever reduction.", "Liver damage in overdose."),
            Medication("Amoxicillin", "Bacterial infections.", "Nausea, diarrhea, allergic reactions."),
            Medication("Aspirin", "Pain, inflammation, fever, blood thinning.", "Stomach upset, bleeding."),
            Medication("Metformin", "Type 2 diabetes.", "Diarrhea, nausea, gas."),
            Medication("Ibuprofen", "Pain, inflammation, fever.", "Stomach upset, kidney problems.")
        )
    }

    private fun getSampleToxicityInfo(): List<ToxicityInfo> {
        return listOf(
            ToxicityInfo("Cyanide", "Headache, dizziness, rapid breathing, convulsions, coma.", "Administer amyl nitrite, sodium nitrite, sodium thiosulfate."),
            ToxicityInfo("Arsenic", "Vomiting, diarrhea, abdominal pain, muscle cramps, abnormal heart rhythm.", "Chelation therapy with dimercaprol."),
            ToxicityInfo("Lead", "Abdominal pain, constipation, headaches, irritability, developmental delay in children.", "Chelation therapy with EDTA or DMSA."),
            ToxicityInfo("Methanol", "Headache, dizziness, nausea, vomiting, blurred vision, blindness, metabolic acidosis.", "Administer fomepizole or ethanol, hemodialysis."),
            ToxicityInfo("Botulinum Toxin", "Muscle weakness, double vision, drooping eyelids, difficulty swallowing, speech problems, paralysis.", "Antitoxin administration.")
        )
    }
}
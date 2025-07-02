package com.quantum.molecuq

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.LayoutInflater
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Locale // Import Locale

class Viewer3DActivity : AppCompatActivity() {

    private lateinit var searchEditText: EditText
    private lateinit var searchButton: Button
    private lateinit var resultsRecyclerView: RecyclerView
    private lateinit var detailCardView: CardView
    private lateinit var moleculeNameTextView: TextView
    private lateinit var iupacNameTextView: TextView
    private lateinit var formulaTextView: TextView
    private lateinit var molecularWeightTextView: TextView
    private lateinit var descriptionContentTextView: TextView
    private lateinit var toxicityContentTextView: TextView
    private lateinit var structure2dImageView: ImageView
    private lateinit var closeDetailButton: Button
    private lateinit var container3dView: FrameLayout

    // Updated Molecule data class (remains the same)
    data class Molecule(
        val id: String,
        val name: String,
        val formula: String,
        val iupacName: String?,
        val molecularWeight: Double?,
        val smiles: String?,
        val description: String?,
        val toxicityInfo: String?,
        val imageUrl: String?
    )

    // Dummy adapter for RecyclerView (remains the same)
    class MoleculeAdapter(private val molecules: List<Molecule>, private val onItemClick: (Molecule) -> Unit) :
        RecyclerView.Adapter<MoleculeAdapter.MoleculeViewHolder>() {

        class MoleculeViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val nameTextView: TextView = view.findViewById(android.R.id.text1)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MoleculeViewHolder {
            val textView = LayoutInflater.from(parent.context).inflate(android.R.layout.simple_list_item_1, parent, false) as TextView
            return MoleculeViewHolder(textView)
        }

        override fun onBindViewHolder(holder: MoleculeViewHolder, position: Int) {
            val molecule = molecules[position]
            holder.nameTextView.text = molecule.name
            holder.itemView.setOnClickListener { onItemClick(molecule) }
        }

        override fun getItemCount() = molecules.size
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_viewer3_dactivity)

        // Initialize views (remains the same)
        searchEditText = findViewById(R.id.search_edit_text)
        searchButton = findViewById(R.id.search_button)
        resultsRecyclerView = findViewById(R.id.results_recycler_view)
        detailCardView = findViewById(R.id.detail_card_view)
        moleculeNameTextView = findViewById(R.id.molecule_name_text_view)
        iupacNameTextView = findViewById(R.id.iupac_name_text_view)
        formulaTextView = findViewById(R.id.formula_text_view)
        molecularWeightTextView = findViewById(R.id.molecular_weight_text_view)
        descriptionContentTextView = findViewById(R.id.description_content_text_view)
        toxicityContentTextView = findViewById(R.id.toxicity_content_text_view)
        structure2dImageView = findViewById(R.id.structure_2d_image_view)
        closeDetailButton = findViewById(R.id.close_detail_button)
        container3dView = findViewById(R.id.container_3d_view)

        resultsRecyclerView.layoutManager = LinearLayoutManager(this)

        searchButton.setOnClickListener {
            performSearch(searchEditText.text.toString())
        }

        searchEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                performSearch(searchEditText.text.toString())
                true
            } else {
                false
            }
        }

        closeDetailButton.setOnClickListener {
            detailCardView.visibility = View.GONE
            resultsRecyclerView.visibility = View.VISIBLE
        }
    }

    private fun performSearch(query: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val results = fetchMoleculesFromApi(query)
            withContext(Dispatchers.Main) {
                resultsRecyclerView.adapter = MoleculeAdapter(results) { molecule ->
                    showMoleculeDetails(molecule)
                }
            }
        }
    }

    // Corrected fetchMoleculesFromApi using lowercase()
    private suspend fun fetchMoleculesFromApi(query: String): List<Molecule> {
        kotlinx.coroutines.delay(1000) // Simulate network delay
        val lowerCaseQuery = query.lowercase(Locale.getDefault()) // Use lowercase(Locale)

        return listOf(
            Molecule(
                id = "1",
                name = "Water",
                formula = "H₂O",
                iupacName = "Dihydrogen monoxide",
                molecularWeight = 18.015,
                smiles = "O",
                description = "Water is a transparent, tasteless, odorless, and nearly colorless chemical substance, which is the main constituent of Earth's hydrosphere and the fluids of all known living organisms.",
                toxicityInfo = "While essential for life, excessive intake of water can lead to water intoxication (hyponatremia).",
                imageUrl = "https://upload.wikimedia.org/wikipedia/commons/thumb/f/f2/Water-3D-vdW.png/220px-Water-3D-vdW.png"
            ),
            Molecule(
                id = "2",
                name = "Ethanol",
                formula = "C₂H₆O",
                iupacName = "Ethanol",
                molecularWeight = 46.069,
                smiles = "CCO",
                description = "Ethanol is a volatile, flammable, colorless liquid with a characteristic odor. It is a psychoactive drug and one of the oldest recreational drugs.",
                toxicityInfo = "Consumption of ethanol can cause intoxication, impaired judgment, and various health issues, including liver damage and addiction, depending on dose and frequency.",
                imageUrl = "https://upload.wikimedia.org/wikipedia/commons/thumb/c/cd/Ethanol_ball_and_stick.png/220px-Ethanol_ball_and_stick.png"
            ),
            Molecule(
                id = "3",
                name = "Aspirin",
                formula = "C₉H₈O₄",
                iupacName = "2-Acetoxybenzoic acid",
                molecularWeight = 180.159,
                smiles = "CC(=O)Oc1ccccc1C(=O)O",
                description = "Aspirin is a salicylate drug, often used as an analgesic to relieve minor aches and pains, as an antipyretic to reduce fever, and as an anti-inflammatory medication.",
                toxicityInfo = "Common side effects include indigestion, heartburn, and nausea. More serious side effects can include gastrointestinal bleeding, ulcers, and ringing in the ears.",
                imageUrl = "https://upload.wikimedia.org/wikipedia/commons/thumb/2/22/Aspirin_structure.png/220px-Aspirin_structure.png"
            ),
            Molecule(
                id = "4",
                name = "Caffeine",
                formula = "C₈H₁₀N₄O₂",
                iupacName = "1,3,7-Trimethyl-1H-purine-2,6(3H,7H)-dione",
                molecularWeight = 194.19,
                smiles = "Cn1cnc2c1c(=O)n(c(=O)n2C)C",
                description = "Caffeine is a central nervous system (CNS) stimulant of the methylxanthine class. It is the world's most widely consumed psychoactive drug.",
                toxicityInfo = "Can cause restlessness, nervousness, insomnia, tremors, and rapid heartbeat. High doses can lead to caffeine intoxication, and in extreme cases, overdose.",
                imageUrl = "https://upload.wikimedia.org/wikipedia/commons/thumb/6/6d/Caffeine_structure.png/220px-Caffeine_structure.png"
            )
        ).filter {
            it.name.lowercase(Locale.getDefault()).contains(lowerCaseQuery) || // Updated
                    it.formula.lowercase(Locale.getDefault()).contains(lowerCaseQuery) || // Updated
                    (it.iupacName?.lowercase(Locale.getDefault())?.contains(lowerCaseQuery) == true) || // Updated
                    (it.description?.lowercase(Locale.getDefault())?.contains(lowerCaseQuery) == true) || // Updated
                    (it.toxicityInfo?.lowercase(Locale.getDefault())?.contains(lowerCaseQuery) == true) // Updated
        }
    }

    // showMoleculeDetails (remains the same as the previous update)
    private fun showMoleculeDetails(molecule: Molecule) {
        moleculeNameTextView.text = molecule.name
        iupacNameTextView.text = "IUPAC Name: ${molecule.iupacName ?: "N/A"}"
        formulaTextView.text = "Formula: ${molecule.formula}"
        molecularWeightTextView.text = "Molecular Weight: ${molecule.molecularWeight?.toString() ?: "N/A"} g/mol"
        descriptionContentTextView.text = molecule.description ?: "No description available."
        toxicityContentTextView.text = molecule.toxicityInfo ?: "No toxicity information available."

        if (!molecule.imageUrl.isNullOrEmpty()) {
            Glide.with(this)
                .load(molecule.imageUrl)
                .placeholder(android.R.drawable.ic_menu_gallery)
                .error(android.R.drawable.ic_delete)
                .into(structure2dImageView)
        } else {
            structure2dImageView.setImageDrawable(null)
        }

        resultsRecyclerView.visibility = View.GONE
        detailCardView.visibility = View.VISIBLE
    }
}
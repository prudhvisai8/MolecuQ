package com.quantum.molecuq

import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.cardview.widget.CardView
import java.io.Serializable

class ToxicityCheckActivity : AppCompatActivity() {

    data class Toxication(
        val name: String,
        val formula: String,
        val symptoms: String,
        val exposureSources: String,
        val antidote: String,
        val severity: String,
        val chemicalGroup: String,
        val discoveryYear: String
    ) : Serializable

    private val toxications = listOf(
        Toxication("Benzene", "C6H6", "Seizures", "Cigarette Smoke", "Antioxidants", "High", "Solvent", "1825 AD"),
        Toxication("Formaldehyde", "CH2O", "Fatigue", "Cosmetics", "BAL", "Low", "Solvent", "1859 AD"),
        Toxication("Chloroform", "CHCl3", "Coma", "Cosmetics", "No known antidote", "Severe", "Solvent", "1831 AD"),
        Toxication("Cadmium", "Cd", "Organ failure", "Batteries", "Dialysis", "High", "Metal", "1817 AD"),
        Toxication("Thallium", "Tl", "Coma", "Cosmetics", "Activated Charcoal", "Severe", "Metal", "1861 AD"),
        Toxication("Nickel", "Ni", "Nausea", "Batteries", "Activated Charcoal", "High", "Metal", "1751 AD"),
        Toxication("Chromium", "Cr", "Nausea", "Plastic Production", "Dialysis", "Low", "Metal", "1797 AD"),
        Toxication("Toluene", "C7H8", "Seizures", "Plastic Production", "Activated Charcoal", "Acute", "Solvent", "1838 AD"),
        Toxication("Xylene", "C8H10", "Respiratory distress", "Fertilizers", "Activated Charcoal", "Severe", "Solvent", "1850 AD"),
        Toxication("Hydrazine", "N2H4", "Fatigue", "Industrial Waste", "BAL", "Low", "Gas", "1875 AD"),
        Toxication("Benzene (Industrial)", "C6H6", "Fatigue", "Industrial Solvents", "Dialysis", "Acute", "Solvent", "1849 AD"),
        Toxication("Formaldehyde (Embalming)", "CH2O", "Muscle cramps", "Embalming Fluid", "Activated Charcoal", "Acute", "Preservative", "1789 AD"),
        Toxication("Chloroform (Anesthetic)", "CHCl3", "Organ failure", "Historical Anesthetics", "Antioxidants", "High", "Solvent", "1847 AD"),
        Toxication("Cadmium (Mining)", "Cd", "Fatigue", "Mining Byproducts", "Antioxidants", "Moderate", "Metal", "1817 AD"),
        Toxication("Thallium (Pesticide)", "Tl", "Seizures", "Historical Pesticides", "No known antidote", "Severe", "Metal", "1861 AD"),
        Toxication("Nickel (Alloy)", "Ni", "Seizures", "Metal Alloys", "Antioxidants", "Low", "Metal", "1751 AD"),
        Toxication("Chromium (Plating)", "Cr", "Nausea", "Electroplating", "Activated Charcoal", "Moderate", "Metal", "1797 AD"),
        Toxication("Toluene (Paint)", "C7H8", "Coma", "Paint Thinners", "Dialysis", "Severe", "Solvent", "1838 AD"),
        Toxication("Xylene (Fuel)", "C8H10", "Organ failure", "Fuel Additives", "No known antidote", "Severe", "Solvent", "1850 AD"),
        Toxication("Hydrazine (Rocket)", "N2H4", "Coma", "Rocket Fuel", "Antioxidants", "Low", "Gas", "1875 AD"),
        Toxication("Arsenic", "As", "Vomiting", "Pesticides", "BAL", "High", "Metalloid", "1250 AD"),
        Toxication("Lead", "Pb", "Neurological damage", "Paints", "EDTA", "Severe", "Metal", "Ancient"),
        Toxication("Mercury", "Hg", "Tremors", "Thermometers", "DMPS", "High", "Metal", "Ancient"),
        Toxication("Carbon Monoxide", "CO", "Headache", "Exhaust Fumes", "Oxygen Therapy", "Severe", "Gas", "1776 AD"),
        Toxication("Cyanide", "CN-", "Respiratory failure", "Industrial Chemicals", "Hydroxocobalamin", "Severe", "Inorganic", "1782 AD"),
        Toxication("Methanol", "CH3OH", "Blindness", "Adulterated Alcohol", "Fomepizole", "High", "Solvent", "1834 AD"),
        Toxication("Ethylene Glycol", "C2H6O2", "Kidney failure", "Antifreeze", "Fomepizole", "Severe", "Organic", "1856 AD"),
        Toxication("DDT", "C14H9Cl5", "Tremors", "Pesticides", "No known antidote", "Moderate", "Pesticide", "1874 AD"),
        Toxication("Sarin", "C4H10FO2P", "Respiratory distress", "Chemical Weapons", "Atropine", "Severe", "Organophosphate", "1938 AD"),
        Toxication("Botulinum Toxin", "N/A", "Paralysis", "Contaminated Food", "Antitoxin", "Severe", "Protein", "1897 AD"),
        Toxication("Aflatoxin B1", "C17H12O6", "Liver damage", "Moldy Grains", "No known antidote", "High", "Mycotoxin", "1960 AD"),
        Toxication("Ricin", "N/A", "Organ failure", "Castor Beans", "No known antidote", "Severe", "Protein", "1888 AD"),
        Toxication("Tetrodotoxin", "C11H17N3O8", "Paralysis", "Pufferfish", "No known antidote", "Severe", "Organic", "1909 AD"),
        Toxication("Dioxin", "C12H4Cl4O2", "Cancer", "Industrial Byproducts", "No known antidote", "High", "Organic", "1957 AD"),
        Toxication("Polychlorinated Biphenyls", "C12H10-xClx", "Skin lesions", "Electrical Equipment", "No known antidote", "Moderate", "Organic", "1865 AD"),
        Toxication("Acrylamide", "C3H5NO", "Neuropathy", "Processed Foods", "No known antidote", "Low", "Organic", "1950 AD"),
        Toxication("Vinyl Chloride", "C2H3Cl", "Liver damage", "Plastics", "No known antidote", "High", "Organic", "1835 AD"),
        Toxication("Ethyl Mercury", "C2H5Hg+", "Neurological damage", "Fungicides", "DMPS", "Severe", "Organic", "1865 AD"),
        Toxication("Strychnine", "C21H22N2O2", "Convulsions", "Pesticides", "Diazepam", "Severe", "Alkaloid", "1818 AD"),
        Toxication("Nicotine", "C10H14N2", "Nausea", "Tobacco", "Activated Charcoal", "Moderate", "Alkaloid", "1828 AD"),
        Toxication("Atrazine", "C8H14ClN5", "Endocrine disruption", "Herbicides", "No known antidote", "Moderate", "Pesticide", "1958 AD"),
        Toxication("Paraquat", "C12H14N2", "Lung damage", "Herbicides", "No known antidote", "Severe", "Organic", "1882 AD"),
        Toxication("Glyphosate", "C3H8NO5P", "Gastrointestinal distress", "Herbicides", "Supportive Care", "Low", "Organic", "1970 AD"),
        Toxication("Beryllium", "Be", "Lung disease", "Aerospace Materials", "No known antidote", "High", "Metal", "1798 AD"),
        Toxication("Asbestos", "Mg3Si2O5(OH)4", "Cancer", "Construction Materials", "No known antidote", "Severe", "Mineral", "Ancient"),
        Toxication("Methyl Isocyanate", "C2H3NO", "Respiratory failure", "Industrial Chemicals", "No known antidote", "Severe", "Organic", "1888 AD"),
        Toxication("Phosgene", "COCl2", "Lung damage", "Chemical Weapons", "Supportive Care", "Severe", "Gas", "1812 AD"),
        Toxication("Mustard Gas", "C4H8Cl2S", "Skin burns", "Chemical Weapons", "No known antidote", "Severe", "Organic", "1860 AD"),
        Toxication("Ammonia", "NH3", "Respiratory irritation", "Fertilizers", "Supportive Care", "Moderate", "Gas", "1774 AD"),
        Toxication("Chlorine", "Cl2", "Respiratory distress", "Disinfectants", "Oxygen Therapy", "Severe", "Gas", "1774 AD"),
        Toxication("Hydrofluoric Acid", "HF", "Tissue damage", "Industrial Chemicals", "Calcium Gluconate", "High", "Inorganic", "Ancient"),
        Toxication("Methanol (Industrial)", "CH3OH", "Blindness", "Industrial Solvents", "Fomepizole", "High", "Solvent", "1834 AD"),
        Toxication("Acetaldehyde", "C2H4O", "Nausea", "Industrial Chemicals", "Supportive Care", "Moderate", "Organic", "1835 AD"),
        Toxication("Carbon Tetrachloride", "CCl4", "Liver damage", "Solvents", "No known antidote", "High", "Solvent", "1839 AD"),
        Toxication("Trichloroethylene", "C2HCl3", "Neurological damage", "Degreasers", "Supportive Care", "Moderate", "Solvent", "1864 AD"),
        Toxication("Perchloroethylene", "C2Cl4", "Dizziness", "Dry Cleaning", "Supportive Care", "Moderate", "Solvent", "1821 AD"),
        Toxication("Ethylbenzene", "C8H10", "Headache", "Fuels", "Supportive Care", "Low", "Solvent", "1836 AD"),
        Toxication("Styrene", "C8H8", "Irritation", "Plastics", "Supportive Care", "Moderate", "Organic", "1839 AD"),
        Toxication("Acrolein", "C3H4O", "Respiratory irritation", "Combustion Byproducts", "Supportive Care", "High", "Organic", "1838 AD"),
        Toxication("Bisphenol A", "C15H16O2", "Endocrine disruption", "Plastics", "No known antidote", "Moderate", "Organic", "1891 AD"),
        Toxication("Phthalates", "C6H4(COOR)2", "Reproductive issues", "Plastics", "No known antidote", "Low", "Organic", "1856 AD"),
        Toxication("Methylene Chloride", "CH2Cl2", "Dizziness", "Paint Strippers", "Supportive Care", "Moderate", "Solvent", "1839 AD"),
        Toxication("Formalin", "CH2O", "Eye irritation", "Disinfectants", "Supportive Care", "Low", "Preservative", "1859 AD"),
        Toxication("Dieldrin", "C12H8Cl6O", "Convulsions", "Pesticides", "No known antidote", "Severe", "Pesticide", "1948 AD"),
        Toxication("Aldrin", "C12H8Cl6", "Seizures", "Pesticides", "No known antidote", "Severe", "Pesticide", "1948 AD"),
        Toxication("Endrin", "C12H8Cl6O", "Neurological damage", "Pesticides", "No known antidote", "Severe", "Pesticide", "1950 AD"),
        Toxication("Chlordane", "C10H6Cl8", "Liver damage", "Pesticides", "No known antidote", "High", "Pesticide", "1945 AD"),
        Toxication("Heptachlor", "C10H5Cl7", "Tremors", "Pesticides", "No known antidote", "High", "Pesticide", "1948 AD"),
        Toxication("Toxaphene", "C10H10Cl8", "Convulsions", "Pesticides", "No known antidote", "Severe", "Pesticide", "1947 AD"),
        Toxication("Lindane", "C6H6Cl6", "Seizures", "Pesticides", "No known antidote", "High", "Pesticide", "1942 AD"),
        Toxication("Malathion", "C10H19O6PS2", "Respiratory distress", "Pesticides", "Atropine", "Moderate", "Organophosphate", "1950 AD"),
        Toxication("Parathion", "C10H14NO5PS", "Paralysis", "Pesticides", "Atropine", "Severe", "Organophosphate", "1947 AD"),
        Toxication("Diazinon", "C12H21N2O3PS", "Nausea", "Pesticides", "Atropine", "Moderate", "Organophosphate", "1952 AD"),
        Toxication("Chlorpyrifos", "C9H11Cl3NO3PS", "Neurological damage", "Pesticides", "Atropine", "High", "Organophosphate", "1965 AD"),
        Toxication("Strychnine (Rodenticide)", "C21H22N2O2", "Convulsions", "Rodenticides", "Diazepam", "Severe", "Alkaloid", "1818 AD"),
        Toxication("Warfarin", "C19H16O4", "Bleeding", "Rodenticides", "Vitamin K", "Moderate", "Organic", "1948 AD"),
        Toxication("Bromadiolone", "C30H23BrO4", "Bleeding", "Rodenticides", "Vitamin K", "High", "Organic", "1976 AD"),
        Toxication("Aconitine", "C34H47NO11", "Heart arrhythmia", "Aconite Plant", "No known antidote", "Severe", "Alkaloid", "1831 AD"),
        Toxication("Colchicine", "C22H25NO6", "Organ failure", "Colchicum Plant", "No known antidote", "Severe", "Alkaloid", "1820 AD"),
        Toxication("Atropine", "C17H23NO3", "Delirium", "Belladonna Plant", "Physostigmine", "High", "Alkaloid", "1819 AD"),
        Toxication("Digoxin", "C41H64O14", "Heart arrhythmia", "Foxglove Plant", "Digoxin Immune Fab", "Severe", "Glycoside", "1785 AD"),
        Toxication("Saxitoxin", "C10H17N7O4", "Paralysis", "Shellfish", "No known antidote", "Severe", "Organic", "1927 AD"),
        Toxication("Batrachotoxin", "C31H42N2O6", "Heart failure", "Poison Dart Frog", "No known antidote", "Severe", "Alkaloid", "1965 AD"),
        Toxication("T-2 Toxin", "C24H34O9", "Immune suppression", "Mold", "No known antidote", "High", "Mycotoxin", "1961 AD"),
        Toxication("Ochratoxin A", "C20H18ClNO6", "Kidney damage", "Mold", "No known antidote", "Moderate", "Mycotoxin", "1965 AD"),
        Toxication("Zearalenone", "C18H22O5", "Reproductive issues", "Mold", "No known antidote", "Low", "Mycotoxin", "1966 AD"),
        Toxication("Fumonisin B1", "C34H59NO15", "Liver damage", "Mold", "No known antidote", "Moderate", "Mycotoxin", "1988 AD"),
        Toxication("Deoxynivalenol", "C15H20O6", "Vomiting", "Mold", "No known antidote", "Low", "Mycotoxin", "1973 AD"),
        Toxication("Patulin", "C7H6O4", "Gastrointestinal distress", "Mold", "No known antidote", "Low", "Mycotoxin", "1943 AD"),
        Toxication("Aniline", "C6H7N", "Methemoglobinemia", "Dyes", "Methylene Blue", "High", "Organic", "1826 AD"),
        Toxication("Phenol", "C6H6O", "Tissue damage", "Disinfectants", "Supportive Care", "High", "Organic", "1834 AD"),
        Toxication("Ethanol (Toxic Dose)", "C2H5OH", "Coma", "Alcohol", "Supportive Care", "Moderate", "Organic", "Ancient"),
        Toxication("Acetaminophen (Overdose)", "C8H9NO2", "Liver failure", "Medications", "N-Acetylcysteine", "Severe", "Organic", "1877 AD")

    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (intent.hasExtra("Toxication")) {
            showDetail(intent.getSerializableExtra("Toxication") as Toxication)
        } else {
            showSearch()
        }
    }

    private fun showSearch() {
        setContentView(R.layout.activity_toxications)

        val searchView = findViewById<SearchView>(R.id.searchView)
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerViewToxications)
        val backBtn = findViewById<ImageView>(R.id.backButton)

        val adapter = ToxicationAdapter(toxications) { selected ->
            val intent = Intent(this, ToxicityCheckActivity::class.java)
            intent.putExtra("Toxication", selected)
            startActivity(intent)
        }

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?) = false
            override fun onQueryTextChange(query: String?): Boolean {
                adapter.filter(query ?: "")
                return true
            }
        })

        backBtn.setOnClickListener { finish() }
    }

    private fun showDetail(tox: Toxication) {
        setContentView(R.layout.activity_toxin_detail)

        findViewById<TextView>(R.id.titleText).text = tox.name
        findViewById<TextView>(R.id.formulaText).text = "Formula: ${tox.formula}"
        findViewById<TextView>(R.id.symptomsText).text = "Symptoms: ${tox.symptoms}"
        findViewById<TextView>(R.id.exposureSourcesText).text = "Exposure Sources: ${tox.exposureSources}"
        findViewById<TextView>(R.id.antidoteText).text = "Antidote: ${tox.antidote}"
        findViewById<TextView>(R.id.severityText).text = "Severity: ${tox.severity}"
        findViewById<TextView>(R.id.chemicalGroupText).text = "Chemical Group: ${tox.chemicalGroup}"
        findViewById<TextView>(R.id.discoveryText).text = "Discovery Year: ${tox.discoveryYear}"

        findViewById<ImageView>(R.id.backButton).setOnClickListener { finish() }
    }

    class ToxicationAdapter(
        private val fullList: List<Toxication>,
        private val onItemClick: (Toxication) -> Unit
    ) : RecyclerView.Adapter<ToxicationAdapter.ToxiViewHolder>() {

        private var filteredList = fullList.toMutableList()

        inner class ToxiViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val nameText: TextView = view.findViewById(R.id.toxicationName)
            val card: CardView = view.findViewById(R.id.cardRoot)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ToxiViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_toxication, parent, false)
            return ToxiViewHolder(view)
        }

        override fun onBindViewHolder(holder: ToxiViewHolder, position: Int) {
            val tox = filteredList[position]
            holder.nameText.text = tox.name
            holder.card.setOnClickListener { onItemClick(tox) }
        }

        override fun getItemCount(): Int = filteredList.size

        fun filter(query: String) {
            filteredList = fullList.filter {
                it.name.contains(query, ignoreCase = true)
            }.toMutableList()
            notifyDataSetChanged()
        }
    }
}

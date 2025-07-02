package com.quantum.molecuq

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.*
import androidx.cardview.widget.CardView
import java.io.Serializable

class MoleculesActivity : AppCompatActivity() {

    data class Molecule(
        val name: String,
        val formula: String,
        val molecularWeight: String,
        val structure: String,
        val type: String,
        val discoveryYear: String,
        val usage: String,
        val bonds: String
    ) : Serializable

    private val molecules = listOf(
        Molecule("Water", "H2O", "18.02 g/mol", "Bent", "Inorganic", "Ancient", "Solvent, Life", "2 Covalent"),
        Molecule("Ethanol", "C2H5OH", "46.07 g/mol", "Tetrahedral", "Organic", "1855", "Alcohol, Fuel", "Single Bonds"),
        Molecule("Benzene", "C6H6", "78.11 g/mol", "Ring", "Aromatic", "1825", "Solvent, Precursor", "Double Alternating"),
        Molecule("Glucose", "C6H12O6", "180.16 g/mol", "Cyclic", "Carbohydrate", "1747", "Energy, Biology", "Covalent"),
        Molecule("Methane", "CH4", "16.04 g/mol", "Tetrahedral", "Hydrocarbon", "1776", "Fuel", "Single Bonds"),
        Molecule("Carbon Dioxide", "CO2", "44.01 g/mol", "Linear", "Inorganic", "1638", "Photosynthesis, Industry", "Double Bonds"),
        Molecule("Ammonia", "NH3", "17.03 g/mol", "Trigonal Pyramidal", "Inorganic", "1774", "Fertilizer, Cleaner", "Covalent"),
        Molecule("Ethylene", "C2H4", "28.05 g/mol", "Planar", "Hydrocarbon", "1795", "Ripening Agent", "Double Bond"),
        Molecule("Acetone", "C3H6O", "58.08 g/mol", "Trigonal Planar", "Organic", "1832", "Solvent, Nail Polish Remover", "Covalent"),
        Molecule("Formaldehyde", "CH2O", "30.03 g/mol", "Trigonal Planar", "Organic", "1859", "Preservative", "Covalent"),
        Molecule("Sulfuric Acid", "H2SO4", "98.08 g/mol", "Tetrahedral", "Inorganic", "Ancient", "Industrial Acid", "Covalent"),
        Molecule("Methanol", "CH3OH", "32.04 g/mol", "Tetrahedral", "Organic", "1834", "Fuel, Solvent", "Single Bonds"),
        Molecule("Acetic Acid", "CH3COOH", "60.05 g/mol", "Tetrahedral", "Organic", "Ancient", "Vinegar, Chemical Synthesis", "Covalent"),
        Molecule("Propane", "C3H8", "44.10 g/mol", "Tetrahedral", "Hydrocarbon", "1910", "Fuel", "Single Bonds"),
        Molecule("Butane", "C4H10", "58.12 g/mol", "Tetrahedral", "Hydrocarbon", "1910", "Fuel, Lighter Fluid", "Single Bonds"),
        Molecule("Chloroform", "CHCl3", "119.38 g/mol", "Tetrahedral", "Organic", "1831", "Solvent, Anesthetic", "Covalent"),
        Molecule("Toluene", "C7H8", "92.14 g/mol", "Ring", "Aromatic", "1838", "Solvent, Fuel Additive", "Covalent"),
        Molecule("Urea", "CH4N2O", "60.06 g/mol", "Planar", "Organic", "1773", "Fertilizer, Resin", "Covalent"),
        Molecule("Nitric Acid", "HNO3", "63.01 g/mol", "Trigonal Planar", "Inorganic", "Ancient", "Explosives, Fertilizer", "Covalent"),
        Molecule("Ethane", "C2H6", "30.07 g/mol", "Tetrahedral", "Hydrocarbon", "1834", "Fuel", "Single Bonds"),
        Molecule("Acetylene", "C2H2", "26.04 g/mol", "Linear", "Hydrocarbon", "1836", "Welding, Chemical Synthesis", "Triple Bond"),
        Molecule("Glycerol", "C3H8O3", "92.09 g/mol", "Tetrahedral", "Organic", "1779", "Cosmetics, Food", "Covalent"),
        Molecule("Hydrochloric Acid", "HCl", "36.46 g/mol", "Linear", "Inorganic", "Ancient", "Industrial Acid", "Covalent"),
        Molecule("Fructose", "C6H12O6", "180.16 g/mol", "Cyclic", "Carbohydrate", "1847", "Sweetener, Biology", "Covalent"),
        Molecule("Sucrose", "C12H22O11", "342.30 g/mol", "Cyclic", "Carbohydrate", "1857", "Sweetener", "Covalent"),
        Molecule("Lactic Acid", "C3H6O3", "90.08 g/mol", "Tetrahedral", "Organic", "1780", "Food, Biodegradable Plastics", "Covalent"),
        Molecule("Caffeine", "C8H10N4O2", "194.19 g/mol", "Planar", "Organic", "1819", "Stimulant", "Covalent"),
        Molecule("Aspirin", "C9H8O4", "180.16 g/mol", "Planar", "Organic", "1897", "Pain Relief", "Covalent"),
        Molecule("Naphthalene", "C10H8", "128.17 g/mol", "Ring", "Aromatic", "1821", "Mothballs, Chemical Synthesis", "Double Alternating"),
        Molecule("Phenol", "C6H6O", "94.11 g/mol", "Ring", "Aromatic", "1834", "Antiseptic, Plastics", "Covalent"),
        Molecule("Carbon Monoxide", "CO", "28.01 g/mol", "Linear", "Inorganic", "1776", "Industrial Gas", "Triple Bond"),
        Molecule("Hydrogen Peroxide", "H2O2", "34.01 g/mol", "Bent", "Inorganic", "1818", "Bleach, Disinfectant", "Covalent"),
        Molecule("Formic Acid", "HCOOH", "46.03 g/mol", "Trigonal Planar", "Organic", "1670", "Preservative, Leather", "Covalent"),
        Molecule("Ethyl Acetate", "C4H8O2", "88.11 g/mol", "Tetrahedral", "Organic", "1840", "Solvent, Nail Polish", "Covalent"),
        Molecule("Aniline", "C6H7N", "93.13 g/mol", "Ring", "Aromatic", "1826", "Dyes, Pharmaceuticals", "Covalent"),
        Molecule("Dimethyl Ether", "C2H6O", "46.07 g/mol", "Bent", "Organic", "1834", "Propellant, Fuel", "Covalent"),
        Molecule("Propanol", "C3H8O", "60.10 g/mol", "Tetrahedral", "Organic", "1853", "Solvent, Antiseptic", "Single Bonds"),
        Molecule("Benzaldehyde", "C7H6O", "106.12 g/mol", "Ring", "Aromatic", "1832", "Flavoring, Chemical Synthesis", "Covalent"),
        Molecule("Acrylonitrile", "C3H3N", "53.06 g/mol", "Planar", "Organic", "1893", "Plastics, Fibers", "Covalent"),
        Molecule("Cyclohexane", "C6H12", "84.16 g/mol", "Ring", "Hydrocarbon", "1899", "Solvent", "Single Bonds"),
        Molecule("Pyridine", "C5H5N", "79.10 g/mol", "Ring", "Aromatic", "1849", "Solvent, Chemical Synthesis", "Covalent"),
        Molecule("Limonene", "C10H16", "136.24 g/mol", "Ring", "Terpene", "1840", "Fragrance, Solvent", "Covalent"),
        Molecule("Cholesterol", "C27H46O", "386.65 g/mol", "Cyclic", "Steroid", "1815", "Membrane Component", "Covalent"),
        Molecule("Vitamin C", "C6H8O6", "176.12 g/mol", "Cyclic", "Organic", "1928", "Antioxidant, Nutrition", "Covalent"),
        Molecule("Adrenaline", "C9H13NO3", "183.20 g/mol", "Planar", "Organic", "1901", "Hormone, Medicine", "Covalent"),
        Molecule("Serotonin", "C10H12N2O", "176.22 g/mol", "Planar", "Organic", "1948", "Neurotransmitter", "Covalent"),
        Molecule("Dopamine", "C8H11NO2", "153.18 g/mol", "Planar", "Organic", "1910", "Neurotransmitter", "Covalent"),
        Molecule("Nicotine", "C10H14N2", "162.23 g/mol", "Planar", "Organic", "1828", "Stimulant", "Covalent"),
        Molecule("Morphine", "C17H19NO3", "285.34 g/mol", "Cyclic", "Organic", "1804", "Pain Relief", "Covalent"),
        Molecule("Codeine", "C18H21NO3", "299.36 g/mol", "Cyclic", "Organic", "1832", "Pain Relief, Cough Suppressant", "Covalent"),
        Molecule("Penicillin", "C16H18N2O4S", "334.39 g/mol", "Cyclic", "Antibiotic", "1928", "Antibacterial", "Covalent"),
        Molecule("Ibuprofen", "C13H18O2", "206.28 g/mol", "Planar", "Organic", "1961", "Anti-inflammatory", "Covalent"),
        Molecule("Paracetamol", "C8H9NO2", "151.16 g/mol", "Planar", "Organic", "1877", "Pain Relief", "Covalent"),
        Molecule("Salicylic Acid", "C7H6O3", "138.12 g/mol", "Planar", "Organic", "1838", "Anti-inflammatory", "Covalent"),
        Molecule("Citric Acid", "C6H8O7", "192.12 g/mol", "Tetrahedral", "Organic", "1784", "Food Additive, Preservative", "Covalent"),
        Molecule("Ascorbic Acid", "C6H8O6", "176.12 g/mol", "Cyclic", "Organic", "1928", "Vitamin, Antioxidant", "Covalent"),
        Molecule("Ethylamine", "C2H7N", "45.08 g/mol", "Tetrahedral", "Organic", "1850", "Chemical Synthesis", "Covalent"),
        Molecule("Acetamide", "C2H5NO", "59.07 g/mol", "Planar", "Organic", "1832", "Plasticizer, Solvent", "Covalent"),
        Molecule("Boric Acid", "B(OH)3", "61.83 g/mol", "Trigonal Planar", "Inorganic", "Ancient", "Antiseptic, Insecticide", "Covalent"),
        Molecule("Sulfur Dioxide", "SO2", "64.06 g/mol", "Bent", "Inorganic", "1777", "Preservative, Bleach", "Covalent"),
        Molecule("Nitrogen Dioxide", "NO2", "46.01 g/mol", "Bent", "Inorganic", "1816", "Oxidizing Agent", "Covalent"),
        Molecule("Ozone", "O3", "48.00 g/mol", "Bent", "Inorganic", "1840", "Disinfectant, Atmospheric Chemistry", "Covalent"),
        Molecule("Hydrogen Sulfide", "H2S", "34.08 g/mol", "Bent", "Inorganic", "1777", "Industrial Gas", "Covalent"),
        Molecule("Silicon Dioxide", "SiO2", "60.08 g/mol", "Tetrahedral", "Inorganic", "Ancient", "Glass, Electronics", "Covalent Network"),
        Molecule("Phosphoric Acid", "H3PO4", "97.99 g/mol", "Tetrahedral", "Inorganic", "1694", "Fertilizer, Food Additive", "Covalent"),
        Molecule("Ethylene Glycol", "C2H6O2", "62.07 g/mol", "Tetrahedral", "Organic", "1856", "Antifreeze, Solvent", "Covalent"),
        Molecule("Propylene", "C3H6", "42.08 g/mol", "Planar", "Hydrocarbon", "1850", "Plastics, Fuel", "Double Bond"),
        Molecule("Acetaldehyde", "C2H4O", "44.05 g/mol", "Trigonal Planar", "Organic", "1835", "Chemical Synthesis", "Covalent"),
        Molecule("Furan", "C4H4O", "68.07 g/mol", "Ring", "Aromatic", "1870", "Solvent, Chemical Synthesis", "Covalent"),
        Molecule("Pyrrole", "C4H5N", "67.09 g/mol", "Ring", "Aromatic", "1857", "Chemical Synthesis", "Covalent"),
        Molecule("Thiophene", "C4H4S", "84.14 g/mol", "Ring", "Aromatic", "1882", "Chemical Synthesis", "Covalent"),
        Molecule("Benzyl Alcohol", "C7H8O", "108.14 g/mol", "Tetrahedral", "Organic", "1834", "Solvent, Preservative", "Covalent"),
        Molecule("Menthol", "C10H20O", "156.27 g/mol", "Cyclic", "Organic", "1771", "Flavoring, Medicine", "Covalent"),
        Molecule("Camphor", "C10H16O", "152.23 g/mol", "Cyclic", "Organic", "Ancient", "Medicinal, Moth Repellent", "Covalent"),
        Molecule("Vanillin", "C8H8O3", "152.15 g/mol", "Planar", "Organic", "1858", "Flavoring", "Covalent"),
        Molecule("Eugenol", "C10H12O2", "164.20 g/mol", "Planar", "Organic", "1842", "Flavoring, Antiseptic", "Covalent"),
        Molecule("Cinnamaldehyde", "C9H8O", "132.16 g/mol", "Planar", "Organic", "1834", "Flavoring, Antimicrobial", "Covalent"),
        Molecule("Thymol", "C10H14O", "150.22 g/mol", "Ring", "Organic", "1719", "Antiseptic, Flavoring", "Covalent"),
        Molecule("Geraniol", "C10H18O", "154.25 g/mol", "Linear", "Terpene", "1871", "Fragrance, Insect Repellent", "Covalent"),
        Molecule("Linalool", "C10H18O", "154.25 g/mol", "Linear", "Terpene", "1875", "Fragrance, Sedative", "Covalent"),
        Molecule("Citral", "C10H16O", "152.23 g/mol", "Linear", "Terpene", "1888", "Flavoring, Fragrance", "Covalent"),
        Molecule("Biotin", "C10H16N2O3S", "244.31 g/mol", "Cyclic", "Organic", "1933", "Vitamin, Supplement", "Covalent"),
        Molecule("Folic Acid", "C19H19N7O6", "441.40 g/mol", "Planar", "Organic", "1941", "Vitamin, Cell Growth", "Covalent"),
        Molecule("Thiamine", "C12H17N4OS", "265.35 g/mol", "Planar", "Organic", "1911", "Vitamin, Metabolism", "Covalent"),
        Molecule("Riboflavin", "C17H20N4O6", "376.36 g/mol", "Planar", "Organic", "1879", "Vitamin, Metabolism", "Covalent"),
        Molecule("Pyridoxine", "C8H11NO3", "169.18 g/mol", "Planar", "Organic", "1934", "Vitamin, Metabolism", "Covalent"),
        Molecule("Cyanocobalamin", "C63H88CoN14O14P", "1355.38 g/mol", "Complex", "Organic", "1948", "Vitamin, Blood Health", "Covalent"),
        Molecule("Retinol", "C20H30O", "286.45 g/mol", "Linear", "Organic", "1931", "Vitamin, Vision", "Covalent"),
        Molecule("Cholecalciferol", "C27H44O", "384.64 g/mol", "Cyclic", "Steroid", "1927", "Vitamin, Bone Health", "Covalent"),
        Molecule("Tocopherol", "C29H50O2", "430.71 g/mol", "Cyclic", "Organic", "1922", "Vitamin, Antioxidant", "Covalent"),
        Molecule("Naphthoquinone", "C10H6O2", "158.15 g/mol", "Ring", "Organic", "1838", "Chemical Synthesis", "Covalent"),
        Molecule("Anthraquinone", "C14H8O2", "208.21 g/mol", "Ring", "Aromatic", "1834", "Dyes, Laxatives", "Covalent"),
        Molecule("Coumarin", "C9H6O2", "146.14 g/mol", "Ring", "Aromatic", "1820", "Fragrance, Anticoagulant", "Covalent"),
        Molecule("Indole", "C8H7N", "117.15 g/mol", "Ring", "Aromatic", "1866", "Chemical Synthesis, Fragrance", "Covalent"),
        Molecule("Quinine", "C20H24N2O2", "324.42 g/mol", "Cyclic", "Organic", "1820", "Antimalarial", "Covalent"),
        Molecule("Atropine", "C17H23NO3", "289.37 g/mol", "Cyclic", "Organic", "1819", "Medicine, Antispasmodic", "Covalent"),
        Molecule("Strychnine", "C21H22N2O2", "334.41 g/mol", "Cyclic", "Organic", "1818", "Pesticide, Stimulant", "Covalent"),
        Molecule("Cocaine", "C17H21NO4", "303.35 g/mol", "Cyclic", "Organic", "1860", "Anesthetic, Stimulant", "Covalent")
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (intent.hasExtra("Molecule")) {
            showDetail(intent.getSerializableExtra("Molecule") as Molecule)
        } else {
            showSearch()
        }
    }

    private fun showSearch() {
        setContentView(R.layout.activity_molecules)

        val searchView = findViewById<SearchView>(R.id.searchView)
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerViewMolecules)
        val backBtn = findViewById<ImageView>(R.id.backButton)

        val adapter = MoleculeAdapter(molecules) { selected ->
            val intent = Intent(this, MoleculesActivity::class.java)
            intent.putExtra("Molecule", selected)
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

    private fun showDetail(mol: Molecule) {
        setContentView(R.layout.activity_molecule_detail)

        findViewById<TextView>(R.id.titleText).text = mol.name
        findViewById<TextView>(R.id.formulaText).text = "Formula: ${mol.formula}"
        findViewById<TextView>(R.id.weightText).text = "Molecular Weight: ${mol.molecularWeight}"
        findViewById<TextView>(R.id.structureText).text = "Structure: ${mol.structure}"
        findViewById<TextView>(R.id.typeText).text = "Type: ${mol.type}"
        findViewById<TextView>(R.id.usageText).text = "Usage: ${mol.usage}"
        findViewById<TextView>(R.id.bondsText).text = "Bonds: ${mol.bonds}"
        findViewById<TextView>(R.id.discoveryText).text = "Discovery Year: ${mol.discoveryYear}"

        findViewById<ImageView>(R.id.backButton).setOnClickListener { finish() }
    }

    class MoleculeAdapter(
        private val fullList: List<Molecule>,
        private val onItemClick: (Molecule) -> Unit
    ) : RecyclerView.Adapter<MoleculeAdapter.MoleculeViewHolder>() {

        private var filteredList = fullList.toMutableList()

        inner class MoleculeViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val nameText: TextView = view.findViewById(R.id.moleculeName)
            val card: CardView = view.findViewById(R.id.cardRoot)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MoleculeViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_molecule, parent, false)
            return MoleculeViewHolder(view)
        }

        override fun onBindViewHolder(holder: MoleculeViewHolder, position: Int) {
            val mol = filteredList[position]
            holder.nameText.text = mol.name
            holder.card.setOnClickListener { onItemClick(mol) }
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

package com.quantum.molecuq

import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.io.Serializable

class ProteinsActivity : AppCompatActivity() {

    data class Protein(
        val name: String,
        val sequence: String,
        val function: String,
        val sourceOrganism: String,
        val molecularWeight: String,
        val discoveryDate: String,
        val structureType: String,
        val length: Int,
        val application: String
    ) : Serializable

    private val proteins = listOf(
        Protein("Insulin", "FVNQHLCGSHLVE...", "Regulates blood sugar", "Homo sapiens", "5.8 kDa", "15 Aug 1922", "Alpha-helix", 51, "Diabetes treatment"),
        Protein("Hemoglobin", "VLSPADKTNVKAA...", "Oxygen transport", "Homo sapiens", "64.5 kDa", "10 Mar 1904", "Globular", 574, "Blood transfusions"),
        Protein("Collagen", "GXYGXYGXY...", "Structural support", "Bos taurus", "300 kDa", "25 Jun 1935", "Triple helix", 1050, "Tissue engineering"),
        Protein("Myoglobin", "GLSDGEWQLVLNV...", "Oxygen storage", "Equus caballus", "17.0 kDa", "8 Feb 1957", "Globular", 153, "Muscle research"),
        Protein("Actin", "DDEETTALVCDNG...", "Cell motility", "Homo sapiens", "41.7 kDa", "12 Apr 1943", "Filamentous", 375, "Cytoskeletal studies"),
        Protein("Tubulin", "MREIVHIQAGQCG...", "Cytoskeleton", "Homo sapiens", "50.0 kDa", "20 Sep 1968", "Globular", 450, "Cancer therapy"),
        Protein("Lysozyme", "KVFERCELARTLK...", "Antibacterial", "Gallus gallus", "14.3 kDa", "7 Nov 1922", "Globular", 129, "Antimicrobial agents"),
        Protein("Albumin", "MKWVTFISLLFSS...", "Blood transport", "Homo sapiens", "66.5 kDa", "3 Jan 1939", "Globular", 585, "Drug delivery"),
        Protein("Trypsin", "IVGGYTCAENSVP...", "Protein digestion", "Bos taurus", "23.3 kDa", "15 May 1876", "Globular", 223, "Proteomics"),
        Protein("Fibrinogen", "GPRVVERHQSACK...", "Blood clotting", "Homo sapiens", "340 kDa", "22 Oct 1920", "Fibrous", 2966, "Wound healing"),
        Protein("Keratin", "MTTCSRQFTSSSS...", "Structural", "Homo sapiens", "60 kDa", "18 Jul 1935", "Alpha-helix", 500, "Cosmetics"),
        Protein("Elastin", "VPGVGVPGVGVPG...", "Elasticity", "Bos taurus", "72 kDa", "9 Mar 1940", "Fibrous", 800, "Tissue repair"),
        Protein("Amylase", "QYSPTTRLRLGDS...", "Starch digestion", "Homo sapiens", "57.6 kDa", "14 Feb 1833", "Globular", 496, "Food industry"),
        Protein("Histone H3", "ARTKQTARKSTGG...", "DNA packaging", "Homo sapiens", "15.3 kDa", "27 Aug 1964", "Globular", 135, "Epigenetics research"),
        Protein("Ubiquitin", "MQIFVKTLTGKTI...", "Protein degradation", "Homo sapiens", "8.6 kDa", "5 Dec 1975", "Globular", 76, "Therapeutics"),
        Protein("Cytochrome C", "GDVEKGKKIFIMK...", "Electron transport", "Equus caballus", "12.4 kDa", "11 Jun 1938", "Globular", 104, "Mitochondrial studies"),
        Protein("GFP", "MSKGEELFTGVVP...", "Fluorescence", "Aequorea victoria", "26.9 kDa", "22 Sep 1962", "Beta-barrel", 238, "Bioimaging"),
        Protein("Chymotrypsin", "CGVPAIQPVLSGL...", "Protein digestion", "Bos taurus", "25.0 kDa", "30 Apr 1932", "Globular", 241, "Proteomics"),
        Protein("Pepsin", "IGDEPLENYLDTE...", "Protein digestion", "Sus scrofa", "34.5 kDa", "8 Oct 1836", "Globular", 326, "Pharmaceuticals"),
        Protein("Immunoglobulin G", "EVQLVESGGGLVQ...", "Immune response", "Homo sapiens", "150 kDa", "12 Dec 1937", "Globular", 1320, "Immunotherapy"),
        Protein("Lactase", "LTLTKVTFNRTGS...", "Lactose digestion", "Homo sapiens", "192 kDa", "17 Mar 1950", "Globular", 1028, "Dairy industry"),
        Protein("Catalase", "ADVLTTNAAGAPV...", "Antioxidant", "Homo sapiens", "240 kDa", "25 Nov 1937", "Globular", 526, "Medical diagnostics"),
        Protein("Ferritin", "SSQIRQNYSTDV...", "Iron storage", "Homo sapiens", "450 kDa", "3 Jul 1938", "Globular", 183, "Nanotechnology"),
        Protein("Glucagon", "HSQGTFTSDYSKY...", "Hormone", "Homo sapiens", "3.7 kDa", "20 Jun 1921", "Alpha-helix", 29, "Diabetes management"),
        Protein("Rhodopsin", "MNGTEGPNFYIPD...", "Vision", "Bos taurus", "38.0 kDa", "15 Feb 1878", "Transmembrane", 348, "Vision research"),
        Protein("Lipase", "SPVDLTKFHLAPG...", "Fat digestion", "Homo sapiens", "42.0 kDa", "10 Aug 1848", "Globular", 377, "Enzyme therapy"),
        Protein("Casein", "RELEELNVPGEIV...", "Milk protein", "Bos taurus", "24.0 kDa", "5 Apr 1890", "Disordered", 207, "Nutrition"),
        Protein("Avidin", "ARKCSLTGKWTND...", "Biotin binding", "Gallus gallus", "68.0 kDa", "14 Nov 1940", "Globular", 128, "Biotechnology"),
        Protein("Thrombin", "TFGSGEADCW...", "Blood clotting", "Homo sapiens", "32.0 kDa", "19 Jun 1933", "Globular", 295, "Anticoagulants"),
        Protein("Luciferase", "MEDAKNIKKGPAP...", "Bioluminescence", "Photinus pyralis", "61.0 kDa", "27 Jul 1930", "Globular", 550, "Biosensing"),
        Protein("Superoxide Dismutase", "ATKAVAVLKGSD...", "Antioxidant", "Homo sapiens", "32.0 kDa", "15 Jun 1969", "Globular", 153, "Neuroprotection"),
        Protein("Pepsinogen", "VDEQPLENYLDME...", "Protein digestion", "Sus scrofa", "40.0 kDa", "10 Jul 1934", "Globular", 370, "Pharmaceuticals"),
        Protein("Troponin C", "MDDIYKAAVEQ...", "Muscle contraction", "Homo sapiens", "18.4 kDa", "20 Aug 1973", "Globular", 159, "Cardiac diagnostics"),
        Protein("Laminin", "AEGRGTQISNP...", "Cell adhesion", "Mus musculus", "900 kDa", "12 May 1979", "Fibrous", 3000, "Tissue engineering"),
        Protein("Annexin A2", "STVHEILTKLS...", "Membrane repair", "Homo sapiens", "38.6 kDa", "25 Sep 1984", "Globular", 339, "Cancer research"),
        Protein("Glutathione S-transferase", "MSPILGYWKIK...", "Detoxification", "Escherichia coli", "25.0 kDa", "30 Nov 1977", "Globular", 211, "Biotechnology"),
        Protein("Matrix Metalloproteinase-2", "MEALMARGALT...", "Matrix remodeling", "Homo sapiens", "72.0 kDa", "18 Oct 1990", "Globular", 660, "Cancer therapy"),
        Protein("Calmodulin", "ADQLTEEQIAE...", "Calcium signaling", "Homo sapiens", "16.8 kDa", "22 Apr 1978", "Globular", 148, "Drug development"),
        Protein("Vimentin", "MSTRSVSSS...", "Cytoskeleton", "Homo sapiens", "53.6 kDa", "14 Feb 1979", "Fibrous", 466, "Cell biology"),
        Protein("Thioredoxin", "MVKQIESKTAF...", "Redox regulation", "Homo sapiens", "12.0 kDa", "10 Jun 1967", "Globular", 105, "Therapeutics"),
        Protein("Hsp70", "MAKAAAIGIDL...", "Protein folding", "Homo sapiens", "70.0 kDa", "8 Sep 1974", "Globular", 641, "Stress response"),
        Protein("Beta-actin", "DDDIAALVVDN...", "Cytoskeleton", "Homo sapiens", "41.7 kDa", "12 Apr 1943", "Filamentous", 375, "Cell motility studies"),
        Protein("Transferrin", "VPDKTVRWCAV...", "Iron transport", "Homo sapiens", "77.0 kDa", "15 Jan 1947", "Globular", 679, "Anemia treatment"),
        Protein("Fibronectin", "QAQQMVQPQSP...", "Cell adhesion", "Homo sapiens", "250 kDa", "20 Jul 1973", "Fibrous", 2386, "Wound healing"),
        Protein("G-actin", "MEEEIAALVID...", "Cell motility", "Homo sapiens", "41.8 kDa", "10 Mar 1943", "Globular", 375, "Cytoskeletal research"),
        Protein("Hemocyanin", "HRLFDKFNQIV...", "Oxygen transport", "Limulus polyphemus", "450 kDa", "5 Aug 1879", "Globular", 628, "Biomedical research"),
        Protein("Clathrin", "MAQILPIRFQE...", "Vesicle formation", "Homo sapiens", "191 kDa", "12 Nov 1976", "Globular", 1675, "Endocytosis studies"),
        Protein("Myosin", "MQQPFHFTINA...", "Muscle contraction", "Homo sapiens", "200 kDa", "18 Jun 1939", "Fibrous", 1939, "Motor protein research"),
        Protein("Spectrin", "MQRKQAVDLLL...", "Membrane structure", "Homo sapiens", "280 kDa", "25 Feb 1968", "Fibrous", 2472, "Cytoskeletal studies")
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Show detail if intent has a protein
        if (intent.hasExtra("Protein")) {
            showDetail(intent.getSerializableExtra("Protein") as Protein)
        } else {
            showSearch()
        }
    }

    private fun showSearch() {
        setContentView(LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(16, 16, 16, 16)

            val topBar = LinearLayout(context).apply {
                orientation = LinearLayout.HORIZONTAL

                val back = ImageView(context).apply {
                    setImageResource(R.drawable.ic_back)
                    setColorFilter(0xFF000000.toInt())
                    setOnClickListener { finish() }
                }

                val title = TextView(context).apply {
                    text = "Proteins"
                    textSize = 20f
                    setTextColor(0xFF000000.toInt())
                    setPadding(16, 0, 0, 0)
                }

                addView(back, LinearLayout.LayoutParams(80, 80))
                addView(title)
            }

            val searchView = SearchView(context).apply {
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                ).apply { setMargins(0, 16, 0, 16) }
                queryHint = "Search Proteins"
            }

            val recyclerView = RecyclerView(context).apply {
                id = R.id.recyclerViewProteins
                layoutManager = LinearLayoutManager(context)
            }

            addView(topBar)
            addView(searchView)
            addView(recyclerView)

            val adapter = ProteinAdapter(proteins) { selected ->
                val intent = Intent(context, ProteinsActivity::class.java)
                intent.putExtra("Protein", selected)
                context.startActivity(intent)
            }

            recyclerView.adapter = adapter

            searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?) = false
                override fun onQueryTextChange(query: String?): Boolean {
                    adapter.filter(query ?: "")
                    return true
                }
            })
        })
    }

    private fun showDetail(protein: Protein) {
        setContentView(ScrollView(this).apply {
            isFillViewport = true
            setPadding(16, 16, 16, 16)

            addView(LinearLayout(context).apply {
                orientation = LinearLayout.VERTICAL

                val topBar = LinearLayout(context).apply {
                    orientation = LinearLayout.HORIZONTAL
                    val back = ImageView(context).apply {
                        setImageResource(R.drawable.ic_back)
                        setColorFilter(0xFF000000.toInt())
                        setOnClickListener { finish() }
                    }
                    val title = TextView(context).apply {
                        text = protein.name
                        textSize = 22f
                        setTextColor(0xFF000000.toInt())
                        setPadding(12, 0, 0, 0)
                    }
                    addView(back, LinearLayout.LayoutParams(80, 80))
                    addView(title)
                }

                fun detail(label: String, value: String) = TextView(context).apply {
                    text = "$label: $value"
                    textSize = 16f
                    setTextColor(0xFF333333.toInt())
                    setPadding(0, 12, 0, 12)
                }

                addView(topBar)
                addView(detail("Sequence", protein.sequence))
                addView(detail("Function", protein.function))
                addView(detail("Source Organism", protein.sourceOrganism))
                addView(detail("Molecular Weight", protein.molecularWeight))
                addView(detail("Structure Type", protein.structureType))
                addView(detail("Length", protein.length.toString()))
                addView(detail("Application", protein.application))
                addView(detail("Discovery Date", protein.discoveryDate))
            })
        })
    }

    class ProteinAdapter(
        private val fullList: List<Protein>,
        private val onItemClick: (Protein) -> Unit
    ) : RecyclerView.Adapter<ProteinAdapter.ProteinViewHolder>() {

        private var filteredList = fullList.toMutableList()

        inner class ProteinViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val nameText: TextView = view.findViewById(R.id.ProteinName)
            val root: View = view.findViewById(R.id.cardRoot)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProteinViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_protein, parent, false)
            return ProteinViewHolder(view)
        }

        override fun onBindViewHolder(holder: ProteinViewHolder, position: Int) {
            val protein = filteredList[position]
            holder.nameText.text = protein.name
            holder.root.setOnClickListener { onItemClick(protein) }
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
package com.quantum.molecuq

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.opencsv.CSVReaderBuilder
import com.opencsv.CSVWriter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import java.io.File
import java.io.FileWriter
import java.io.InputStreamReader

data class SmilesResult(
    val smiles: String,
    val isValid: Boolean,
    val errorMessage: String? = null,
    val molecularWeight: Double? = null,
    val atomCount: Int? = null
)

class SmilesResultAdapter(
    var results: List<SmilesResult>,
    private val onItemClick: (SmilesResult) -> Unit
) : RecyclerView.Adapter<SmilesResultAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val mainContent: ViewGroup = itemView.findViewById(R.id.mainContent)
        val smilesTextView: TextView = itemView.findViewById(R.id.smilesTextView)
        val statusTextView: TextView = itemView.findViewById(R.id.statusTextView)
        val detailsLayout: ViewGroup = itemView.findViewById(R.id.detailsLayout)
        val errorTextView: TextView = itemView.findViewById(R.id.errorTextView)
        val molWeightTextView: TextView = itemView.findViewById(R.id.molWeightTextView)
        val atomCountTextView: TextView = itemView.findViewById(R.id.atomCountTextView)
        val moleculeWebView: android.webkit.WebView = itemView.findViewById(R.id.moleculeWebView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_smiles_result, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val result = results[position]
        holder.smilesTextView.text = result.smiles
        holder.statusTextView.text = if (result.isValid) "Valid" else "Invalid"
        holder.statusTextView.setTextColor(
            if (result.isValid) 0xFF4CAF50.toInt() else 0xFFFF0000.toInt()
        )
        holder.errorTextView.visibility = if (result.errorMessage != null && !result.isValid) View.VISIBLE else View.GONE
        holder.errorTextView.text = result.errorMessage
        holder.molWeightTextView.visibility = if (result.molecularWeight != null) View.VISIBLE else View.GONE
        holder.molWeightTextView.text = result.molecularWeight?.let { "Mol. Weight: %.2f".format(it) }
        holder.atomCountTextView.visibility = if (result.atomCount != null) View.VISIBLE else View.GONE
        holder.atomCountTextView.text = result.atomCount?.let { "Atom Count: $it" }

        if (result.isValid) {
            holder.moleculeWebView.visibility = View.VISIBLE
            holder.moleculeWebView.settings.javaScriptEnabled = true
            holder.moleculeWebView.settings.setSupportZoom(true)
            holder.moleculeWebView.settings.builtInZoomControls = true
            holder.moleculeWebView.settings.displayZoomControls = false
            val htmlContent = """
                <html>
                <head>
                    <script src="file:///android_asset/smiles-drawer.min.js"></script>
                </head>
                <body>
                    <canvas id="molecule-canvas" data-smiles="${result.smiles}"></canvas>
                    <script>
                        let drawer = new SmilesDrawer.Drawer({ width: 200, height: 200 });
                        SmilesDrawer.parse(document.getElementById('molecule-canvas').getAttribute('data-smiles'), function(tree) {
                            drawer.draw(tree, 'molecule-canvas', 'light', false);
                        });
                    </script>
                </body>
                </html>
            """.trimIndent()
            holder.moleculeWebView.loadDataWithBaseURL("file:///android_asset/", htmlContent, "text/html", "UTF-8", null)
        } else {
            holder.moleculeWebView.visibility = View.GONE
        }

        holder.mainContent.setOnClickListener {
            holder.detailsLayout.visibility = if (holder.detailsLayout.visibility == View.VISIBLE) View.GONE else View.VISIBLE
            onItemClick(result)
        }
    }

    override fun getItemCount(): Int = results.size

    fun updateResults(newResults: List<SmilesResult>) {
        results = newResults
        notifyDataSetChanged()
    }
}

class AnalysisActivity : AppCompatActivity() {

    private lateinit var collapsingToolbar: CollapsingToolbarLayout
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var backButton: ImageButton
    private lateinit var uploadButton: Button
    private lateinit var searchEditText: EditText
    private lateinit var validOnlyCheckBox: CheckBox
    private lateinit var sortByWeightCheckBox: CheckBox
    private lateinit var minWeightEditText: EditText
    private lateinit var maxWeightEditText: EditText
    private lateinit var exportButton: Button
    private lateinit var shareButton: Button
    private lateinit var progressBar: ProgressBar
    private lateinit var summaryTextView: TextView
    private lateinit var resultRecyclerView: RecyclerView
    private lateinit var adapter: SmilesResultAdapter
    private var allResults = mutableListOf<SmilesResult>()
    private var fileUri: Uri? = null
    private val httpClient = OkHttpClient()

    // Improved regex for SMILES validation
    private val smilesRegex = Regex("^[A-Za-z0-9@+\\-=#$%*()\\[\\]/.:]+(?:[0-9]|%[0-9]{2})*\$")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_analysis)

        collapsingToolbar = findViewById(R.id.collapsingToolbar)
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout)
        backButton = findViewById(R.id.backButton)
        uploadButton = findViewById(R.id.uploadButton)
        searchEditText = findViewById(R.id.searchEditText)
        validOnlyCheckBox = findViewById(R.id.validOnlyCheckBox)
        sortByWeightCheckBox = findViewById(R.id.sortByWeightCheckBox)
        minWeightEditText = findViewById(R.id.minWeightEditText)
        maxWeightEditText = findViewById(R.id.maxWeightEditText)
        exportButton = findViewById(R.id.exportButton)
        shareButton = findViewById(R.id.shareButton)
        progressBar = findViewById(R.id.progressBar)
        summaryTextView = findViewById(R.id.summaryTextView)
        resultRecyclerView = findViewById(R.id.resultRecyclerView)

        adapter = SmilesResultAdapter(emptyList()) { /* Handle item click */ }
        resultRecyclerView.layoutManager = LinearLayoutManager(this)
        resultRecyclerView.adapter = adapter

        val pickFile = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            fileUri = uri
            uri?.let { analyzeFile(it) } ?: run { summaryTextView.text = "No file selected" }
        }

        uploadButton.setOnClickListener {
            pickFile.launch("text/*")
        }

        backButton.setOnClickListener {
            resetToUploadState()
        }

        swipeRefreshLayout.setOnRefreshListener {
            fileUri?.let { analyzeFile(it) } ?: run { swipeRefreshLayout.isRefreshing = false }
        }

        searchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                filterResults(s.toString(), validOnlyCheckBox.isChecked, minWeightEditText.text.toString(), maxWeightEditText.text.toString())
            }
        })

        validOnlyCheckBox.setOnCheckedChangeListener { _, isChecked ->
            filterResults(searchEditText.text.toString(), isChecked, minWeightEditText.text.toString(), maxWeightEditText.text.toString())
        }

        sortByWeightCheckBox.setOnCheckedChangeListener { _, isChecked ->
            sortResults(isChecked)
        }

        minWeightEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                filterResults(searchEditText.text.toString(), validOnlyCheckBox.isChecked, s.toString(), maxWeightEditText.text.toString())
            }
        })

        maxWeightEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                filterResults(searchEditText.text.toString(), validOnlyCheckBox.isChecked, minWeightEditText.text.toString(), s.toString())
            }
        })

        exportButton.setOnClickListener {
            exportResults()
        }

        shareButton.setOnClickListener {
            shareResults()
        }

        // Request permissions for older Android versions
        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.TIRAMISU) {
            requestPermissions(arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.WRITE_EXTERNAL_STORAGE), 100)
        }
    }

    private fun resetToUploadState() {
        allResults.clear()
        adapter.updateResults(emptyList())
        fileUri = null
        summaryTextView.text = "Upload a file to start analysis"
        backButton.visibility = View.GONE
        searchEditText.visibility = View.GONE
        searchEditText.text.clear()
        validOnlyCheckBox.visibility = View.GONE
        sortByWeightCheckBox.visibility = View.GONE
        minWeightEditText.visibility = View.GONE
        maxWeightEditText.visibility = View.GONE
        exportButton.visibility = View.GONE
        shareButton.visibility = View.GONE
        resultRecyclerView.visibility = View.GONE
        uploadButton.visibility = View.VISIBLE
        swipeRefreshLayout.isRefreshing = false
    }

    private fun analyzeFile(uri: Uri) {
        CoroutineScope(Dispatchers.Main).launch {
            progressBar.visibility = View.VISIBLE
            uploadButton.visibility = View.GONE
            val results = withContext(Dispatchers.IO) {
                val tempResults = mutableListOf<SmilesResult>()
                try {
                    contentResolver.openInputStream(uri)?.use { inputStream ->
                        CSVReaderBuilder(InputStreamReader(inputStream)).build().use { csvReader ->
                            csvReader.forEach { row ->
                                val smiles = row.getOrNull(0)?.trim() ?: ""
                                if (smiles.isNotEmpty()) {
                                    val isValid = isValidSmiles(smiles)
                                    val molecularWeight = if (isValid) calculateMolecularWeight(smiles) else null
                                    val atomCount = if (isValid) fetchAtomCount(smiles) else null
                                    val result = SmilesResult(
                                        smiles = smiles,
                                        isValid = isValid,
                                        errorMessage = if (isValid) null else "Invalid SMILES syntax",
                                        molecularWeight = molecularWeight,
                                        atomCount = atomCount
                                    )
                                    tempResults.add(result)
                                }
                            }
                        }
                    }
                } catch (e: Exception) {
                    withContext(Dispatchers.Main) {
                        summaryTextView.text = "Error: ${e.message}"
                    }
                }
                tempResults
            }
            allResults.clear()
            allResults.addAll(results)
            adapter.updateResults(results)
            updateSummary(results)
            progressBar.visibility = View.GONE
            backButton.visibility = View.VISIBLE
            searchEditText.visibility = View.VISIBLE
            validOnlyCheckBox.visibility = View.VISIBLE
            sortByWeightCheckBox.visibility = View.VISIBLE
            minWeightEditText.visibility = View.VISIBLE
            maxWeightEditText.visibility = View.VISIBLE
            exportButton.visibility = View.VISIBLE
            shareButton.visibility = View.VISIBLE
            resultRecyclerView.visibility = View.VISIBLE
            swipeRefreshLayout.isRefreshing = false
        }
    }

    private fun filterResults(query: String, validOnly: Boolean, minWeight: String, maxWeight: String) {
        val min = minWeight.toDoubleOrNull() ?: 0.0
        val max = maxWeight.toDoubleOrNull() ?: Double.MAX_VALUE
        val filtered = allResults.filter { result ->
            result.smiles.contains(query, ignoreCase = true) &&
                    (!validOnly || result.isValid) &&
                    (result.molecularWeight == null || (result.molecularWeight in min..max))
        }
        adapter.updateResults(filtered)
        updateSummary(filtered)
    }

    private fun sortResults(byMolecularWeight: Boolean) {
        val sorted = if (byMolecularWeight) {
            allResults.sortedBy { it.molecularWeight ?: Double.MAX_VALUE }
        } else {
            allResults.sortedBy { it.smiles }
        }
        adapter.updateResults(sorted)
    }

    private fun updateSummary(results: List<SmilesResult>) {
        summaryTextView.text = """
            Analysis Summary:
            Total SMILES: ${results.size}
            Valid: ${results.count { it.isValid }}
            Invalid: ${results.count { !it.isValid }}
            Avg. Mol. Weight: ${results.filter { it.molecularWeight != null }.mapNotNull { it.molecularWeight }.average().let { if (it.isNaN()) "N/A" else "%.2f".format(it) }}
        """.trimIndent()
    }

    private fun exportResults() {
        try {
            val file = File(getExternalFilesDir(null), "smiles_analysis.csv")
            CSVWriter(FileWriter(file)).use { writer ->
                writer.writeNext(arrayOf("SMILES", "Valid", "Error", "MolecularWeight", "AtomCount"))
                adapter.results.forEach { result ->
                    writer.writeNext(
                        arrayOf(
                            result.smiles,
                            result.isValid.toString(),
                            result.errorMessage ?: "",
                            result.molecularWeight?.toString() ?: "",
                            result.atomCount?.toString() ?: ""
                        )
                    )
                }
            }
            summaryTextView.text = "Exported to ${file.absolutePath}"
        } catch (e: Exception) {
            summaryTextView.text = "Export failed: ${e.message}"
        }
    }

    private fun shareResults() {
        try {
            val file = File(getExternalFilesDir(null), "smiles_analysis.csv")
            CSVWriter(FileWriter(file)).use { writer ->
                writer.writeNext(arrayOf("SMILES", "Valid", "Error", "MolecularWeight", "AtomCount"))
                adapter.results.forEach { result ->
                    writer.writeNext(
                        arrayOf(
                            result.smiles,
                            result.isValid.toString(),
                            result.errorMessage ?: "",
                            result.molecularWeight?.toString() ?: "",
                            result.atomCount?.toString() ?: ""
                        )
                    )
                }
            }
            val shareIntent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_STREAM, FileProvider.getUriForFile(this@AnalysisActivity, "${packageName}.provider", file))
                type = "text/csv"
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }
            startActivity(Intent.createChooser(shareIntent, "Share SMILES Analysis"))
        } catch (e: Exception) {
            summaryTextView.text = "Share failed: ${e.message}"
        }
    }

    private fun isValidSmiles(smiles: String): Boolean {
        // Placeholder for RDKit validation
        return smilesRegex.matches(smiles) && smiles.isNotBlank()
    }

    private fun calculateMolecularWeight(smiles: String): Double? {
        // Placeholder for RDKit
        return null
    }

    private suspend fun fetchAtomCount(smiles: String): Int? {
        // Placeholder for RDKit or PubChem API
        return try {
            val response = withContext(Dispatchers.IO) {
                val request = Request.Builder()
                    .url("https://pubchem.ncbi.nlm.nih.gov/rest/pug/compound/smiles/$smiles/property/HeavyAtomCount/JSON")
                    .build()
                httpClient.newCall(request).execute().body?.string()
            }
            response?.let {
                JSONObject(it).getJSONObject("PropertyTable").getJSONArray("Properties").getJSONObject(0).getInt("HeavyAtomCount")
            }
        } catch (e: Exception) {
            null
        }
    }
}
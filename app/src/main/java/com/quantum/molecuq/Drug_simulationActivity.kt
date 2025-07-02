package com.quantum.molecuq

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.lang.Math.log
import kotlin.math.exp
import kotlin.math.log

/**
 * Drug_simulationActivity: This activity simulates basic pharmacokinetic (PK)
 * properties of selected drugs based on user-provided dosage and patient weight.
 * It provides a simplified one-compartment model to estimate drug concentration
 * and offers interpretations based on therapeutic and toxic ranges.
 */
class Drug_simulationActivity : AppCompatActivity() {

    // --- UI Element Declarations ---
    // These 'lateinit' variables will be initialized later in the onCreate method.
    private lateinit var spinnerDrugs: Spinner
    private lateinit var btnBack: ImageButton// Dropdown for drug selection
    private lateinit var tvDrugInfo: TextView              // Displays detailed info about selected drug
    private lateinit var etDosage: EditText                // Input field for drug dosage (mg)
    private lateinit var etPatientWeight: EditText         // Input field for patient weight (kg)
    private lateinit var btnSimulate: Button               // Button to trigger the simulation
    private lateinit var tvSimulationResult: TextView      // Displays the primary outcome of the simulation
    private lateinit var tvAdditionalSimulationOutput: TextView // Displays detailed PK parameters

    // --- Drug Data Model ---
    // A map holding pre-defined drug data. Each drug is an instance of the 'Drug' data class.
    // This simulates a database of drug information.
    private val drugData = mapOf(
        "Aspirin" to Drug(
            name = "Aspirin",
            description = "A salicylate and NSAID. Used for pain, fever, inflammation, and anti-platelet effects.",
            typicalDosage = "325mg - 650mg every 4 hours for pain/fever; 81mg daily for anti-platelet.",
            sideEffects = "Stomach upset, heartburn, drowsiness. Severe: Reye's syndrome in children, bleeding, tinnitus.",
            absorptionRateConstant = 0.8, // ka (h^-1) - how fast it gets into the blood
            eliminationRateConstant = 0.25, // kel (h^-1) - how fast it's removed from the blood
            volumeOfDistributionLKg = 0.15, // Vd (L/kg) - how widely it distributes in the body
            therapeuticConcentrationMin = 10.0, // ug/mL - minimum effective concentration
            therapeuticConcentrationMax = 100.0, // ug/mL - maximum safe therapeutic concentration
            toxicConcentration = 300.0, // ug/mL - concentration where toxicity starts
            notes = "Aspirin's pharmacokinetics can be complex with dose-dependent elimination."
        ),
        "Paracetamol" to Drug(
            name = "Paracetamol (Acetaminophen)",
            description = "Common painkiller and fever reducer. Acts centrally.",
            typicalDosage = "500mg - 1000mg every 4-6 hours (max 4g/day).",
            sideEffects = "Rare at recommended doses. Overdose can cause severe liver damage (hepatotoxicity).",
            absorptionRateConstant = 1.2, // ka (h^-1)
            eliminationRateConstant = 0.15, // kel (h^-1)
            volumeOfDistributionLKg = 0.8, // Vd (L/kg)
            therapeuticConcentrationMin = 10.0, // ug/mL
            therapeuticConcentrationMax = 20.0, // ug/mL
            toxicConcentration = 150.0, // ug/mL (acute toxicity threshold at 4 hours post-ingestion)
            notes = "Paracetamol overdose is a medical emergency due to liver toxicity risk."
        ),
        "Metformin" to Drug(
            name = "Metformin",
            description = "Oral anti-diabetic drug for type 2 diabetes. Lowers blood glucose.",
            typicalDosage = "500mg - 1000mg two or three times daily (max 2550mg/day).",
            sideEffects = "Nausea, vomiting, diarrhea, abdominal discomfort. Rare: Lactic acidosis.",
            absorptionRateConstant = 0.5, // ka (h^-1)
            eliminationRateConstant = 0.08, // kel (h^-1)
            volumeOfDistributionLKg = 1.0, // Vd (L/kg)
            therapeuticConcentrationMin = 0.5, // ug/mL (rough therapeutic range)
            therapeuticConcentrationMax = 2.0, // ug/mL
            toxicConcentration = 5.0, // ug/mL
            notes = "Primarily eliminated renally; dose adjustment needed in kidney impairment."
        )
    )

    /**
     * Called when the activity is first created. Initializes the UI, sets up listeners,
     * and prepares the drug selection spinner.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_drug_simulation) // Link to the XML layout file

        // Initialize all UI elements by finding them by their IDs from the layout file.
        initializeUI()
        // Set up the drug selection spinner with available drug names.
        setupDrugSpinner()

        // Attach click listener to the simulation button.
        setupListeners()
        btnBack.setOnClickListener {
            finish() // This closes the current activity and returns to the previous one
        }
    }

    /**
     * Initializes all UI elements by linking their IDs from the XML layout to
     * the corresponding 'lateinit' variables.
     */
    private fun initializeUI() {
        spinnerDrugs = findViewById(R.id.spinnerDrugs)
        btnBack = findViewById(R.id.btnBack)
        tvDrugInfo = findViewById(R.id.tvDrugInfo)
        etDosage = findViewById(R.id.etDosage)
        etPatientWeight = findViewById(R.id.etPatientWeight)
        btnSimulate = findViewById(R.id.btnSimulate)
        tvSimulationResult = findViewById(R.id.tvSimulationResult)
        tvAdditionalSimulationOutput = findViewById(R.id.tvAdditionalSimulationOutput)
    }

    /**
     * Populates the drug spinner with drug names from 'drugData' and sets up
     * a listener to display drug information when a new drug is selected.
     */
    private fun setupDrugSpinner() {
        val drugNames = drugData.keys.toList() // Get a list of all drug names
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, drugNames)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerDrugs.adapter = adapter // Assign the adapter to the spinner

        // Set an item selected listener for the spinner
        spinnerDrugs.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedDrugName = parent?.getItemAtPosition(position).toString()
                displayDrugInformation(selectedDrugName) // Show info for the newly selected drug

                // Clear previous simulation results when a new drug is selected
                resetSimulationResultsDisplay()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // This callback is invoked when the selection disappears from this view.
                // In most spinner cases, it's not commonly used.
            }
        }
    }


    /**
     * Resets the text and background color of the simulation result display TextViews.
     */
    private fun resetSimulationResultsDisplay() {
        tvSimulationResult.text = "Simulation results will appear here after you run a simulation."
        tvSimulationResult.setBackgroundColor(Color.parseColor("#4CAF50")) // Default green for informational
        tvAdditionalSimulationOutput.text = ""
    }

    /**
     * Displays detailed information about the currently selected drug in the 'tvDrugInfo' TextView.
     * @param drugName The name of the drug whose information is to be displayed.
     */
    private fun displayDrugInformation(drugName: String) {
        val drug = drugData[drugName] // Retrieve the Drug object from the map
        drug?.let { // 'let' block executes if 'drug' is not null
            val info = """
                Name: ${it.name}
                Description: ${it.description}
                Typical Dosage: ${it.typicalDosage}
                Possible Side Effects: ${it.sideEffects}
                Notes: ${it.notes}

                --- Pharmacokinetic Parameters (Simplified) ---
                - Absorption Rate Constant (ka): ${it.absorptionRateConstant} h⁻¹
                - Elimination Rate Constant (kel): ${it.eliminationRateConstant} h⁻¹
                - Volume of Distribution (Vd): ${it.volumeOfDistributionLKg} L/kg

                --- Concentration Ranges ---
                - Therapeutic Range: ${it.therapeuticConcentrationMin} - ${it.therapeuticConcentrationMax} µg/mL
                - Toxic Level (Acute): >${it.toxicConcentration} µg/mL
            """.trimIndent()
            tvDrugInfo.text = info
        } ?: run { // 'run' block executes if 'drug' is null
            tvDrugInfo.text = "No information available for this drug."
        }
    }

    /**
     * Sets up the click listener for the 'Run Simulation' button.
     */
    private fun setupListeners() {
        btnSimulate.setOnClickListener {
            runDrugSimulation() // Call the simulation logic when button is clicked
        }
    }

    /**
     * Validates user inputs (dosage, patient weight) and then triggers the drug simulation.
     * Displays error messages as Toast if inputs are invalid.
     */
    private fun runDrugSimulation() {
        val selectedDrugName = spinnerDrugs.selectedItem.toString()
        val dosageInput = etDosage.text.toString()
        val weightInput = etPatientWeight.text.toString()

        // --- Input Validation ---
        if (dosageInput.isBlank()) {
            Toast.makeText(this, "Please enter a dosage (e.g., 500).", Toast.LENGTH_SHORT).show()
            return // Stop execution if dosage is empty
        }
        if (weightInput.isBlank()) {
            Toast.makeText(this, "Please enter patient weight (e.g., 70).", Toast.LENGTH_SHORT).show()
            return // Stop execution if weight is empty
        }

        val dosage = dosageInput.toDoubleOrNull()
        if (dosage == null || dosage <= 0) {
            Toast.makeText(this, "Invalid dosage. Please enter a positive number (e.g., 500).", Toast.LENGTH_SHORT).show()
            return // Stop execution if dosage is not a valid positive number
        }

        val patientWeight = weightInput.toDoubleOrNull()
        if (patientWeight == null || patientWeight <= 0) {
            Toast.makeText(this, "Invalid patient weight. Please enter a positive number (e.g., 70).", Toast.LENGTH_SHORT).show()
            return // Stop execution if weight is not a valid positive number
        }

        // Retrieve the selected drug object
        val drug = drugData[selectedDrugName]
        drug?.let {
            // If drug found, run the simulation
            val simulationResult = simulateDrugEffect(it, dosage, patientWeight)
            tvSimulationResult.text = simulationResult.primaryResult
            tvAdditionalSimulationOutput.text = simulationResult.additionalInfo

            // Set background color based on simulation outcome color indicator
            tvSimulationResult.setBackgroundColor(simulationResult.colorIndicator)

        } ?: run {
            // This case should ideally not happen if spinner is populated correctly
            tvSimulationResult.text = "Error: Drug not found for simulation. Please try again."
            tvSimulationResult.setBackgroundColor(Color.RED) // Indicate error
            tvAdditionalSimulationOutput.text = ""
        }
    }

    /**
     * Simulates the drug's effect using a simplified one-compartment pharmacokinetic model.
     * Calculates peak concentration (Cmax), time to peak (Tmax), and half-life (t1/2).
     *
     * @param drug The Drug object containing its PK parameters.
     * @param dosageMg The dosage administered in milligrams (mg).
     * @param patientWeightKg The patient's weight in kilograms (kg).
     * @return A SimulationOutcome object containing primary and additional results, and a color indicator.
     */
    private fun simulateDrugEffect(drug: Drug, dosageMg: Double, patientWeightKg: Double): SimulationOutcome {
        // Retrieve PK parameters from the drug object
        val ka = drug.absorptionRateConstant       // Absorption rate constant (h^-1)
        val kel = drug.eliminationRateConstant     // Elimination rate constant (h^-1)

        // Calculate the patient-specific Volume of Distribution (Vd)
        // Vd determines how much of the drug stays in the blood vs. distributes into tissues.
        val vd = drug.volumeOfDistributionLKg * patientWeightKg // Vd in Liters (L)

        // Prevent division by zero if Vd somehow calculates to zero (should not happen with positive weight)
        if (vd == 0.0) {
            return SimulationOutcome("Error in calculation: Patient Vd is zero.",
                "Cannot perform simulation.", Color.RED)
        }

        // --- Pharmacokinetic Calculations (One-Compartment Extravascular Model) ---
        // These formulas are derived from standard pharmacokinetic equations.

        // Calculate Time to Maximum Concentration (Tmax)
        // This is the time when the drug concentration in the blood is highest.
        val tmax = if (ka != kel) log(ka / kel) / (ka - kel) else 0.0 // Avoid division by zero if ka == kel

        // Calculate Maximum Concentration (Cmax)
        // This is the highest drug concentration achieved in the blood.
        // The * 1000 converts mg/L to µg/mL (1 mg/L = 1 µg/mL).
        val peakConcentrationUgMl = (dosageMg / vd) * (ka / (ka - kel)) *
                (exp(-kel * tmax) - exp(-ka * tmax)) * 1000.0

        // Calculate Elimination Half-Life (t½)
        // The time it takes for the drug concentration to reduce by half.
        val halfLife = log(2.0) / kel

        // --- Interpret Simulation Results ---
        var primaryResultText: String
        var resultColor: Int = Color.GRAY // Default neutral color

        when {
            peakConcentrationUgMl > drug.toxicConcentration -> {
                primaryResultText = "Warning: Predicted Peak Concentration is in TOXIC range!"
                resultColor = Color.RED // High risk
            }
            peakConcentrationUgMl >= drug.therapeuticConcentrationMin && peakConcentrationUgMl <= drug.therapeuticConcentrationMax -> {
                primaryResultText = "Predicted Peak Concentration is within THERAPEUTIC range."
                resultColor = Color.parseColor("#4CAF50") // Therapeutic green
            }
            peakConcentrationUgMl < drug.therapeuticConcentrationMin -> {
                primaryResultText = "Caution: Predicted Peak Concentration is SUB-THERAPEUTIC."
                resultColor = Color.parseColor("#FF9800") // Orange for caution/sub-therapeutic
            }
            else -> {
                primaryResultText = "Simulated Peak Concentration: ${"%.2f".format(peakConcentrationUgMl)} µg/mL."
                resultColor = Color.DKGRAY // General informational color
            }
        }

        // --- Construct Additional Information String ---
        val additionalDetails = """
            --- Detailed Pharmacokinetics ---
            - Calculated Patient Vd: ${"%.2f".format(vd)} L
            - Estimated Peak Concentration (Cmax): ${"%.2f".format(peakConcentrationUgMl)} µg/mL
            - Estimated Time to Peak (Tmax): ${"%.2f".format(tmax)} hours
            - Estimated Half-Life (t½): ${"%.2f".format(halfLife)} hours

            --- Drug-Specific Interpretation ---
            ${getDrugSpecificInterpretation(drug, dosageMg, peakConcentrationUgMl)}
        """.trimIndent()

        return SimulationOutcome(primaryResultText, additionalDetails, resultColor)
    }

    /**
     * Provides drug-specific interpretations based on the simulated concentration and dosage.
     * This function contains simplified clinical advice based on general knowledge.
     * IT IS NOT MEDICAL ADVICE and should not be used for actual patient care.
     *
     * @param drug The Drug object.
     * @param dosage The dosage administered.
     * @param peakConcentration The simulated peak drug concentration.
     * @return A string containing drug-specific advice or warnings.
     */
    private fun getDrugSpecificInterpretation(drug: Drug, dosage: Double, peakConcentration: Double): String {
        return when (drug.name) {
            "Aspirin" -> {
                when {
                    dosage < 100 && peakConcentration < drug.therapeuticConcentrationMin -> "This dosage is typical for anti-platelet effects, not primary pain relief or anti-inflammatory action. Concentration is likely sub-therapeutic for analgesic action."
                    dosage >= 325 && dosage <= 650 && peakConcentration >= drug.therapeuticConcentrationMin -> "Dosage and concentration are within the expected range for analgesic and anti-inflammatory effects. Monitor for gastrointestinal upset or bleeding risk."
                    peakConcentration > drug.toxicConcentration -> "Very high concentration. Symptoms of salicylate toxicity include tinnitus (ringing in ears), dizziness, headache, nausea, and hyperventilation. Severe cases can lead to metabolic acidosis and coma."
                    else -> "Consider individual patient factors, such as kidney function and co-medications, for precise interpretation."
                }
            }
            "Paracetamol" -> {
                when {
                    // Very high single acute dose for Paracetamol (simplified threshold)
                    // Real-world: >7.5g or >10g single ingestion is highly concerning.
                    dosage > 7500.0 -> "EXTREMELY HIGH SINGLE DOSE! Significant and immediate risk of severe liver damage (hepatotoxicity). This requires urgent medical attention and treatment with N-acetylcysteine (NAC)."
                    peakConcentration > drug.toxicConcentration -> "Acute toxic concentration detected. Liver toxicity risk is significantly elevated. This level suggests a need for immediate medical review and potential intervention, especially if detected within hours of ingestion."
                    peakConcentration >= drug.therapeuticConcentrationMin && peakConcentration <= drug.therapeuticConcentrationMax -> "Therapeutic concentration achieved. For pain/fever, adhere strictly to a maximum of 4 grams (4000mg) per 24 hours to prevent liver damage from cumulative dosing."
                    peakConcentration < drug.therapeuticConcentrationMin -> "Low concentration, may not provide adequate pain relief or fever reduction. Consider if the dosage is appropriate for the patient's condition or if absorption issues exist."
                    else -> "Monitor liver function if prolonged use or high doses are considered. Always be aware of other paracetamol-containing medications."
                }
            }
            "Metformin" -> {
                when {
                    peakConcentration > drug.toxicConcentration -> "Very high concentration. While acute Metformin overdose alone is rarely fatal, it significantly increases the risk of lactic acidosis, especially in patients with impaired renal function, heart failure, or hypoxia. Immediate medical evaluation is crucial."
                    peakConcentration >= drug.therapeuticConcentrationMin && peakConcentration <= drug.therapeuticConcentrationMax -> "Therapeutic concentration for blood glucose control. Ensure adequate renal function as Metformin is primarily eliminated unchanged by the kidneys."
                    peakConcentration < drug.therapeuticConcentrationMin && dosage >= 500 -> "May be sub-therapeutic if this is an initial dose. Metformin doses are often titrated up gradually over weeks to minimize gastrointestinal side effects and achieve optimal glucose control."
                    else -> "The effectiveness of Metformin is also closely tied to dietary and exercise habits. Dosage adjustments should always be made under medical supervision."
                }
            }
            else -> "No specific interpretation available for this drug beyond its concentration ranges. Always consult medical professionals for accurate advice."
        }
    }

    /**
     * Data class to define the properties of a drug relevant to this simulation.
     */
    data class Drug(
        val name: String,
        val description: String,
        val typicalDosage: String,
        val sideEffects: String,
        val absorptionRateConstant: Double, // ka in h^-1 (rate of drug entering systemic circulation)
        val eliminationRateConstant: Double, // kel in h^-1 (rate of drug removal from the body)
        val volumeOfDistributionLKg: Double, // Vd in L/kg (apparent volume drug distributes into)
        val therapeuticConcentrationMin: Double, // ug/mL (lower bound of effective concentration)
        val therapeuticConcentrationMax: Double, // ug/mL (upper bound of effective and safe concentration)
        val toxicConcentration: Double, // ug/mL (concentration where adverse effects become significant)
        val notes: String // Additional general notes or warnings about the drug
    )

    /**
     * Data class to encapsulate the results of a drug simulation.
     * Includes the primary textual result, additional detailed info, and a color indicator for UI.
     */
    data class SimulationOutcome(
        val primaryResult: String,
        val additionalInfo: String,
        val colorIndicator: Int // Android Color.RED, Color.GREEN etc.
    )
}
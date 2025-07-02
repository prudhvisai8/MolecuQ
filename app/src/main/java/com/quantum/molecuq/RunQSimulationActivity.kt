package com.quantum.molecuq

import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlin.math.*
import kotlin.random.Random

/**
 * RunQSimulationActivity is the primary activity for the Quantum Qubit Simulator app.
 * It handles the UI interactions and the core quantum simulation logic for a single qubit,
 * including initialization, applying various quantum gates, and performing measurements.
 */
class RunQSimulationActivity : AppCompatActivity() {

    // UI Elements
    private lateinit var etAmplitude0Real: EditText
    private lateinit var etAmplitude0Imag: EditText
    private lateinit var etAmplitude1Real: EditText
    private lateinit var etAmplitude1Imag: EditText
    private lateinit var btnInitialize: Button
    private lateinit var btnHadamard: Button
    private lateinit var btnPauliX: Button
    private lateinit var btnPauliY: Button
    private lateinit var btnPauliZ: Button
    private lateinit var btnPhaseS: Button
    private lateinit var btnPhaseT: Button
    private lateinit var btnMeasure: Button
    private lateinit var btnReset: Button
    private lateinit var tvStateDetails: TextView
    private lateinit var backButton: ImageButton

    // Current Qubit state
    private var currentQubit: Qubit = Qubit() // Initialize with default |0> state

    /**
     * Called when the activity is first created.
     * Initializes the UI components and sets up event listeners.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_run_qsimulation)

        // Initialize UI components by finding their IDs from the layout
        etAmplitude0Real = findViewById(R.id.etAmplitude0Real)
        etAmplitude0Imag = findViewById(R.id.etAmplitude0Imag)
        etAmplitude1Real = findViewById(R.id.etAmplitude1Real)
        etAmplitude1Imag = findViewById(R.id.etAmplitude1Imag)
        btnInitialize = findViewById(R.id.btnInitialize)
        btnHadamard = findViewById(R.id.btnHadamard)
        btnPauliX = findViewById(R.id.btnPauliX)
        btnPauliY = findViewById(R.id.btnPauliY)
        btnPauliZ = findViewById(R.id.btnPauliZ)
        btnPhaseS = findViewById(R.id.btnPhaseS)
        btnPhaseT = findViewById(R.id.btnPhaseT)
        btnMeasure = findViewById(R.id.btnMeasure)
        btnReset = findViewById(R.id.btnReset)
        tvStateDetails = findViewById(R.id.tvStateDetails)
        backButton = findViewById(R.id.backButton)

        // Set up click listeners for the buttons
        btnInitialize.setOnClickListener { initializeQubit() }
        btnHadamard.setOnClickListener { applyGate(QuantumGate.HADAMARD) }
        btnPauliX.setOnClickListener { applyGate(QuantumGate.PAULI_X) }
        btnPauliY.setOnClickListener { applyGate(QuantumGate.PAULI_Y) }
        btnPauliZ.setOnClickListener { applyGate(QuantumGate.PAULI_Z) }
        btnPhaseS.setOnClickListener { applyGate(QuantumGate.PHASE_S) }
        btnPhaseT.setOnClickListener { applyGate(QuantumGate.PHASE_T) }
        btnMeasure.setOnClickListener { measureQubit() }
        btnReset.setOnClickListener { resetQubit() }

        // Set up click listener for the back button
        backButton.setOnClickListener {
            onBackPressedDispatcher.onBackPressed() // Navigate back
        }

        // Display the initial qubit state
        updateStateDisplay()
    }

    /**
     * Initializes the qubit state based on user input from EditText fields.
     * It parses real and imaginary parts for |0> and |1> amplitudes, normalizes them,
     * and updates the currentQubit object.
     */
    private fun initializeQubit() {
        try {
            // Parse real and imaginary parts for |0> amplitude
            val alphaReal = etAmplitude0Real.text.toString().toDoubleOrNull() ?: 0.0
            val alphaImag = etAmplitude0Imag.text.toString().toDoubleOrNull() ?: 0.0

            // Parse real and imaginary parts for |1> amplitude
            val betaReal = etAmplitude1Real.text.toString().toDoubleOrNull() ?: 0.0
            val betaImag = etAmplitude1Imag.text.toString().toDoubleOrNull() ?: 0.0

            // Create Complex numbers for amplitudes
            val alpha = Complex(alphaReal, alphaImag)
            val beta = Complex(betaReal, betaImag)

            // Calculate the normalization factor to ensure |alpha|^2 + |beta|^2 = 1
            val normSquared = alpha.magnitudeSquared() + beta.magnitudeSquared()

            if (normSquared == 0.0) {
                // If both amplitudes are zero, default to |0> state and show a warning
                currentQubit = Qubit()
                Toast.makeText(this, "Amplitudes cannot be all zero. Defaulting to |0⟩.", Toast.LENGTH_LONG).show()
            } else {
                // Normalize the amplitudes
                val normFactor = sqrt(normSquared)
                val normalizedAlpha = alpha.divide(normFactor)
                val normalizedBeta = beta.divide(normFactor)
                currentQubit = Qubit(normalizedAlpha, normalizedBeta)
                Toast.makeText(this, "Qubit initialized and normalized.", Toast.LENGTH_SHORT).show()
            }

            // Update the display with the new state
            updateStateDisplay()
        } catch (e: NumberFormatException) {
            // Handle cases where input is not a valid number
            Toast.makeText(this, "Invalid number format. Please enter valid numbers.", Toast.LENGTH_LONG).show()
        } catch (e: Exception) {
            // Catch any other unexpected errors
            Toast.makeText(this, "An error occurred: ${e.message}", Toast.LENGTH_LONG).show()
            e.printStackTrace()
        }
    }

    /**
     * Applies the specified quantum gate to the current qubit state.
     */
    private fun applyGate(gate: QuantumGate) {
        val (newAlpha, newBeta) = when (gate) {
            QuantumGate.HADAMARD -> {
                // H = 1/sqrt(2) * [[1, 1], [1, -1]]
                val newAlpha = currentQubit.alpha.add(currentQubit.beta).divide(sqrt(2.0))
                val newBeta = currentQubit.alpha.subtract(currentQubit.beta).divide(sqrt(2.0))
                Pair(newAlpha, newBeta)
            }
            QuantumGate.PAULI_X -> {
                // X = [[0, 1], [1, 0]]
                // |ψ'> = X|ψ> => α' = β, β' = α
                Pair(currentQubit.beta, currentQubit.alpha)
            }
            QuantumGate.PAULI_Y -> {
                // Y = [[0, -i], [i, 0]]
                // |ψ'> = Y|ψ> => α' = -iβ, β' = iα
                val imagUnit = Complex(0.0, 1.0)
                val newAlpha = currentQubit.beta.multiply(imagUnit.multiply(-1.0))
                val newBeta = currentQubit.alpha.multiply(imagUnit)
                Pair(newAlpha, newBeta)
            }
            QuantumGate.PAULI_Z -> {
                // Z = [[1, 0], [0, -1]]
                // |ψ'> = Z|ψ> => α' = α, β' = -β
                Pair(currentQubit.alpha, currentQubit.beta.multiply(-1.0))
            }
            QuantumGate.PHASE_S -> {
                // S = [[1, 0], [0, i]] (Phase gate, pi/2 phase shift)
                // |ψ'> = S|ψ> => α' = α, β' = iβ
                val imagUnit = Complex(0.0, 1.0)
                Pair(currentQubit.alpha, currentQubit.beta.multiply(imagUnit))
            }
            QuantumGate.PHASE_T -> {
                // T = [[1, 0], [0, e^(i*pi/4)]] (T-gate, pi/4 phase shift)
                // e^(i*pi/4) = cos(pi/4) + i*sin(pi/4) = 1/sqrt(2) + i/sqrt(2)
                val phaseFactor = Complex(1/sqrt(2.0), 1/sqrt(2.0))
                Pair(currentQubit.alpha, currentQubit.beta.multiply(phaseFactor))
            }
        }
        currentQubit = Qubit(newAlpha, newBeta)
        updateStateDisplay()
        Toast.makeText(this, "$gate gate applied.", Toast.LENGTH_SHORT).show()
    }

    /**
     * Simulates the measurement of the qubit.
     * The qubit collapses to either |0> or |1> based on its probabilities.
     */
    private fun measureQubit() {
        val prob0 = currentQubit.alpha.magnitudeSquared()
        val randomValue = Random.nextDouble() // Generate a random double between 0.0 (inclusive) and 1.0 (exclusive)

        val measuredState: String
        currentQubit = if (randomValue < prob0) {
            // Measure |0>
            measuredState = "|0⟩"
            Qubit(Complex(1.0, 0.0), Complex(0.0, 0.0)) // Collapse to |0>
        } else {
            // Measure |1>
            measuredState = "|1⟩"
            Qubit(Complex(0.0, 0.0), Complex(1.0, 0.0)) // Collapse to |1>
        }
        updateStateDisplay()
        Toast.makeText(this, "Qubit measured: $measuredState", Toast.LENGTH_LONG).show()
    }

    /**
     * Resets the qubit state to the initial |0> state.
     */
    private fun resetQubit() {
        currentQubit = Qubit() // Resets to alpha=1, beta=0
        updateStateDisplay()
        Toast.makeText(this, "Qubit reset to |0⟩ state.", Toast.LENGTH_SHORT).show()
    }

    /**
     * Updates the TextView to display the current state of the qubit.
     * Shows the complex amplitudes for |0> (alpha) and |1> (beta),
     * and the probabilities of measuring |0> and |1>.
     */
    private fun updateStateDisplay() {
        val alpha = currentQubit.alpha
        val beta = currentQubit.beta

        // Calculate probabilities (already normalized, so squared magnitude is the probability)
        val prob0 = alpha.magnitudeSquared() * 100
        val prob1 = beta.magnitudeSquared() * 100

        // Format the output string
        val stateText = String.format(
            "Current Qubit State:\nα: %.2f + %.2fi\nβ: %.2f + %.2fi\n\nP(|0⟩): %.2f%%\nP(|1⟩): %.2f%%\n\n(State updates after initialization or gate application)",
            alpha.real, alpha.imaginary,
            beta.real, beta.imaginary,
            prob0, prob1
        )
        tvStateDetails.text = stateText
    }

    /**
     * Represents a complex number with real and imaginary parts.
     * Provides basic arithmetic operations for complex numbers.
     */
    data class Complex(val real: Double, val imaginary: Double) {
        /**
         * Adds another complex number to this one.
         */
        fun add(other: Complex): Complex {
            return Complex(real + other.real, imaginary + other.imaginary)
        }

        /**
         * Subtracts another complex number from this one.
         */
        fun subtract(other: Complex): Complex {
            return Complex(real - other.real, imaginary - other.imaginary)
        }

        /**
         * Multiplies this complex number by another complex number.
         */
        fun multiply(other: Complex): Complex {
            return Complex(
                real * other.real - imaginary * other.imaginary,
                real * other.imaginary + imaginary * other.real
            )
        }

        /**
         * Multiplies this complex number by a scalar (real number).
         */
        fun multiply(scalar: Double): Complex {
            return Complex(real * scalar, imaginary * scalar)
        }

        /**
         * Divides this complex number by a scalar (real number).
         */
        fun divide(scalar: Double): Complex {
            if (scalar == 0.0) throw IllegalArgumentException("Division by zero")
            return Complex(real / scalar, imaginary / scalar)
        }

        /**
         * Returns the squared magnitude (absolute value squared) of the complex number.
         * |z|^2 = real^2 + imaginary^2
         */
        fun magnitudeSquared(): Double {
            return real * real + imaginary * imaginary
        }

        /**
         * Returns the magnitude (absolute value) of the complex number.
         * |z| = sqrt(real^2 + imaginary^2)
         */
        fun magnitude(): Double {
            return sqrt(magnitudeSquared())
        }
    }

    /**
     * Represents the state of a single qubit.
     * A qubit is defined by two complex amplitudes, alpha for |0> and beta for |1>.
     * By default, it initializes to the |0> state (alpha=1, beta=0).
     */
    data class Qubit(val alpha: Complex = Complex(1.0, 0.0), val beta: Complex = Complex(0.0, 0.0))

    /**
     * Enum class to represent different types of quantum gates.
     */
    enum class QuantumGate {
        HADAMARD,
        PAULI_X,
        PAULI_Y,
        PAULI_Z,
        PHASE_S, // S-gate, pi/2 phase shift
        PHASE_T  // T-gate, pi/4 phase shift
    }
}
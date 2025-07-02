// MainActivity.kt
package com.quantum.molecuq

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlin.math.sqrt
import kotlin.math.roundToInt
import kotlin.random.Random // Import for random number generation for measurement

// 1. Define a Complex number class
/**
 * Represents a complex number with a real and imaginary part.
 */
data class Complex(val real: Double, val imag: Double) {
    /**
     * Adds two complex numbers.
     */
    operator fun plus(other: Complex) = Complex(real + other.real, imag + other.imag)

    /**
     * Subtracts one complex number from another.
     */
    operator fun minus(other: Complex) = Complex(real - other.real, imag - other.imag)

    /**
     * Multiplies two complex numbers.
     * (a + bi)(c + di) = (ac - bd) + (ad + bc)i
     */
    operator fun times(other: Complex) =
        Complex(real * other.real - imag * other.imag, real * other.imag + imag * other.real)

    /**
     * Multiplies a complex number by a scalar (double).
     */
    operator fun times(scalar: Double) = Complex(real * scalar, imag * scalar)

    /**
     * Returns the complex conjugate of the number.
     */
    fun conjugate() = Complex(real, -imag)

    /**
     * Calculates the squared magnitude (absolute value squared) of the complex number.
     * |z|^2 = a^2 + b^2
     */
    fun magnitudeSquared() = real * real + imag * imag

    /**
     * Calculates the magnitude (absolute value) of the complex number.
     * |z| = sqrt(magnitudeSquared())
     */
    fun magnitude() = sqrt(magnitudeSquared())

    /**
     * Formats the complex number as a string (e.g., "1.00 + 0.50i").
     */
    override fun toString(): String {
        return String.format("%.2f %s %.2fi", real, if (imag >= 0) "+" else "-", kotlin.math.abs(imag))
    }

    companion object {
        /**
         * Represents the imaginary unit 'i'.
         */
        val I = Complex(0.0, 1.0)
    }
}

// 2. Define a Qubit class
/**
 * Represents a single qubit's state using complex amplitudes for |0> and |1>.
 * The state is normalized such that |alpha|^2 + |beta|^2 = 1.
 */
data class Qubit(var alpha: Complex, var beta: Complex) {

    init {
        // Ensure the initial state is normalized
        normalize()
    }

    /**
     * Normalizes the qubit state such that the sum of the squared magnitudes of amplitudes is 1.
     */
    fun normalize() {
        val sumSq = alpha.magnitudeSquared() + beta.magnitudeSquared()
        if (sumSq > 0) {
            val factor = 1.0 / sqrt(sumSq)
            alpha = alpha * factor
            beta = beta * factor
        }
    }

    /**
     * Applies a 2x2 unitary quantum gate matrix to the qubit.
     * A quantum gate is represented by a matrix of Complex numbers.
     *
     * @param gate A 2x2 matrix of Complex numbers representing the quantum gate.
     * gate[0][0]  gate[0][1]
     * gate[1][0]  gate[1][1]
     */
    fun applyGate(gate: Array<Array<Complex>>) {
        val newAlpha = gate[0][0] * alpha + gate[0][1] * beta
        val newBeta = gate[1][0] * alpha + gate[1][1] * beta
        this.alpha = newAlpha
        this.beta = newBeta
        normalize() // Re-normalize after applying the gate due to potential floating point errors
    }

    /**
     * Resets the qubit to the |0> state (alpha = 1, beta = 0).
     */
    fun reset() {
        alpha = Complex(1.0, 0.0)
        beta = Complex(0.0, 0.0)
        normalize()
    }

    /**
     * Gets the probability of measuring the qubit in the |0> state.
     * P(|0>) = |alpha|^2
     */
    fun probabilityZero(): Double = alpha.magnitudeSquared()

    /**
     * Gets the probability of measuring the qubit in the |1> state.
     * P(|1>) = |beta|^2
     */
    fun probabilityOne(): Double = beta.magnitudeSquared()

    /**
     * Simulates a measurement of the qubit.
     * The qubit collapses to either |0> or |1> based on probabilities.
     *
     * @return The measured classical bit (0 or 1).
     */
    fun measure(): Int {
        val p0 = probabilityZero()
        val randomValue = Random.nextDouble() // Generates a random double between 0.0 (inclusive) and 1.0 (exclusive)

        return if (randomValue < p0) {
            // Collapse to |0>
            alpha = Complex(1.0, 0.0)
            beta = Complex(0.0, 0.0)
            0
        } else {
            // Collapse to |1>
            alpha = Complex(0.0, 0.0)
            beta = Complex(1.0, 0.0)
            1
        }.also {
            normalize() // Ensure normalization after collapse
        }
    }
}

class QuantumEncodingActivity : AppCompatActivity() {

    // UI elements
    private lateinit var qubitStateTextView: TextView
    private lateinit var hadamardButton: Button
    private lateinit var pauliXButton: Button
    private lateinit var pauliYButton: Button
    private lateinit var pauliZButton: Button
    private lateinit var phaseSButton: Button
    private lateinit var measureButton: Button
    private lateinit var resetButton: Button
    private lateinit var measurementResultTextView: TextView


    // The qubit instance
    private lateinit var currentQubit: Qubit

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quantum_encoding)

        // Initialize UI elements
        qubitStateTextView = findViewById(R.id.qubitStateTextView)
        hadamardButton = findViewById(R.id.hadamardButton)
        pauliXButton = findViewById(R.id.pauliXButton)
        pauliYButton = findViewById(R.id.pauliYButton)
        pauliZButton = findViewById(R.id.pauliZButton)
        phaseSButton = findViewById(R.id.phaseSButton)
        measureButton = findViewById(R.id.measureButton)
        resetButton = findViewById(R.id.resetButton)
        measurementResultTextView = findViewById(R.id.measurementResultTextView)

        // Initialize the qubit to the |0> state
        currentQubit = Qubit(alpha = Complex(1.0, 0.0), beta = Complex(0.0, 0.0))

        // Update the display for the initial state
        updateQubitDisplay()

        // Set up click listeners for the buttons
        hadamardButton.setOnClickListener {
            applyHadamardGate()
            measurementResultTextView.text = "" // Clear previous measurement result
        }

        pauliXButton.setOnClickListener {
            applyPauliXGate()
            measurementResultTextView.text = "" // Clear previous measurement result
        }

        pauliYButton.setOnClickListener {
            applyPauliYGate() // Call new gate function
            measurementResultTextView.text = "" // Clear previous measurement result
        }

        pauliZButton.setOnClickListener {
            applyPauliZGate() // Call new gate function
            measurementResultTextView.text = "" // Clear previous measurement result
        }

        phaseSButton.setOnClickListener {
            applyPhaseSGate() // Call new gate function
            measurementResultTextView.text = "" // Clear previous measurement result
        }

        measureButton.setOnClickListener {
            val result = currentQubit.measure() // Perform measurement
            measurementResultTextView.text = "Measured: |$result>" // Display result
            updateQubitDisplay() // Update qubit state after measurement collapse
        }

        resetButton.setOnClickListener {
            currentQubit.reset()
            measurementResultTextView.text = "" // Clear previous measurement result
            updateQubitDisplay()
        }
    }

    /**
     * Applies the Hadamard (H) gate to the current qubit.
     * H = 1/sqrt(2) * | 1  1 |
     * | 1 -1 |
     */
    private fun applyHadamardGate() {
        val sqrt2Inv = 1.0 / sqrt(2.0)
        val hGate = arrayOf(
            arrayOf(Complex(sqrt2Inv, 0.0), Complex(sqrt2Inv, 0.0)),
            arrayOf(Complex(sqrt2Inv, 0.0), Complex(-sqrt2Inv, 0.0))
        )
        currentQubit.applyGate(hGate)
        updateQubitDisplay()
    }

    /**
     * Applies the Pauli-X (NOT) gate to the current qubit.
     * X = | 0  1 |
     * | 1  0 |
     */
    private fun applyPauliXGate() {
        val xGate = arrayOf(
            arrayOf(Complex(0.0, 0.0), Complex(1.0, 0.0)),
            arrayOf(Complex(1.0, 0.0), Complex(0.0, 0.0))
        )
        currentQubit.applyGate(xGate)
        updateQubitDisplay()
    }

    /**
     * Applies the Pauli-Y (Y) gate to the current qubit.
     * Y = | 0 -i |
     * | i  0 |
     */
    private fun applyPauliYGate() {
        val yGate = arrayOf(
            arrayOf(Complex(0.0, 0.0), Complex.I * -1.0), // -i
            arrayOf(Complex.I, Complex(0.0, 0.0))          // i
        )
        currentQubit.applyGate(yGate)
        updateQubitDisplay()
    }

    /**
     * Applies the Pauli-Z (Z) gate to the current qubit.
     * Z = | 1  0 |
     * | 0 -1 |
     */
    private fun applyPauliZGate() {
        val zGate = arrayOf(
            arrayOf(Complex(1.0, 0.0), Complex(0.0, 0.0)),
            arrayOf(Complex(0.0, 0.0), Complex(-1.0, 0.0))
        )
        currentQubit.applyGate(zGate)
        updateQubitDisplay()
    }

    /**
     * Applies the Phase (S) gate to the current qubit.
     * S = | 1  0 |
     * | 0  i |
     */
    private fun applyPhaseSGate() {
        val sGate = arrayOf(
            arrayOf(Complex(1.0, 0.0), Complex(0.0, 0.0)),
            arrayOf(Complex(0.0, 0.0), Complex.I) // i
        )
        currentQubit.applyGate(sGate)
        updateQubitDisplay()
    }

    /**
     * Updates the TextView to display the current state of the qubit,
     * including amplitudes and probabilities.
     */
    private fun updateQubitDisplay() {
        val alphaProb = (currentQubit.probabilityZero() * 10000).roundToInt() / 100.0
        val betaProb = (currentQubit.probabilityOne() * 10000).roundToInt() / 100.0

        val stateText = "α: ${currentQubit.alpha} (P=${String.format("%.2f", alphaProb)}% |0>)\n" +
                "β: ${currentQubit.beta} (P=${String.format("%.2f", betaProb)}% |1>)"
        qubitStateTextView.text = stateText
    }
}

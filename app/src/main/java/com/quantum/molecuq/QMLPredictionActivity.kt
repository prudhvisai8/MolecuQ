package com.quantum.molecuq

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RadialGradient
import android.graphics.Shader
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.RelativeSizeSpan
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.GridLayout
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.SeekBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.widget.NestedScrollView
import kotlin.math.*
import kotlin.random.Random

// BlochSphereView.kt (Normally a separate file, included here for brevity)
class BlochSphereView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val circlePaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val dotPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val textPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        textSize = 24f
        textAlign = Paint.Align.CENTER
        // Initial color for Bloch sphere labels, will be set from resources in onDraw
    }
    private val axisPaint = Paint(Paint(Paint.ANTI_ALIAS_FLAG)).apply {
        strokeWidth = 2f
    }

    var prob0: Float = 1.0f // Probability of being in |0> state
        set(value) {
            field = value.coerceIn(0.0f, 1.0f)
            invalidate()
        }

    init {
        // Dot color (red accent)
        dotPaint.color = ContextCompat.getColor(context, R.color.rose)
        // Axis color (medium gray)
        axisPaint.color = ContextCompat.getColor(context, R.color.gray_medium)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val centerX = width / 2f
        val centerY = height / 2f
        val radius = min(centerX, centerY) * 0.8f

        // Dynamic circle color based on prob0 (using light theme accent colors for visibility)
        val hslColor = ColorUtils.hslToColor(floatArrayOf(prob0 * 180f, 0.7f, 0.6f))

        val gradient = RadialGradient(
            centerX, dotYForGradient(radius), radius,
            intArrayOf(hslColor, Color.TRANSPARENT),
            floatArrayOf(0.0f, 0.7f),
            Shader.TileMode.CLAMP
        )
        circlePaint.shader = gradient
        canvas.drawCircle(centerX, centerY, radius, circlePaint)

        canvas.drawLine(centerX, centerY - radius, centerX, centerY + radius, axisPaint)
        canvas.drawLine(centerX - radius, centerY, centerX + radius, centerY, axisPaint)

        val textOffset = radius * 0.9f
        // Set text color for Bloch sphere labels to a light color for dark background
        textPaint.color = ContextCompat.getColor(context, R.color.blue_dark)
        canvas.drawText("0⟩", centerX, centerY - textOffset + textPaint.textSize / 2, textPaint)
        canvas.drawText("1⟩", centerX, centerY + textOffset + textPaint.textSize / 2, textPaint)
        canvas.drawText("+⟩", centerX - textOffset, centerY + textPaint.textSize / 2, textPaint)
        canvas.drawText("-⟩", centerX + textOffset, centerY + textPaint.textSize / 2, textPaint)

        val dotY = (1 - prob0) * (2 * radius) - radius
        canvas.drawCircle(centerX, centerY + dotY, 8f, dotPaint)
    }

    private fun dotYForGradient(radius: Float): Float {
        return (1 - prob0) * height
    }
}

object ColorUtils {
    fun hslToColor(hsl: FloatArray): Int {
        val h = hsl[0]
        val s = hsl[1]
        val l = hsl[2]

        val c = (1 - abs(2 * l - 1)) * s
        val x = c * (1 - abs((h / 60) % 2 - 1))
        val m = l - c / 2

        val (r, g, b) = when (h.toInt()) {
            in 0..59 -> Triple(c, x, 0f)
            in 60..119 -> Triple(x, c, 0f)
            in 120..179 -> Triple(0f, c, x)
            in 180..239 -> Triple(0f, x, c)
            in 240..299 -> Triple(x, 0f, c)
            else -> Triple(c, 0f, x)
        }

        return Color.rgb(
            ((r + m) * 255).toInt(),
            ((g + m) * 255).toInt(),
            ((b + m) * 255).toInt()
        )
    }
}


class QMLPredictionActivity : AppCompatActivity() {

    private lateinit var btnBack: ImageButton
    private lateinit var quantumStateTextView: TextView
    private lateinit var measurementResultTextView: TextView
    private lateinit var blochSphereQ1: BlochSphereView
    private lateinit var blochSphereQ2: BlochSphereView
    private lateinit var probabilitiesGridLayout: GridLayout
    private lateinit var ryAngleSlider: SeekBar
    private lateinit var ryAngleTextView: TextView
    private lateinit var circuitHistoryLayout: LinearLayout
    private lateinit var btnResetTo00: Button
    private lateinit var btnCreateBellState: Button
    private lateinit var btnMeasureState: Button
    private lateinit var btnFullReset: Button
    private lateinit var scrollView: NestedScrollView

    private var quantumState = floatArrayOf(1f, 0f, 0f, 0f)
    private var measurementResult: String? = null
    private var isMeasured = false
    private val circuitHistory = mutableListOf<Map<String, Any>>()
    private var ryAngle: Float = PI.toFloat() / 2

    private val basisStates = arrayOf("00", "01", "10", "11")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_qmlprediction)

        btnBack = findViewById(R.id.btnBack)
        quantumStateTextView = findViewById(R.id.quantumStateTextView)
        measurementResultTextView = findViewById(R.id.measurementResultTextView)
        blochSphereQ1 = findViewById(R.id.blochSphereQ1)
        blochSphereQ2 = findViewById(R.id.blochSphereQ2)
        probabilitiesGridLayout = findViewById(R.id.probabilitiesGridLayout)
        ryAngleSlider = findViewById(R.id.ryAngleSlider)
        ryAngleTextView = findViewById(R.id.ryAngleTextView)
        circuitHistoryLayout = findViewById(R.id.circuitHistoryLayout)
        btnResetTo00 = findViewById(R.id.btnResetTo00)
        btnCreateBellState = findViewById(R.id.btnCreateBellState)
        btnMeasureState = findViewById(R.id.btnMeasureState)
        btnFullReset = findViewById(R.id.btnFullReset)
        scrollView = findViewById(R.id.scrollView)

        btnBack.setOnClickListener { onBackPressedDispatcher.onBackPressed() }
        btnResetTo00.setOnClickListener { setInitialState("00") }
        btnCreateBellState.setOnClickListener { setInitialState("bell") }
        btnMeasureState.setOnClickListener { measureState() }
        btnFullReset.setOnClickListener { resetSimulation() }

        findViewById<Button>(R.id.btnH1).setOnClickListener { handleApplyGate("H", 0) }
        findViewById<Button>(R.id.btnH2).setOnClickListener { handleApplyGate("H", 1) }
        findViewById<Button>(R.id.btnX1).setOnClickListener { handleApplyGate("X", 0) }
        findViewById<Button>(R.id.btnX2).setOnClickListener { handleApplyGate("X", 1) }
        findViewById<Button>(R.id.btnRy1).setOnClickListener { handleApplyGate("Ry", 0, null, ryAngle) }
        findViewById<Button>(R.id.btnRy2).setOnClickListener { handleApplyGate("Ry", 1, null, ryAngle) }
        findViewById<Button>(R.id.btnS1).setOnClickListener { handleApplyGate("S", 0) }
        findViewById<Button>(R.id.btnS2).setOnClickListener { handleApplyGate("S", 1) }
        findViewById<Button>(R.id.btnT1).setOnClickListener { handleApplyGate("T", 0) }
        findViewById<Button>(R.id.btnT2).setOnClickListener { handleApplyGate("T", 1) }
        findViewById<Button>(R.id.btnCNOT_Q1Ctrl_Q2Target).setOnClickListener { handleApplyGate("CNOT", 1, 0) }
        findViewById<Button>(R.id.btnCNOT_Q2Ctrl_Q1Target).setOnClickListener { handleApplyGate("CNOT", 0, 1) }
        findViewById<Button>(R.id.btnSWAP).setOnClickListener { handleApplyGate("SWAP", 0, 1) }

        ryAngleSlider.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                ryAngle = (progress / 100.0 * (2 * PI)).toFloat()
                ryAngleTextView.text = "Ry Angle: ${ryAngle.format(2)} rad"
            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        updateUI()
    }

    private fun normalizeState(state: FloatArray): FloatArray {
        val sumOfSquares = state.sumOf { (it * it).toDouble() }.toFloat()
        if (sumOfSquares == 0f) return state
        val magnitude = sqrt(sumOfSquares)
        return state.map { it / magnitude }.toFloatArray()
    }

    private fun applyGate(currentState: FloatArray, gate: String, qubitIndex: Int, controlQubitIndex: Int? = null, angle: Float = 0f): FloatArray {
        if (isMeasured) return currentState

        val newState = currentState.copyOf()
        val numQubits = log2(currentState.size.toDouble()).toInt()

        when (gate) {
            "H" -> { // Hadamard Gate
                val sqrt2 = sqrt(2.0f)
                for (i in newState.indices) {
                    val qubitValue = (i shr (numQubits - 1 - qubitIndex)) and 1
                    if (qubitValue == 0) {
                        val flippedStateIndex = i or (1 shl (numQubits - 1 - qubitIndex))
                        if (i < flippedStateIndex) {
                            val amp0 = currentState[i]
                            val amp1 = currentState[flippedStateIndex]
                            newState[i] = (amp0 + amp1) / sqrt2
                            newState[flippedStateIndex] = (amp0 - amp1) / sqrt2
                        }
                    }
                }
            }
            "X" -> { // Pauli-X Gate
                for (i in newState.indices) {
                    val qubitValue = (i shr (numQubits - 1 - qubitIndex)) and 1
                    if (qubitValue == 0) {
                        val flippedStateIndex = i or (1 shl (numQubits - 1 - qubitIndex))
                        if (i < flippedStateIndex) {
                            val temp = newState[i]
                            newState[i] = newState[flippedStateIndex]
                            newState[flippedStateIndex] = temp
                        }
                    }
                }
            }
            "Ry" -> { // Rotation around Y-axis
                val cosAngle = cos(angle / 2)
                val sinAngle = sin(angle / 2)

                for (i in newState.indices) {
                    val qubitValue = (i shr (numQubits - 1 - qubitIndex)) and 1
                    if (qubitValue == 0) {
                        val conjugateStateIndex = i or (1 shl (numQubits - 1 - qubitIndex))
                        if (i < conjugateStateIndex) {
                            val amp0 = currentState[i]
                            val amp1 = currentState[conjugateStateIndex]
                            newState[i] = amp0 * cosAngle - amp1 * sinAngle
                            newState[conjugateStateIndex] = amp0 * sinAngle + amp1 * cosAngle
                        }
                    }
                }
            }
            "Rz" -> { // Rotation around Z-axis (conceptual for real amplitudes)
                println("Rz(${angle.format(2)}) applied to Qubit ${qubitIndex + 1}. Note: Rz only affects phase, not real amplitudes in this simplified model.")
            }
            "S" -> { // Phase Shift pi/2 gate (conceptual for real amplitudes)
                println("S gate applied to Qubit ${qubitIndex + 1}. Note: S gate applies a phase, not visible in this real-amplitude simulation.")
            }
            "T" -> { // Phase Shift pi/4 gate (conceptual for real amplitudes)
                println("T gate applied to Qubit ${qubitIndex + 1}. Note: T gate applies a phase, not visible in this real-amplitude simulation.")
            }
            "SWAP" -> { // SWAP Gate
                if (qubitIndex == controlQubitIndex) return currentState

                for (i in newState.indices) {
                    val bit1 = (i shr (numQubits - 1 - qubitIndex)) and 1
                    val bit2 = (i shr (numQubits - 1 - controlQubitIndex!!)) and 1

                    if (bit1 != bit2) {
                        val mask = ((1 shl (numQubits - 1 - qubitIndex)) or (1 shl (numQubits - 1 - controlQubitIndex))).inv()
                        val baseState = i and mask

                        var swappedIndex = baseState
                        if (bit2 == 1) swappedIndex = swappedIndex or (1 shl (numQubits - 1 - qubitIndex))
                        if (bit1 == 1) swappedIndex = swappedIndex or (1 shl (numQubits - 1 - controlQubitIndex))

                        if (i < swappedIndex) {
                            val temp = newState[i]
                            newState[i] = newState[swappedIndex]
                            newState[swappedIndex] = temp
                        }
                    }
                }
            }
            "CNOT" -> { // Controlled-NOT Gate
                if (controlQubitIndex == null || qubitIndex == controlQubitIndex) return currentState

                for (i in newState.indices) {
                    val controlValue = (i shr (numQubits - 1 - controlQubitIndex)) and 1
                    if (controlValue == 1) {
                        val flippedStateIndex = i xor (1 shl (numQubits - 1 - qubitIndex))
                        if (i < flippedStateIndex) {
                            val temp = newState[i]
                            newState[i] = newState[flippedStateIndex]
                            newState[flippedStateIndex] = temp
                        }
                    }
                }
            }
            else -> println("Unknown gate: $gate")
        }
        return normalizeState(newState)
    }

    private fun setInitialState(type: String) {
        var newState = floatArrayOf(0f, 0f, 0f, 0f)
        circuitHistory.clear()

        if (type == "00") {
            newState[0] = 1f
        } else if (type == "bell") {
            var tempState = floatArrayOf(1f, 0f, 0f, 0f)
            tempState = applyGate(tempState, "H", 0)
            tempState = applyGate(tempState, "CNOT", 1, 0)
            newState = tempState
            circuitHistory.add(mapOf("gate" to "H", "qubit" to 0))
            circuitHistory.add(mapOf("gate" to "CNOT", "qubit" to 1, "control" to 0))
        }
        quantumState = normalizeState(newState)
        measurementResult = null
        isMeasured = false
        updateUI()
    }

    private fun handleApplyGate(gate: String, qubitIndex: Int, controlQubitIndex: Int? = null, angle: Float = 0f) {
        if (isMeasured) return
        val newQuantumState = applyGate(quantumState, gate, qubitIndex, controlQubitIndex, angle)
        quantumState = newQuantumState
        val gateInfo = mutableMapOf<String, Any>("gate" to gate, "qubit" to qubitIndex)
        controlQubitIndex?.let { gateInfo["control"] = it }
        if (angle != 0f) gateInfo["angle"] = angle
        circuitHistory.add(gateInfo)
        updateUI()
    }

    private fun measureState() {
        if (isMeasured) return

        val probabilities = quantumState.map { it * it }
        var cumulativeProb = 0f
        val randomValue = Random.nextFloat()

        var resultIndex = -1
        for (i in probabilities.indices) {
            cumulativeProb += probabilities[i]
            if (randomValue < cumulativeProb) {
                resultIndex = i
                break
            }
        }

        val collapsedState = FloatArray(4) { 0f }
        if (resultIndex != -1) {
            collapsedState[resultIndex] = 1f
        }
        quantumState = collapsedState
        measurementResult = basisStates[resultIndex]
        isMeasured = true
        updateUI()
    }

    private fun resetSimulation() {
        quantumState = floatArrayOf(1f, 0f, 0f, 0f)
        measurementResult = null
        isMeasured = false
        circuitHistory.clear()
        ryAngle = PI.toFloat() / 2
        ryAngleSlider.progress = (ryAngle / (2 * PI) * 100).toInt()
        updateUI()
    }

    @SuppressLint("SetTextI18n")
    private fun updateUI() {
        // Update Quantum State Display
        if (isMeasured) {
            quantumStateTextView.text = ""
            measurementResultTextView.text = "Measured: |${measurementResult}⟩"
            measurementResultTextView.setTextColor(ContextCompat.getColor(this, R.color.green_dark)) // Green accent for measurement result
        } else {
            measurementResultTextView.text = ""
            val stateString = quantumState.mapIndexed { index, amp ->
                if (abs(amp) > 1e-6) {
                    "${amp.format(3)}|${basisStates[index]}⟩"
                } else {
                    ""
                }
            }.filter { it.isNotEmpty() }.joinToString(" + ")

            quantumStateTextView.text = if (stateString.isEmpty()) "|ψ⟩ = |00⟩ (effectively)" else "|ψ⟩ = $stateString"
            quantumStateTextView.setTextColor(ContextCompat.getColor(this, R.color.quantum_dark)) // Light text for dark background
        }

        // Update Bloch Spheres
        val probQubit1_0 = quantumState[0] * quantumState[0] + quantumState[1] * quantumState[1]
        val probQubit2_0 = quantumState[0] * quantumState[0] + quantumState[2] * quantumState[2]
        blochSphereQ1.prob0 = probQubit1_0
        blochSphereQ2.prob0 = probQubit2_0

        // Update Measurement Probabilities Grid
        probabilitiesGridLayout.removeAllViews()
        for (i in basisStates.indices) {
            val prob = quantumState[i] * quantumState[i]
            val itemView = createProbabilityItemView(basisStates[i], prob)
            probabilitiesGridLayout.addView(itemView)
        }

        // Update Circuit History
        circuitHistoryLayout.removeAllViews()
        if (circuitHistory.isEmpty()) {
            val textView = TextView(this).apply {
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
                text = "No gates applied yet. Start building your circuit!"
                setTextColor(ContextCompat.getColor(context, R.color.gray_dark)) // Medium light for history text
                setPadding(12.dpToPx(context), 12.dpToPx(context), 12.dpToPx(context), 12.dpToPx(context))
            }
            circuitHistoryLayout.addView(textView)
        } else {
            circuitHistory.forEachIndexed { index, step ->
                val textView = TextView(this).apply {
                    layoutParams = LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                    ).apply {
                        setMargins(0, 8.dpToPx(context), 0, 8.dpToPx(context))
                    }
                    val gateName = step["gate"] as String
                    val qubit = (step["qubit"] as Int) + 1
                    val control = step["control"] as Int?
                    val angle = step["angle"] as Float?

                    val textBuilder = StringBuilder()
                    textBuilder.append("${index + 1}. ")
                    textBuilder.append("$gateName on Qubit $qubit")
                    control?.let { textBuilder.append(" (Control Qubit ${it + 1})") }
                    angle?.let { if (it != 0f) textBuilder.append(" (Angle: ${it.format(2)} rad)") }

                    setTextColor(ContextCompat.getColor(context, R.color.quantum_dark)) // Medium light for history text
                    setPadding(12.dpToPx(context), 12.dpToPx(context), 12.dpToPx(context), 12.dpToPx(context))
                    setBackgroundResource(if (index % 2 == 0) R.drawable.rounded_bg_dark else R.drawable.rounded_bg_dark_alt) // Alternating dark backgrounds
                    setTextAppearance(androidx.appcompat.R.style.TextAppearance_AppCompat_Small)

                    val spannableString = SpannableString(textBuilder.toString())
                    val gateNameStart = textBuilder.indexOf(gateName)
                    if (gateNameStart != -1) {
                        spannableString.setSpan(
                            android.text.style.ForegroundColorSpan(ContextCompat.getColor(context, R.color.cyan_light)),
                            gateNameStart,
                            gateNameStart + gateName.length,
                            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                        )
                        spannableString.setSpan(
                            android.text.style.StyleSpan(android.graphics.Typeface.BOLD),
                            gateNameStart,
                            gateNameStart + gateName.length,
                            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                        )
                    }
                    this.text = spannableString
                }
                circuitHistoryLayout.addView(textView)
            }
        }
        scrollView.post {
            scrollView.smoothScrollTo(0, circuitHistoryLayout.bottom)
        }

        val enableButtons = !isMeasured
        findViewById<Button>(R.id.btnH1).isEnabled = enableButtons
        findViewById<Button>(R.id.btnH2).isEnabled = enableButtons
        findViewById<Button>(R.id.btnX1).isEnabled = enableButtons
        findViewById<Button>(R.id.btnX2).isEnabled = enableButtons
        findViewById<Button>(R.id.btnRy1).isEnabled = enableButtons
        findViewById<Button>(R.id.btnRy2).isEnabled = enableButtons
        findViewById<Button>(R.id.btnS1).isEnabled = enableButtons
        findViewById<Button>(R.id.btnS2).isEnabled = enableButtons
        findViewById<Button>(R.id.btnT1).isEnabled = enableButtons
        findViewById<Button>(R.id.btnT2).isEnabled = enableButtons
        findViewById<Button>(R.id.btnCNOT_Q1Ctrl_Q2Target).isEnabled = enableButtons
        findViewById<Button>(R.id.btnCNOT_Q2Ctrl_Q1Target).isEnabled = enableButtons
        findViewById<Button>(R.id.btnSWAP).isEnabled = enableButtons
        ryAngleSlider.isEnabled = enableButtons

        btnResetTo00.isEnabled = enableButtons
        btnCreateBellState.isEnabled = enableButtons
        btnMeasureState.isEnabled = enableButtons
        btnFullReset.isEnabled = true
    }

    private fun createProbabilityItemView(basisState: String, probability: Float): View {
        val context = this
        val linearLayout = LinearLayout(context).apply {
            layoutParams = GridLayout.LayoutParams().apply {
                width = 0
                height = ViewGroup.LayoutParams.WRAP_CONTENT
                columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f)
                rowSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f)
                setMargins(8.dpToPx(context), 8.dpToPx(context), 8.dpToPx(context), 8.dpToPx(context))
            }
            orientation = LinearLayout.VERTICAL
            gravity = Gravity.CENTER
            setPadding(12.dpToPx(context), 12.dpToPx(context), 12.dpToPx(context), 12.dpToPx(context))
        }

        val stateTextView = TextView(context).apply {
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            text = "|${basisState}⟩"
            textSize = 18f
            setTextColor(ContextCompat.getColor(context, R.color.quantum_dark)) // Light text for dark background
        }
        linearLayout.addView(stateTextView)

        val probCircle = View(context).apply {
            val size = 72.dpToPx(context)
            layoutParams = LinearLayout.LayoutParams(size, size).apply {
                setMargins(0, 10.dpToPx(context), 0, 0)
            }
            background = ContextCompat.getDrawable(context, R.drawable.probability_circle_bg) // Use dark version

            val scale = if (probability > 0.005) probability.coerceAtLeast(0.2f) else 0.0f
            scaleX = scale
            scaleY = scale

            alpha = (probability + 0.15f).coerceAtMost(1.0f)
        }
        linearLayout.addView(probCircle)

        val probTextView = TextView(context).apply {
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                topMargin = 6.dpToPx(context)
            }
            text = "${(probability * 100).format(1)}%"
            textSize = 14f
            setTextColor(ContextCompat.getColor(context, R.color.black)) // Medium light text for dark background
        }
        linearLayout.addView(probTextView)

        return linearLayout
    }
}

// Extension function for formatting floats
fun Float.format(digits: Int) = "%.${digits}f".format(this)

// Extension function for dp to px conversion
fun Int.dpToPx(context: Context): Int {
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        this.toFloat(),
        context.resources.displayMetrics
    ).toInt()
}

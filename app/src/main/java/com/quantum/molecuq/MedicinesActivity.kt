// MedicinesActivity.kt (Updated with CardView in RecyclerView Items)
package com.quantum.molecuq

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.cardview.widget.CardView
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.quantum.molecuq.R
import java.io.Serializable

class MedicinesActivity : AppCompatActivity() {

    data class Medication(
        val name: String,
        val chemicalComposition: String,
        val combination: String,
        val uses: String,
        val sideEffects: String,
        val manufacturer: String,
        val approvalDate: String
    ) : Serializable

    val medications = listOf(
        Medication("Paracetamol", "C8H9NO2", "None", "Pain relief, fever reduction", "Nausea, rash", "ABC Pharma", "12 Jan 2020"),
        Medication("Ibuprofen", "C13H18O2", "Ibuprofen + Paracetamol", "Anti-inflammatory", "Dizziness", "XYZ Pharma", "8 Mar 2021"),
        Medication("Amoxicillin", "C16H19N3O5S", "Amoxicillin + Clavulanic Acid", "Bacterial infections", "Allergy", "MediCure", "5 Jun 2022"),
        Medication("Aspirin", "C9H8O4", "None", "Pain relief, antiplatelet", "Stomach upset, bleeding", "HealthCorp", "15 Feb 2019"),
        Medication("Metformin", "C4H11N5", "None", "Type 2 diabetes", "Diarrhea, nausea", "GlucoPharm", "22 Apr 2020"),
        Medication("Losartan", "C22H23ClN6O", "None", "Hypertension", "Fatigue, cough", "CardioMed", "10 Jul 2021"),
        Medication("Omeprazole", "C17H19N3O3S", "None", "Acid reflux, ulcers", "Headache, abdominal pain", "GastroCare", "3 Sep 2018"),
        Medication("Atorvastatin", "C33H35FN2O5", "None", "High cholesterol", "Muscle pain", "LipidLab", "19 Nov 2020"),
        Medication("Ciprofloxacin", "C17H18FN3O3", "None", "Bacterial infections", "Tendonitis, nausea", "AntiBio", "25 Dec 2021"),
        Medication("Sertraline", "C17H17Cl2N", "None", "Depression, anxiety", "Insomnia, fatigue", "NeuroPharm", "7 Feb 2022"),
        Medication("Levothyroxine", "C15H11I4NO4", "None", "Hypothyroidism", "Weight loss, tremors", "ThyroMed", "14 May 2020"),
        Medication("Lisinopril", "C21H31N3O5", "None", "Hypertension, heart failure", "Cough, dizziness", "CardioHealth", "30 Jun 2021"),
        Medication("Prednisone", "C21H26O5", "None", "Inflammation, autoimmune disorders", "Weight gain, insomnia", "ImmunoPharm", "12 Oct 2019"),
        Medication("Azithromycin", "C38H72N2O12", "None", "Bacterial infections", "Diarrhea, nausea", "BioCure", "8 Aug 2022"),
        Medication("Fluoxetine", "C17H18F3NO", "None", "Depression, OCD", "Nausea, headache", "MentalHealthCorp", "17 Mar 2020"),
        Medication("Hydrochlorothiazide", "C7H8ClN3O4S2", "None", "Hypertension, edema", "Low potassium, dizziness", "AquaPharm", "4 Jan 2021"),
        Medication("Gabapentin", "C9H17NO2", "None", "Neuropathic pain, seizures", "Drowsiness, dizziness", "NeuroCare", "22 Sep 2022"),
        Medication("Tramadol", "C16H25NO2", "None", "Moderate to severe pain", "Nausea, constipation", "PainReliefInc", "11 Apr 2020"),
        Medication("Citalopram", "C20H21FN2O", "None", "Depression", "Dry mouth, sweating", "PsychePharm", "28 Feb 2021"),
        Medication("Doxycycline", "C22H24N2O8", "None", "Bacterial infections, acne", "Photosensitivity, nausea", "DermaBio", "16 Jul 2022"),
        Medication("Clonazepam", "C15H10ClN3O3", "None", "Seizures, anxiety", "Drowsiness, confusion", "NeuroMed", "9 May 2023"),
        Medication("Warfarin", "C19H16O4", "None", "Blood clot prevention", "Bleeding risk", "HemoPharm", "14 Oct 2020"),
        Medication("Simvastatin", "C25H38O5", "None", "High cholesterol", "Muscle pain, liver issues", "LipidCare", "3 Dec 2021"),
        Medication("Pantoprazole", "C16H15F2N3O4S", "None", "Acid reflux, ulcers", "Headache, diarrhea", "GastroMed", "27 Jun 2020"),
        Medication("Albuterol", "C13H21NO3", "None", "Asthma, COPD", "Tremors, palpitations", "RespiraPharm", "18 Jan 2022"),
        Medication("Amlodipine", "C20H25ClN2O5", "None", "Hypertension, angina", "Swelling, fatigue", "CardioPlus", "5 Mar 2020"),
        Medication("Escitalopram", "C20H21FN2O", "None", "Depression, anxiety", "Nausea, insomnia", "MindCare", "12 Aug 2021"),
        Medication("Clarithromycin", "C38H69NO13", "None", "Bacterial infections", "Taste disturbance, nausea", "BioSafe", "20 Feb 2022"),
        Medication("Loratadine", "C22H23ClN2O2", "None", "Allergies", "Drowsiness, headache", "AllergyRelief", "7 Nov 2019"),
        Medication("Metoprolol", "C15H25NO3", "None", "Hypertension, heart failure", "Fatigue, slow heart rate", "HeartMed", "15 Apr 2021"),
        Medication("Rosuvastatin", "C22H28FN3O6S", "None", "High cholesterol", "Muscle pain, weakness", "CholestCare", "29 Oct 2020"),
        Medication("Furosemide", "C12H11ClN2O5S", "None", "Edema, hypertension", "Dehydration, low potassium", "DiureticPharm", "3 Jun 2021"),
        Medication("Venlafaxine", "C17H27NO2", "None", "Depression, anxiety", "Nausea, sweating", "NeuroHealth", "17 Dec 2020"),
        Medication("Montelukast", "C35H36ClNO3S", "None", "Asthma, allergies", "Headache, abdominal pain", "RespiraCare", "25 Jul 2022"),
        Medication("Tamsulosin", "C20H28N2O5S", "None", "Benign prostatic hyperplasia", "Dizziness, low blood pressure", "UroMed", "10 Jan 2020"),
        Medication("Cetirizine", "C21H25ClN2O3", "None", "Allergies", "Drowsiness, dry mouth", "AllergyPharm", "8 Sep 2019"),
        Medication("Esomeprazole", "C17H19N3O3S", "None", "Acid reflux, ulcers", "Nausea, headache", "GastroHealth", "14 Mar 2021"),
        Medication("Bupropion", "C13H18ClNO", "None", "Depression, smoking cessation", "Dry mouth, insomnia", "PsycheMed", "22 Jun 2020"),
        Medication("Ranitidine", "C13H22N4O3S", "None", "Acid reflux, ulcers", "Headache, constipation", "GastroRelief", "5 Dec 2019"),
        Medication("Atenolol", "C14H22N2O3", "None", "Hypertension, angina", "Fatigue, cold hands", "CardioCare", "19 Aug 2021"),
        Medication("Fluconazole", "C13H12F2N6O", "None", "Fungal infections", "Nausea, liver issues", "AntiFungal", "11 Feb 2022"),
        Medication("Duloxetine", "C18H19NOS", "None", "Depression, neuropathic pain", "Nausea, dry mouth", "NeuroPharma", "30 Apr 2020"),
        Medication("Trazodone", "C19H22ClN5O", "None", "Depression, insomnia", "Drowsiness, dizziness", "SleepAid", "7 Oct 2021"),
        Medication("Allopurinol", "C5H4N4O", "None", "Gout, kidney stones", "Rash, nausea", "UricCare", "16 May 2020"),
        Medication("Carvedilol", "C24H26N2O4", "None", "Hypertension, heart failure", "Dizziness, fatigue", "HeartHealth", "23 Nov 2021"),
        Medication("Levofloxacin", "C18H20FN3O4", "None", "Bacterial infections", "Tendon rupture, nausea", "BioShield", "9 Jul 2022"),
        Medication("Mirtazapine", "C17H19N3", "None", "Depression", "Weight gain, drowsiness", "MindRelief", "12 Jan 2021"),
        Medication("Spironolactone", "C24H32O4S", "None", "Edema, hypertension", "High potassium, gynecomastia", "DiureticMed", "4 Apr 2020"),
        Medication("Zolpidem", "C19H21N3O", "None", "Insomnia", "Drowsiness, headache", "SleepPharm", "28 Feb 2022"),
        Medication("Clopidogrel", "C16H16ClNO2S", "None", "Antiplatelet, stroke prevention", "Bleeding, rash", "HemoCare", "15 Sep 2020"),
        Medication("Lansoprazole", "C16H14F3N3O2S", "None", "Acid reflux, ulcers", "Diarrhea, headache", "GastroPharm", "6 Dec 2021"),
        Medication("Bisoprolol", "C18H31NO4", "None", "Hypertension, heart failure", "Fatigue, slow heart rate", "CardioShield", "22 Jul 2020"),
        Medication("Valacyclovir", "C13H20N6O4", "None", "Viral infections", "Nausea, headache", "AntiViral", "13 Mar 2022"),
        Medication("Naproxen", "C14H14O3", "None", "Pain, inflammation", "Stomach upset, dizziness", "PainCare", "10 Nov 2019"),
        Medication("Pregabalin", "C8H17NO2", "None", "Neuropathic pain, seizures", "Dizziness, weight gain", "NeuroRelief", "25 Apr 2021"),
        Medication("Ramipril", "C23H32N2O5", "None", "Hypertension, heart failure", "Cough, dizziness", "CardioPlus", "8 Aug 2020"),
        Medication("Risperidone", "C23H27FN4O2", "None", "Schizophrenia, bipolar disorder", "Weight gain, drowsiness", "PsycheCare", "17 Feb 2022"),
        Medication("Fexofenadine", "C32H39NO4", "None", "Allergies", "Headache, drowsiness", "AllergyMed", "3 Jun 2020"),
        Medication("Enalapril", "C20H28N2O5", "None", "Hypertension, heart failure", "Cough, low blood pressure", "HeartPharm", "29 Oct 2021"),
        Medication("Methotrexate", "C20H22N8O5", "None", "Rheumatoid arthritis, cancer", "Nausea, liver issues", "ImmunoCare", "12 May 2020"),
        Medication("Oxycodone", "C18H21NO4", "None", "Severe pain", "Constipation, addiction risk", "PainMed", "7 Jan 2022"),
        Medication("Sildenafil", "C22H30N6O4S", "None", "Erectile dysfunction", "Headache, flushing", "VitaPharm", "19 Sep 2020"),
        Medication("Propranolol", "C16H21NO2", "None", "Hypertension, anxiety", "Fatigue, slow heart rate", "CardioRelief", "4 Mar 2021"),
        Medication("Cyclobenzaprine", "C20H21N", "None", "Muscle spasms", "Drowsiness, dry mouth", "MuscleCare", "16 Aug 2022"),
        Medication("Lamotrigine", "C9H7Cl2N5", "None", "Seizures, bipolar disorder", "Rash, dizziness", "NeuroShield", "23 Apr 2020"),
        Medication("Pravastatin", "C23H36O7", "None", "High cholesterol", "Muscle pain, liver issues", "LipidHealth", "11 Nov 2021"),
        Medication("Glyburide", "C23H28ClN3O5S", "None", "Type 2 diabetes", "Low blood sugar, nausea", "GlucoCare", "5 Jun 2020"),
        Medication("Celecoxib", "C17H14F3N3O2S", "None", "Arthritis, pain", "Stomach upset, swelling", "PainRelief", "28 Feb 2022"),
        Medication("Acyclovir", "C8H11N5O3", "None", "Viral infections", "Nausea, headache", "ViroMed", "14 Jul 2020"),
        Medication("Hydroxyzine", "C21H27ClN2O2", "None", "Anxiety, allergies", "Drowsiness, dry mouth", "AllergyCare", "9 Dec 2021"),
        Medication("Meloxicam", "C14H13N3O4S2", "None", "Arthritis, pain", "Stomach upset, dizziness", "JointHealth", "3 May 2020"),
        Medication("Topiramate", "C12H21NO8S", "None", "Seizures, migraines", "Weight loss, confusion", "NeuroMed", "18 Oct 2022"),
        Medication("Nifedipine", "C17H18N2O6", "None", "Hypertension, angina", "Swelling, headache", "CardioPharm", "27 Jun 2021")
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (intent.hasExtra("medication")) {
            showDetail(intent.getSerializableExtra("medication") as Medication)
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
                    text = "Medications"
                    textSize = 20f
                    setTextColor(0xFF000000.toInt())
                    setPadding(16, 0, 0, 0)
                }
                addView(back, LinearLayout.LayoutParams(80, 80))
                addView(title)
            }

            val searchView = SearchView(context).apply {
                id = R.id.searchView
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                ).apply { setMargins(0, 16, 0, 16) }
            }

            val recyclerView = RecyclerView(context).apply {
                id = R.id.recyclerViewMedications
                layoutManager = LinearLayoutManager(context)
            }

            addView(topBar)
            addView(searchView)
            addView(recyclerView)

            val adapter = MedicationAdapter(medications) { medication ->
                val intent = Intent(context, MedicinesActivity::class.java)
                intent.putExtra("medication", medication)
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

    private fun showDetail(med: Medication) {
        setContentView(ScrollView(this).apply {
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
                        text = med.name
                        textSize = 22f
                        setTextColor(0xFF000000.toInt())
                        setPadding(12, 0, 0, 0)
                    }
                    addView(back, LinearLayout.LayoutParams(80, 80))
                    addView(title)
                }

                val cardView = CardView(context).apply {
                    radius = 20f
                    cardElevation = 10f
                    useCompatPadding = true
                    setContentPadding(24, 24, 24, 24)

                    addView(LinearLayout(context).apply {
                        orientation = LinearLayout.VERTICAL

                        fun detail(text: String): TextView = TextView(context).apply {
                            setText(text)
                            textSize = 16f
                            setTextColor(0xFF333333.toInt())
                            setPadding(0, 12, 0, 12)
                        }

                        addView(detail("Formula: ${med.chemicalComposition}"))
                        addView(detail("Combinations: ${med.combination}"))
                        addView(detail("Uses: ${med.uses}"))
                        addView(detail("Side Effects: ${med.sideEffects}"))
                        addView(detail("Manufacturer: ${med.manufacturer}"))
                        addView(detail("Approval Date: ${med.approvalDate}"))
                    })
                }

                addView(topBar)
                addView(cardView)
            })
        })
    }

    class MedicationAdapter(
        private val fullList: List<Medication>,
        private val onItemClick: (Medication) -> Unit
    ) : RecyclerView.Adapter<MedicationAdapter.MedViewHolder>() {

        private var filteredList = fullList.toMutableList()

        inner class MedViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val nameText: TextView = view.findViewById(R.id.medicationName)
            val root: View = view
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MedViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_medication_card, parent, false)
            return MedViewHolder(view)
        }

        override fun onBindViewHolder(holder: MedViewHolder, position: Int) {
            val med = filteredList[position]
            holder.nameText.text = med.name
            holder.root.setOnClickListener { onItemClick(med) }
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

package com.example.serenemind.activities

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.serenemind.R
import com.example.serenemind.constants.Constants
import com.example.serenemind.databinding.ActivityResultBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class ResultActivity : AppCompatActivity() {

    private var binding: ActivityResultBinding? = null
    private var testName = ""
    private var testScore = 0
    private val database: FirebaseDatabase = FirebaseDatabase.getInstance()
    private val user = FirebaseAuth.getInstance().currentUser
    private val userId = user?.uid
    private val userRef: DatabaseReference = database.getReference("users")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        val receivedIntent = intent
        testName = receivedIntent.getStringExtra("TEST_NAME").toString()
        testScore = receivedIntent.getIntExtra("SCORE", 0)

        setupToolbar()

        if(testName == "PHQ-9") {
            showResultForPHQ()
        }else if(testName == "GAD-7") {
            showResultForGAD()
        }

        binding?.btnFinish?.setOnClickListener {
            finish()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun showResultForPHQ() {
        val assessmentText: String
        val textColor: Int
        val score = testScore

        when (score) {
            in 0..4 -> {
                assessmentText = "Minimal depression"
                textColor = getColor(R.color.very_light_green)
            }
            in 5..9 -> {
                assessmentText = "Mild depression"
                textColor = getColor(R.color.darker_green)
            }
            in 10..14 -> {
                assessmentText = "Moderate depression"
                textColor = getColor(R.color.orange)
            }
            in 15..19 -> {
                assessmentText = "Moderately severe depression"
                textColor = getColor(R.color.red)
            }
            in 20..27 -> {
                assessmentText = "Severe depression"
                textColor = getColor(R.color.dark_red)
            }
            else -> {
                assessmentText = "Unable to assess"
                textColor = getColor(R.color.default_text_color)
            }
        }

        binding?.tvAssessmentResult?.setTextColor(textColor)
        binding?.tvAssessmentResult?.text = assessmentText
        binding?.tvScore?.text = "Score: $score"
        binding?.tvName?.text = "Hello ${Constants.DISPLAY_NAME}!"

        if(!Constants.IS_GUEST) saveAssessmentDataToFirebase("PHQ-9", score)
    }

    @SuppressLint("SetTextI18n")
    private fun showResultForGAD() {
        val assessmentText: String
        val textColor: Int
        val score = testScore

        when (score) {
            in 0..4 -> {
                assessmentText = "Minimal anxiety"
                textColor = getColor(R.color.very_light_green)
            }
            in 5..9 -> {
                assessmentText = "Mild anxiety"
                textColor = getColor(R.color.darker_green)
            }
            in 10..14 -> {
                assessmentText = "Moderate anxiety"
                textColor = getColor(R.color.orange)
            }
            in 15..21 -> {
                assessmentText = "Moderately severe anxiety"
                textColor = getColor(R.color.red)
            }
            else -> {
                assessmentText = "Severe anxiety"
                textColor = getColor(R.color.dark_red)
            }
        }

        binding?.tvAssessmentResult?.setTextColor(textColor)
        binding?.tvAssessmentResult?.text = assessmentText
        binding?.tvScore?.text = "Score: $score"
        binding?.tvName?.text = "Hello ${Constants.DISPLAY_NAME}!"

        if(!Constants.IS_GUEST) saveAssessmentDataToFirebase("GAD-7", score)
    }


    private fun setupToolbar() {
        setSupportActionBar(binding?.tbResult)
        supportActionBar?.title = "Result $testName"
        binding?.tbResult?.setTitleTextColor(resources.getColor(android.R.color.black, theme))
    }

    override fun onDestroy() {
        binding = null
        super.onDestroy()
    }

    private fun saveAssessmentDataToFirebase(testName: String, score: Int) {
        userId?.let {
            val currentDate = Calendar.getInstance().time // Get current date and time
            val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
            val formattedDate = dateFormat.format(currentDate)

            val assessmentData = HashMap<String, Any>()
            assessmentData["testName"] = testName
            assessmentData["score"] = score
            assessmentData["date"] = formattedDate // Add the formatted date to assessment data

            val userAssessmentRef = FirebaseDatabase.getInstance().getReference("users").child(it).child("assessments")
            val newAssessmentRef = userAssessmentRef.push()
            newAssessmentRef.setValue(assessmentData)
                .addOnSuccessListener {
                    Toast.makeText(this@ResultActivity, "Assessment data saved.", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener { e ->
                    Toast.makeText(this@ResultActivity, "Failed to save data: $e", Toast.LENGTH_SHORT).show()
                    Log.e("FirebaseException", e.toString())
                }
        }
    }
}
package com.example.serenemind.activities

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.serenemind.R
import com.example.serenemind.classes.AssessmentData
import com.example.serenemind.databinding.ActivityAiBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import org.json.JSONObject
import java.io.IOException

class AIActivity : AppCompatActivity() {
    private var binding: ActivityAiBinding? = null
    private lateinit var assessmentList: ArrayList<AssessmentData>
    private lateinit var user: FirebaseUser
    private var prompt = "Below are the test details, including scores and dates for 5 tests. Could you provide an evaluation or analysis of these tests? Keep the response to the point."
    private var resultsFetched = false
    private val client = OkHttpClient()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAiBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        assessmentList = ArrayList()
        user = FirebaseAuth.getInstance().currentUser!!

        setupToolbar()
        retrieveLastFiveAssessments(user.uid)

        binding?.ivAiAnalysis?.setOnClickListener {
            if (!resultsFetched) {
                Toast.makeText(
                    this@AIActivity,
                    "Please wait while the results are being fetched...",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                getResponse(prompt) { response ->
                    runOnUiThread {
                        binding?.tvResponse?.text = response
                    }
                }
            }
        }
    }

    private fun retrieveLastFiveAssessments(userId: String) {
        val userAssessmentRef = FirebaseDatabase.getInstance().getReference("users").child(userId)
            .child("assessments")
            .orderByChild("date")
            .limitToLast(5)

        userAssessmentRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                assessmentList.clear()
                for (assessmentSnapshot in snapshot.children) {
                    val assessment = assessmentSnapshot.getValue(AssessmentData::class.java)
                    assessment?.let {
                        assessmentList.add(it)
                    }
                }

                if (assessmentList.size < 5) {
                    Toast.makeText(this@AIActivity, "Not enough assessments found.", Toast.LENGTH_SHORT).show()
                    return
                }

                // Prepare the prompt with the last 5 assessments
                val tests = StringBuilder()
                for (test in assessmentList) {
                    tests.append("${test.testName} Score: ${test.score} Date: ${test.date}\n")
                }
                prompt += "\n" + tests.toString()
                Log.e("Prompt", prompt)

                resultsFetched = true
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@AIActivity, "$error", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun getResponse(question: String, callback: (String) -> Unit) {
        val apiKey = "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"
        val url = "https://api.openai.com/v1/chat/completions"

        val requestBody = """
        {
            "model": "gpt-3.5-turbo-instruct",
            "messages": [{"role": "user", "content": "$question"}],
            "max_tokens": 100,
            "temperature": 0.7
        }
    """.trimIndent()

        val request = Request.Builder()
            .url(url)
            .addHeader("Content-Type", "application/json")
            .addHeader("Authorization", "Bearer $apiKey")
            .post(requestBody.toRequestBody("application/json".toMediaTypeOrNull()))
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e("error", "API failed", e)
                callback(e.message.toString())
            }

            override fun onResponse(call: Call, response: Response) {
                val body = response.body?.string()
                if (body != null) {
                    Log.v("data", body)
                    try {
                        val jsonObject = JSONObject(body)
                        val choicesArray = jsonObject.getJSONArray("choices")
                        if (choicesArray.length() > 0) {
                            // Get the first choice and the message content
                            val textResult = choicesArray.getJSONObject(0).getString("text")
                            callback(textResult)
                        } else {
                            callback("No choices available in the response.")
                        }
                    } catch (e: Exception) {
                        Log.e("error", "JSON parsing error", e)
                        callback("Error parsing response: ${e.message}")
                    }
                } else {
                    callback("Empty response body")
                }
            }


        })
    }


    private fun setupToolbar() {
        setSupportActionBar(binding?.tbAi)
        supportActionBar?.title = "AI Analysis"
        binding?.tbAi?.setTitleTextColor(resources.getColor(android.R.color.black, theme))
    }

    override fun onDestroy() {
        binding = null
        super.onDestroy()
    }
}

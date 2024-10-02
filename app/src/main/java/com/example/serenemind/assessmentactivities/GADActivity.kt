package com.example.serenemind.assessmentactivities

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.serenemind.activities.ResultActivity
import com.example.serenemind.classes.TestFormat
import com.example.serenemind.databinding.ActivityGadBinding

class GADActivity : AppCompatActivity() {

    private var binding: ActivityGadBinding? = null
    private var testQuestions = ArrayList<TestFormat>()
    private var ptr = 0
    private var selectedOption = -1
    private var result = arrayListOf<Int>(0, 0, 0, 0, 0, 0, 0)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGadBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        setupToolbar()

        backFunctionality()
        forwardFunctionality()

        addQuestions()
        setupQuestion()

        optionsOnClick()

    }

    private fun optionsOnClick() {
        binding?.option1?.setOnClickListener {
            selectedOption = 0
        }
        binding?.option2?.setOnClickListener {
            selectedOption = 1
        }
        binding?.option3?.setOnClickListener {
            selectedOption = 2
        }
        binding?.option4?.setOnClickListener {
            selectedOption = 3
        }
    }

    private fun forwardFunctionality() {
        binding?.btnForward?.setOnClickListener {
            if(ptr >= testQuestions.size) {
                result[ptr - 1] = selectedOption
                val score = result.sum()

                val intent = Intent(this@GADActivity, ResultActivity::class.java)
                intent.putExtra("TEST_NAME", "GAD-7")
                intent.putExtra("SCORE", score)

                startActivity(intent)
                finish()

                return@setOnClickListener
            }
            else if(selectedOption == -1) {
                Toast.makeText(
                    this@GADActivity,
                    "You have not selected an option.",
                    Toast.LENGTH_LONG
                ).show()
                return@setOnClickListener
            }
            result[ptr - 1] = selectedOption
            setupQuestion()
            supportActionBar?.title = "GAD-7 Question $ptr"
        }
    }

    private fun backFunctionality() {
        binding?.btnBack?.setOnClickListener {
            if(ptr <= 1) {
                Toast.makeText(
                    this@GADActivity,
                    "You are at the first question of the test.",
                    Toast.LENGTH_LONG
                ).show()
            } else {
                ptr -= 2
                setupQuestion()
                supportActionBar?.title = "GAD-7 Question $ptr"
            }
        }
    }

    private fun setupQuestion() {
        if(ptr >= testQuestions.size)   return

        binding?.tvQuestion?.text = testQuestions[ptr].question
        binding?.option1?.text = testQuestions[ptr].options[0]
        binding?.option2?.text = testQuestions[ptr].options[1]
        binding?.option3?.text = testQuestions[ptr].options[2]
        binding?.option4?.text = testQuestions[ptr].options[3]

        binding?.rgOptions?.clearCheck()

        selectedOption = -1

        ptr++
    }

    private fun addQuestions() {
        val question1 = TestFormat(
            "Over the last 2 weeks, how often have you been bothered by any of the following problems?\n" +
                    "1. Feeling nervous, anxious or on edge",
            arrayListOf(
                "Not at all",
                "Several days",
                "More than half the days",
                "Nearly every day"
            ),
            -1 // Correct answer not applicable for this question
        )

        val question2 = TestFormat(
            "2. Not being able to stop or control worrying",
            arrayListOf(
                "Not at all",
                "Several days",
                "More than half the days",
                "Nearly every day"
            ),
            -1 // Correct answer not applicable for this question
        )

        val question3 = TestFormat(
            "3. Worrying too much about different things",
            arrayListOf(
                "Not at all",
                "Several days",
                "More than half the days",
                "Nearly every day"
            ),
            -1 // Correct answer not applicable for this question
        )

        val question4 = TestFormat(
            "4. Trouble relaxing",
            arrayListOf(
                "Not at all",
                "Several days",
                "More than half the days",
                "Nearly every day"
            ),
            -1 // Correct answer not applicable for this question
        )

        val question5 = TestFormat(
            "5. Being so restless that it's hard to sit still",
            arrayListOf(
                "Not at all",
                "Several days",
                "More than half the days",
                "Nearly every day"
            ),
            -1 // Correct answer not applicable for this question
        )

        val question6 = TestFormat(
            "6. Becoming easily annoyed or irritable",
            arrayListOf(
                "Not at all",
                "Several days",
                "More than half the days",
                "Nearly every day"
            ),
            -1 // Correct answer not applicable for this question
        )

        val question7 = TestFormat(
            "7. Feeling afraid as if something awful might happen",
            arrayListOf(
                "Not at all",
                "Several days",
                "More than half the days",
                "Nearly every day"
            ),
            -1 // Correct answer not applicable for this question
        )

        // Add the questions to the ArrayList
        testQuestions.add(question1)
        testQuestions.add(question2)
        testQuestions.add(question3)
        testQuestions.add(question4)
        testQuestions.add(question5)
        testQuestions.add(question6)
        testQuestions.add(question7)
    }

    private fun setupToolbar() {
        setSupportActionBar(binding?.tbTest)
        supportActionBar?.title = "GAD-7 Question 1"
        binding?.tbTest?.setTitleTextColor(resources.getColor(android.R.color.black, theme))
    }

    override fun onDestroy() {
        binding = null
        super.onDestroy()
    }
}

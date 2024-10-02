package com.example.serenemind.assessmentactivities

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.serenemind.activities.ResultActivity
import com.example.serenemind.classes.TestFormat
import com.example.serenemind.databinding.ActivityPhqBinding

class PHQActivity : AppCompatActivity() {

    private var binding: ActivityPhqBinding? = null
    private var testQuestions = ArrayList<TestFormat>()
    private var ptr = 0
    private var selectedOption = -1
    private var result = arrayListOf<Int>(0, 0, 0, 0, 0, 0, 0, 0, 0)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPhqBinding.inflate(layoutInflater)
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
                val score = result.sum()

                val intent = Intent(this@PHQActivity, ResultActivity::class.java)
                intent.putExtra("TEST_NAME", "PHQ-9")
                intent.putExtra("SCORE", score)

                startActivity(intent)
                finish()

                return@setOnClickListener
            }
            else if(selectedOption == -1) {
                Toast.makeText(
                    this@PHQActivity,
                    "You have not selected an option.",
                    Toast.LENGTH_LONG
                ).show()
                return@setOnClickListener
            }
            result[ptr - 1] = selectedOption
            setupQuestion()
            supportActionBar?.title = "PHQ-9 Question $ptr"
        }
    }

    private fun backFunctionality() {
        binding?.btnBack?.setOnClickListener {
            if(ptr <= 1) {
                Toast.makeText(
                    this@PHQActivity,
                    "You are at the first question of the test.",
                    Toast.LENGTH_LONG
                ).show()
            }else {
                ptr -= 2
                setupQuestion()
                supportActionBar?.title = "PHQ-9 Question $ptr"
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

        ptr ++
    }

    private fun addQuestions() {
        val question1 = TestFormat(
            "Over the last 2 weeks, how often have you been bothered by any of the following problems?\n" +
                    "1. Little interest or pleasure in doing things",
            arrayListOf(
                "Not at all",
                "Several days",
                "More than half the days",
                "Nearly every day"
            ),
            -1 // Correct answer not applicable for this question
        )

        val question2 = TestFormat(
            "2. Feeling down, depressed, or hopeless",
            arrayListOf(
                "Not at all",
                "Several days",
                "More than half the days",
                "Nearly every day"
            ),
            -1 // Correct answer not applicable for this question
        )

        val question3 = TestFormat(
            "3. Trouble falling or staying asleep, or sleeping too much",
            arrayListOf(
                "Not at all",
                "Several days",
                "More than half the days",
                "Nearly every day"
            ),
            -1 // Correct answer not applicable for this question
        )

        val question4 = TestFormat(
            "4. Feeling tired or having little energy",
            arrayListOf(
                "Not at all",
                "Several days",
                "More than half the days",
                "Nearly every day"
            ),
            -1 // Correct answer not applicable for this question
        )

        val question5 = TestFormat(
            "5. Poor appetite or overeating",
            arrayListOf(
                "Not at all",
                "Several days",
                "More than half the days",
                "Nearly every day"
            ),
            -1 // Correct answer not applicable for this question
        )

        val question6 = TestFormat(
            "6. Feeling bad about yourself or that you are a failure or have let yourself or your family down",
            arrayListOf(
                "Not at all",
                "Several days",
                "More than half the days",
                "Nearly every day"
            ),
            -1 // Correct answer not applicable for this question
        )

        val question7 = TestFormat(
            "7. Trouble concentrating on things, such as reading the newspaper or watching television",
            arrayListOf(
                "Not at all",
                "Several days",
                "More than half the days",
                "Nearly every day"
            ),
            -1 // Correct answer not applicable for this question
        )

        val question8 = TestFormat(
            "8. Moving or speaking so slowly that other people could have noticed. " +
                    "Or the opposite being so fidgety or restless that you have been moving around a lot more than usual",
            arrayListOf(
                "Not at all",
                "Several days",
                "More than half the days",
                "Nearly every day"
            ),
            -1 // Correct answer not applicable for this question
        )

        val question9 = TestFormat(
            "9. Thoughts that you would be better off dead, or of hurting yourself",
            arrayListOf(
                "Not at all",
                "Several days",
                "More than half the days",
                "Nearly every day"
            ),
            -1 // Correct answer not applicable for this question
        )

        val question10 = TestFormat(
            "If you checked off any problems, how difficult have these problems made it for you to do your work, " +
                    "take care of things at home, or get along with other people?",
            arrayListOf(
                "Not difficult at all",
                "Somewhat difficult",
                "Very difficult",
                "Extremely difficult"
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
        testQuestions.add(question8)
        testQuestions.add(question9)
        testQuestions.add(question10)
    }

    private fun setupToolbar() {
        setSupportActionBar(binding?.tbTest)
        supportActionBar?.title = "PHQ-9 Question 1"
        binding?.tbTest?.setTitleTextColor(resources.getColor(android.R.color.black, theme))
    }

    override fun onDestroy() {
        binding = null
        super.onDestroy()
    }
}
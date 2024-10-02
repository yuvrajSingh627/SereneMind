package com.example.serenemind.constants

import com.example.serenemind.classes.TestNames

object Constants {
    var EMAIL_ID = ""
    var DISPLAY_NAME = ""
    var IS_GUEST = true

    val testNames = arrayListOf<TestNames>(
        TestNames("PHQ-9", "Patient Health Questionnaire-9", "#F72585"),
        TestNames("GAD-7", "Generalized Anxiety Disorder-7", "#66CC99")
    )
}
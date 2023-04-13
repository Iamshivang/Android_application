package com.example.quiz

object Constants {

    const val USER_NAME: String= "user_name"
    const val TOTAL_QUESTIONS: String= "total_questions"
    const val CORRECT_ANSWER: String= "correct_answer"

    fun getQuestions(): ArrayList<Question>{
        val questionsList= ArrayList<Question>()

        val ques1= Question(
            1, "What country does this flag belong to?",
            R.drawable.argentina,
            "Argentina", "Australia", "America", "Austria",
            correctAnswer = 1

        )
        questionsList.add(ques1)

        val ques2= Question(
            2, "What country does this flag belong to?",
            R.drawable.australia,
            "Argentina", "Australia", "America", "Austria",
            correctAnswer = 2

        )
        questionsList.add(ques2)

        val ques3= Question(
            3, "What country does this flag belong to?",
            R.drawable.belgium,
            "Brazil", "Pakistan", "Belgium", "Austria",
            correctAnswer = 3

        )
        questionsList.add(ques3)

        val ques4= Question(
            4, "What country does this flag belong to?",
            R.drawable.brazil,
            "Brazil", "Pakistan", "Belgium","Austria",
            correctAnswer = 1

        )
        questionsList.add(ques4)

        val ques5= Question(
            5, "What country does this flag belong to?",
            R.drawable.denmark,
            "Sri lanka", "Bhutan", "America", "Denmark",
            correctAnswer = 4

        )
        questionsList.add(ques5)

        val ques6= Question(
            6, "What country does this flag belong to?",
            R.drawable.fiji,
            "Argentina", "Fiji", "Finland", "France",
            correctAnswer = 2

        )
        questionsList.add(ques6)

        val ques7= Question(
            7, "What country does this flag belong to?",
            R.drawable.france,
            "Finland", "Fiji", "America", "France",
            correctAnswer = 4

        )
        questionsList.add(ques7)

        val ques8= Question(
            8, "What country does this flag belong to?",
            R.drawable.germany,
            "Finland", "Ghana", "Germany", "Greece",
            correctAnswer = 3

        )
        questionsList.add(ques8)

        val ques9= Question(
            9, "What country does this flag belong to?",
            R.drawable.india,
            "Finland", "Ghana", "Germany", "India",
            correctAnswer = 4

        )
        questionsList.add(ques9)

        val ques10= Question(
            10, "What country does this flag belong to?",
            R.drawable.japan,
            "Jamaica", "Japan", "Jordan", "Italy",
            correctAnswer = 2

        )
        questionsList.add(ques10)

        return questionsList
    }
}
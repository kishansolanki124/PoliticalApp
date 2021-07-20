package com.politics.politicalapp.pojo

data class QuizAndContestRunningResponse(
    var message: String = "",
    var quiz_detail: List<QuizDetail> = listOf(),
    var quiz_question: List<QuizQuestion> = listOf(),
    var status: String = ""
) {
    data class QuizDetail(
        var correct_answer: String = "",
        var end_date: String = "",
        var id: String = "",
        var quiz_detail: String = "",
        var quiz_rules: String = "",
        var quiz_winner: String = "",
        var result_date: String = "",
        var sponser_img: String = "",
        var sponser_url: String = "",
        var start_date: String = "",
        var user_answer: String = ""
    )

    data class QuizQuestion(
        var question: String = "",
        var quiz_options: List<QuizOption> = listOf()
    ) {
        data class QuizOption(
            var option_id: String = "",
            var option_name: String = ""
        )
    }
}
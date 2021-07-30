package com.app.colorsofgujarat.pojo

data class QuizAndContestResponse(
    var message: String = "",
    var quiz_list: ArrayList<Quiz> = ArrayList(),
    var status: String = "",
    var total_records: Int = 0
) {
    data class Quiz(
        var id: String = "",
        var name: String = "",
        var quiz_mode: String = ""
    )
}
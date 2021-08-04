package com.app.colorsofgujarat.pojo

data class QuizAndContestDynamicResponse(
    var message: String = "",
    var photo_contest: List<PhotoContest> = listOf(),
    var status: String = ""
) {
    data class PhotoContest(
        var contest_detail: String = "",
        var user_participate: String ?= null,
        var contest_name: String = "",
        var contest_rules: String = "",
        var contest_winner: String = "",
        var id: String = ""
    )
}
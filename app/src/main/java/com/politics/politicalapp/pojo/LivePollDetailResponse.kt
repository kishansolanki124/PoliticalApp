package com.politics.politicalapp.pojo

data class LivePollDetailResponse(
    var message: String = "",
    var poll: List<Poll> = listOf(),
    var poll_result: List<PollResult> = listOf(),
    var status: String = ""
) {
    data class Poll(
        var poll_id: String = "",
        var poll_options: List<PollOption> = listOf(),
        var poll_question: String = "",
        var user_rating: String = ""
    ) {
        data class PollOption(
            var option_id: String = "",
            var option_name: String = ""
        )
    }

    data class PollResult(
        var option_id: String = "",
        var option_name: String = "",
        var percentage: String = ""
    )
}
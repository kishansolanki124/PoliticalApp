package com.politics.politicalapp.pojo

data class DistrictPollListResponse(
    var message: String = "",
    var rating_status: String = "",
    var poll_list: ArrayList<Poll> = ArrayList(),
    var poll_option: ArrayList<PollOption> = ArrayList(),
    var poll_result: List<PollResult> = listOf(),
    var status: String = "",
    var poll_result_text: String = ""
) {
    data class Poll(
        var id: String = "",
        var name: String = "",
        var checkedRadioId: String = "",
        var user_rating: String = ""
    )

    data class PollOption(
        var option_id: String = "",
        var option_name: String = ""
    )

    data class PollResult(
        var option_id: String = "",
        var option_name: String = "",
        var percentage: String = ""
    )
}
package com.politics.politicalapp.pojo

data class DistrictPollListResponse(
    var message: String = "",
    var poll_list: ArrayList<Poll> = ArrayList(),
    var poll_option: ArrayList<PollOption> = ArrayList(),
    var poll_result: List<PollResult> = listOf(),
    var status: String = ""
) {
    data class Poll(
        var id: String = "",
        var name: String = "",
        var checkedRadioId: String = ""
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
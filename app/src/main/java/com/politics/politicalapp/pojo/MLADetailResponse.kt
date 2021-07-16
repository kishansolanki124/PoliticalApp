package com.politics.politicalapp.pojo

data class MLADetailResponse(
    var gov_mla_detail: List<GovMlaDetail> = listOf(),
    var message: String = "",
    var poll: List<Poll> = listOf(),
    var poll_result: List<PollResult> = listOf(),
    var status: String = ""
) {
    data class GovMlaDetail(
        var city: String = "",
        var district_id: String = "",
        var id: String = "",
        var mla_name: String = "",
        var mla_profile: String = "",
        var mla_work: String = "",
        var percenrage: String = "",
        var political_party: String = "",
        var total_vote: String = "",
        var up_pro_img: String = "",
        var user_rating: String = ""
    )

    data class Poll(
        var poll_id: String = "",
        var poll_options: List<PollOption> = listOf(),
        var poll_question: String = ""
    ) {
        data class PollOption(
            var option_id: String = "",
            var option_name: String = ""
        )
    }

    data class PollResult(
        var option_id: String = "",
        var option_name: String = "",
        var percenrage: String = ""
    )
}
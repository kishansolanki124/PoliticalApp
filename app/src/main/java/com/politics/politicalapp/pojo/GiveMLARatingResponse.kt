package com.politics.politicalapp.pojo

data class GiveMLARatingResponse(
    var message: String = "",
    var poll_result: List<MLADetailResponse.PollResult> = listOf(),
    var status: String = "",
    var user_points: Int = 0
)
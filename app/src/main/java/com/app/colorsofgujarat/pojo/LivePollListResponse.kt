package com.app.colorsofgujarat.pojo

data class LivePollListResponse(
    var live_poll_list: ArrayList<LivePoll> = ArrayList(),
    var message: String = "",
    var status: String = "",
    var total_records: Int = 0
) {
    data class LivePoll(
        var id: String = "",
        var name: String = "",
        var votes: Int = 0,
        var poll_status: String = ""
    )
}
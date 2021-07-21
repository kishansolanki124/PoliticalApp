package com.politics.politicalapp.pojo

data class WinnerListResponse(
    var message: String = "",
    var points_prize_list: ArrayList<PointsPrize> = ArrayList(),
    var status: String = "",
    var total_records: Int = 0
) {
    data class PointsPrize(
        var id: String = "",
        var name1: String = "",
        var name2: String = "",
        var prize_mode: String = ""
    )
}
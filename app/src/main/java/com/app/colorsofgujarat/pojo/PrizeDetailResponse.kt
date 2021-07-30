package com.app.colorsofgujarat.pojo

data class PrizeDetailResponse(
    var message: String = "",
    var prize_detail: List<PrizeDetail> = listOf(),
    var status: String = ""
) {
    data class PrizeDetail(
        var id: String = "",
        var name1: String = "",
        var name2: String = "",
        var points_banner: String = "",
        var prize_detail: String = "",
        var prize_rules: String = "",
        var prize_winner: String = "",
        var winner_banner: String = ""
    )
}
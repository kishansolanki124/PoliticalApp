package com.app.colorsofgujarat.pojo

data class UserAdviseDetailResponse(
    var advice_detail: List<AdviceDetail> = listOf(),
    var message: String = "",
    var status: String = ""
) {
    data class AdviceDetail(
        var city: String = "",
        var description: String = "",
        var district: String = "",
        var id: String = "",
        var title: String = "",
        var up_pro_img: String = "",
        var user_name: String = ""
    )
}
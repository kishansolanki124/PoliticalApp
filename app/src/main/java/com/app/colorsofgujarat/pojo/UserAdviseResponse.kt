package com.app.colorsofgujarat.pojo

data class UserAdviseResponse(
    var message: String = "",
    var status: String = "",
    var total_records: Int = 0,
    var user_advice_list: ArrayList<UserAdvice> = ArrayList()
) {
    data class UserAdvice(
        var city: String = "",
        var district: String = "",
        var id: String = "",
        var title: String = "",
        var up_pro_img: String = "",
        var user_name: String = ""
    )
}
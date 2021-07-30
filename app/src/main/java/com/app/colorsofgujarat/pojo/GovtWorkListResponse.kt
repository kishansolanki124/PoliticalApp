package com.app.colorsofgujarat.pojo

data class GovtWorkListResponse(
    var gov_work_list: ArrayList<GovWork> = ArrayList(),
    var message: String = "",
    var status: String = "",
    var total_records: Int = 0
) {
    data class GovWork(
        var average_rating: String = "",
        var district_id: String = "",
        var id: String = "",
        var name: String = "",
        var up_pro_img: String = ""
    )
}
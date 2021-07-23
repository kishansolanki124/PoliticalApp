package com.politics.politicalapp.pojo

import java.io.Serializable

data class MLAListResponse(
    var gov_mla_list: ArrayList<GovMla> = ArrayList(),
    var message: String = "",
    var status: String = "",
    var total_records: Int = 0
) : Serializable {
    data class GovMla(
        var city: String = "",
        var district_id: String = "",
        var id: String = "",
        var mla_name: String = "",
        var percentage: String = "",
        var political_party: String = "",
        var up_pro_img: String = "",
        var votes: Int = 0
    ) : Serializable
}
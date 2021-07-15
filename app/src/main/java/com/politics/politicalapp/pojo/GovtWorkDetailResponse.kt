package com.politics.politicalapp.pojo

data class GovtWorkDetailResponse(
    var gov_work_detail: List<GovWorkDetail> = listOf(),
    var message: String = "",
    var status: String = "",
    var user_comment: List<UserComment> = listOf()
) {
    data class GovWorkDetail(
        var average_rating: String = "",
        var description: String = "",
        var id: String = "",
        var name: String = "",
        var up_pro_img: String = "",
        var user_rating: String = ""
    )

    data class UserComment(
        var comment: String = "",
        var user_name: String = ""
    )
}
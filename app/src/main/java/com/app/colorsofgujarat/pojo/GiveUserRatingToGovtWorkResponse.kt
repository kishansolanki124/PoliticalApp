package com.app.colorsofgujarat.pojo

data class GiveUserRatingToGovtWorkResponse(
    var message: String = "",
    var status: String = "",
    var comment_list: ArrayList<GovtWorkDetailResponse.UserComment> = ArrayList(),
    var user_points: Int = 0
)
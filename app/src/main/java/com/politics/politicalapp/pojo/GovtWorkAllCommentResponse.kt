package com.politics.politicalapp.pojo

data class GovtWorkAllCommentResponse(
    var message: String = "",
    var status: String = "",
    var total_records: Int = 0,
    var user_comment: ArrayList<GovtWorkDetailResponse.UserComment> = ArrayList()
)
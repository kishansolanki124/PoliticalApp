package com.politics.politicalapp.pojo

data class NewsDetailResponse(
    var message: String = "",
    var news_detail: List<NewsDetail> = listOf(),
    var status: String = ""
) {
    data class NewsDetail(
        var description: String = "",
        var id: String = "",
        var name: String = "",
        var up_pro_img: String = ""
    )
}
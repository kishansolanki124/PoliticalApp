package com.politics.politicalapp.pojo

data class NewsDetailResponse(
    var message: String = "",
    var news_detail: List<NewsDetail> = listOf(),
    var news_gallery: List<NewsGallery> = listOf(),
    var user_comment: ArrayList<GovtWorkDetailResponse.UserComment> = ArrayList(),
    var status: String = ""
) {
    data class NewsDetail(
        var description: String = "",
        var id: String = "",
        var name: String = "",
        var up_pro_img: String = ""
    )

    data class NewsGallery(
        var id: String = "",
        var nid: String = "",
        var up_pro_img: String = ""
    )
}
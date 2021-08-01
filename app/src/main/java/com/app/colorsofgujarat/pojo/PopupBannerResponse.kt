package com.app.colorsofgujarat.pojo

data class PopupBannerResponse(
    var delay_time: String = "",
    var initial_time: String = "",
    var message: String = "",
    var popup_banner: List<PopupBanner> = listOf(),
    var status: String = ""
) {
    data class PopupBanner(
        var id: String = "",
        var name: String = "",
        var up_pro_img: String = "",
        var url: String = ""
    )
}
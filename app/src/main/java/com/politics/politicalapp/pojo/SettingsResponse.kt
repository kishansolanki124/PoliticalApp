package com.politics.politicalapp.pojo

data class SettingsResponse(
    var district_list: List<District> = listOf(),
    var settings: List<Setting> = listOf(),
    var welcome_banner: List<WelcomeBanner> = listOf(),
    var news_list: List<News> = listOf(),
    var user_points: String = ""
) {
    data class District(
        var id: String = "",
        var name: String = ""
    ) {
        override fun toString(): String {
            return name
        }
    }

    data class Setting(
        var android_version: String = "",
        var appsharemsg: String = "",
        var ios_version: String = "",
        var isfourceupdate: String = "",
        var postsharemsg: String = "",
        var update_link: String = "",
        var updatemsg: String = ""
    )

    data class News(
        var id: String = "",
        var name: String = "",
        var pdate: String = "",
        var up_pro_img: String = ""
    )

    data class WelcomeBanner(
        var id: String = "",
        var up_pro_img: String = ""
    )
}
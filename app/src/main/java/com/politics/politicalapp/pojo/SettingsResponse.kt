package com.politics.politicalapp.pojo

data class SettingsResponse(
    var district_list: List<District> = listOf(),
    var settings: List<Setting> = listOf(),
    var welcome_banner: List<WelcomeBanner> = listOf(),
    var terms: List<Terms> = listOf(),
    var contest: List<Contest> = listOf(),
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
        var points_tooltip: String = "",
        var updatemsg: String = ""
    )

    data class Contest(
        var id: String = "",
        var menu_name: String = "",
        var menu_icon: String = ""
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
    data class Terms(
        var id: String = "",
        var name: String = "",
        var description: String = ""
    )
}
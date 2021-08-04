package com.app.colorsofgujarat.pojo

data class SettingsResponse(
    var district_list: List<District> = listOf(),
    var settings: List<Setting> = listOf(),
    var welcome_banner: List<WelcomeBanner> = listOf(),
    var terms: List<Terms> = listOf(),
    var contest: List<Contest> = listOf(),
    var news_list: List<News> = listOf(),
    var notification_count: String = "",
    var user_points: String = "",
    var user_name: String = "",
    var user_city: String = "",
    var user_district: String = ""
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
        var share_points: String = "",
        var poll_points: String = "",
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
        var url: String = "",
        var up_pro_img: String = ""
    )
    data class Terms(
        var id: String = "",
        var name: String = "",
        var description: String = ""
    )
}
package com.app.colorsofgujarat.pojo

data class StaticPageResponse(
    var message: String = "",
    var staticpage: List<Staticpage> = listOf(),
    var status: String = ""
) {
    data class Staticpage(
        var description: String = "",
        var name: String = ""
    )
}
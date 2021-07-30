package com.app.colorsofgujarat.pojo

data class ScratchCardResponse(
    var message: String = "",
    var status: String = "",
    var scratch_card: ArrayList<ScratchCard> = ArrayList()
) {
    data class ScratchCard(
        var max: String = "",
        var min: String = ""
    )
}
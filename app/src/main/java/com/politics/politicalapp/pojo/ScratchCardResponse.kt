package com.politics.politicalapp.pojo

data class ScratchCardResponse(
    var message: String = "",
    var scratch_card: ArrayList<ScratchCard> = ArrayList(),
    var status: String = ""
) {
    data class ScratchCard(
        var max: String = "",
        var min: String = ""
    )
}
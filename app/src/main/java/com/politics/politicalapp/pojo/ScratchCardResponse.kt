package com.politics.politicalapp.pojo

data class ScratchCardResponse(
    var message: String = "",
    var scratch_card: List<ScratchCard> = listOf(),
    var status: String = ""
) {
    data class ScratchCard(
        var max: String = "",
        var min: String = ""
    )
}
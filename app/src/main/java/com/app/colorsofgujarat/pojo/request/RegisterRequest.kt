package com.app.colorsofgujarat.pojo.request

data class RegisterRequest(
    val district_id: String,
    val city: String,
    val name: String,
    val mobile: String,
    val platform: String = "android"
)
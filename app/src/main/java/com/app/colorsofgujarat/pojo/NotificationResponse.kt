package com.app.colorsofgujarat.pojo

data class NotificationResponse(
    var message: String = "",
    var notification_list: ArrayList<Notification> = ArrayList(),
    var status: String = "",
    var total_records: Int = 0
) {
    data class Notification(
        var id: String = "",
        var pdate: String = "",
        var name: String = ""
    )
}
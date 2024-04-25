package com.fleaudie.chatapp.data.model

import java.util.Date

data class Message(
    val senderId: String = "",
    val receiverId: String = "",
    val message: String = "",
    val timestamp: Date = Date()
) {
    constructor() : this("", "", "")
}

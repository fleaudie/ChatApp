package com.fleaudie.chatapp.data.model

import java.util.Date

data class ChatInfo(
    val lastMessage: String = "",
    val partnerId: String = "",
    val profileImage: String = "",
    val partnerPhoneNumber: String = "",
    val partnerName : String = "",
    val lastTimestamp: Date = Date()
)

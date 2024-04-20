package com.fleaudie.chatapp.data.model

import java.io.Serializable

data class User(
    val name: String = "",
    val surname: String = "",
    val phoneNumber: String = "",
    val userId: String = ""
): Serializable

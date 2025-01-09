package com.codersee.model

import com.codersee.util.Item
import kotlinx.serialization.Serializable

@Serializable
data class User(
    val id: Int? = null,
    val username: String,
    val password: String,
    val itemList: List<Item> = emptyList()
)
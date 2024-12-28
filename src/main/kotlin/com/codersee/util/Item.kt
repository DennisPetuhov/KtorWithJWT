package com.codersee.util

import kotlinx.serialization.Serializable

@Serializable
data class Item(val id: Int? = null, val name: String)
package com.devkt.adminguninevigation.model

data class LocationItem(
    val id: Int,
    val uid: String,
    val imageUrl: String,
    val name: String,
    val date: String
)

data class GetSpecificLocationResponse(
    val message: List<LocationItem>,
    val status: Int
)
package com.devkt.adminguninevigation.model

data class LocationItems(
    val id: Int,
    val uid: String,
    val mainLocation: String,
    val imageUrl: String,
    val name: String,
    val description: String,
    val phoneNo: String,
    val longitude: String,
    val latitude: String,
    val date: String
)

data class GetSpecificSubLocationResponse(
    val message: List<LocationItems>,
    val status: Int
)
package com.devkt.adminguninevigation.screens.nav

import kotlinx.serialization.Serializable

class Routs {
    @Serializable
    object LoginScreen

    @Serializable
    object HomeScreen

    @Serializable
    object AddLocationScreen

    @Serializable
    object AddSubLocationScreen

    @Serializable
    object LocationPickerScreen

    @Serializable
    data class UpdateLocationCategoryScreen(val place: String)

    @Serializable
    object ShowLocationCategoryScreen

    @Serializable
    data class ShowAllLocationScreen(val category: String)

    @Serializable
    data class UpdateAllLocationScreen(val subLocationName: String)

    @Serializable
    object ShowAllLocationWithCategoryScreen

}
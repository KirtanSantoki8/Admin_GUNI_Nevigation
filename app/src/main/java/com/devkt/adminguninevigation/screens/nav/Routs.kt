package com.devkt.adminguninevigation.screens.nav

import kotlinx.serialization.Serializable

@Serializable
sealed class Routs {
    @Serializable
    data object LoginScreen : Routs()

    @Serializable
    data object HomeScreen : Routs()

    @Serializable
    data object AddLocationScreen : Routs()

    @Serializable
    data object AddSubLocationScreen : Routs()

    @Serializable
    data object LocationPickerScreen : Routs()

    @Serializable
    data class UpdateLocationCategoryScreen(val place: String) : Routs()

    @Serializable
    data object ShowLocationCategoryScreen : Routs()

    @Serializable
    data class ShowAllLocationScreen(val category: String) : Routs()

    @Serializable
    data class UpdateAllLocationScreen(val subLocationName: String) : Routs()

    @Serializable
    data object ShowAllLocationWithCategoryScreen : Routs()

    @Serializable
    data object SuggestionScreen : Routs()
}
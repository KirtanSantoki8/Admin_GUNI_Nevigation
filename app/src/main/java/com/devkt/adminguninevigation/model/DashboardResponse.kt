package com.devkt.adminguninevigation.model

data class DashboardResponse(
    val category_wise_sub_location_data: List<Any>,
    val location_count: Int,
    val status: Int,
    val sub_location_count: Int
)
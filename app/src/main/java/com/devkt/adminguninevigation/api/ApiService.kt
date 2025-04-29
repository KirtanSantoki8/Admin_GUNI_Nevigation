package com.devkt.adminguninevigation.api

import com.devkt.adminguninevigation.model.AddLocationResponse
import com.devkt.adminguninevigation.model.CategoryWiseLocationCountResponse
import com.devkt.adminguninevigation.model.DashboardResponse
import com.devkt.adminguninevigation.model.GetMoreLocationsResponse
import com.devkt.adminguninevigation.model.GetSpecificLocationResponse
import com.devkt.adminguninevigation.model.GetSpecificSubLocationResponse
import com.devkt.adminguninevigation.model.LoginAdminResponse
import com.devkt.adminguninevigation.model.MainLocationsResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface ApiService {
    @FormUrlEncoded
    @POST("loginAdmin")
    suspend fun login(
        @Field("name") name: String,
        @Field("password") password: String
    ): Response<LoginAdminResponse>

    @Multipart
    @POST("uploadLocation")
    suspend fun uploadLocation(
        @Part("location") location: RequestBody,
        @Part image: MultipartBody.Part
    ): Response<AddLocationResponse>

    @GET("getAllMainLocations")
    suspend fun getMainLocations(): Response<MainLocationsResponse>

    @Multipart
    @POST("uploadSubPlaces")
    suspend fun uploadSubLocations(
        @Part("subLocation") subLocation: RequestBody,
        @Part("mainLocation") mainLocation: RequestBody,
        @Part("description") description: RequestBody,
        @Part("phone_no") phone_no: RequestBody,
        @Part("longitude") longitude: RequestBody,
        @Part("latitude") latitude: RequestBody,
        @Part image: MultipartBody.Part
    ): Response<AddLocationResponse>

    @GET("dashboard")
    suspend fun dashboard(): Response<DashboardResponse>

    @GET("getAllLocation")
    suspend fun getMoreLocations(): Response<GetMoreLocationsResponse>

    @Multipart
    @POST("updateLocationCategory")
    suspend fun updateLocationCategory(
        @Part("old_name") old_name: RequestBody,
        @Part("new_name") new_name: RequestBody?,
        @Part image: MultipartBody.Part?
    ): Response<AddLocationResponse>

    @FormUrlEncoded
    @POST("getSpecificLocation")
    suspend fun getSpecificLocation(
        @Field("location_name") name: String
    ): Response<GetSpecificLocationResponse>

    @FormUrlEncoded
    @POST("deleteLocationCategory")
    suspend fun deleteLocationCategory(
        @Field("location") location_name: String
    ): Response<AddLocationResponse>

    @Multipart
    @POST("updateSubLocation")
    suspend fun updateSubLocation(
        @Part("old_name") old_name: RequestBody,
        @Part("main_location_name") main_location_name: RequestBody?,
        @Part("sub_location_name") sub_location_name: RequestBody?,
        @Part("sub_location_description") sub_location_description: RequestBody?,
        @Part("sub_location_phone_no") sub_location_phone_no: RequestBody?,
        @Part("longitude") longitude: RequestBody?,
        @Part("latitude") sub_location_latitude: RequestBody?,
        @Part image: MultipartBody.Part?
    ): Response<AddLocationResponse>

    @FormUrlEncoded
    @POST("deleteSubLocation")
    suspend fun deleteSubLocation(
        @Field("sub_location_name") sub_location_name: String
    ): Response<AddLocationResponse>

    @FormUrlEncoded
    @POST("getSpecificSubLocation")
    suspend fun getSpecificSubLocation(
        @Field("name") name: String
    ): Response<GetSpecificSubLocationResponse>

    @FormUrlEncoded
    @POST("getCategoryWiseLocationCount")
    suspend fun getCategoryWiseLocationCount(
        @Field("category") category: String
    ): Response<CategoryWiseLocationCountResponse>

    @FormUrlEncoded
    @POST("getCategoryWiseLocations")
    suspend fun getCategoryWiseLocations(
        @Field("category") category: String
    ): Response<MainLocationsResponse>
}
package com.devkt.adminguninevigation.repo

import com.devkt.adminguninevigation.api.ApiBuilder
import com.devkt.adminguninevigation.common.Result
import com.devkt.adminguninevigation.model.AddLocationResponse
import com.devkt.adminguninevigation.model.CategoryWiseLocationCountResponse
import com.devkt.adminguninevigation.model.DashboardResponse
import com.devkt.adminguninevigation.model.GetMoreLocationsResponse
import com.devkt.adminguninevigation.model.GetSpecificLocationResponse
import com.devkt.adminguninevigation.model.GetSpecificSubLocationResponse
import com.devkt.adminguninevigation.model.LoginAdminResponse
import com.devkt.adminguninevigation.model.MainLocationsResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import javax.inject.Inject

class Repo @Inject constructor(private val api: ApiBuilder) {
    suspend fun loginAdmin(
        name: String,
        password: String
    ): Flow<Result<Response<LoginAdminResponse>>> = flow {
        emit(Result.Loading)
        try {
            val response = api.api.login(name, password)
            emit(Result.Success(response))
        } catch (e : Exception){
            emit(Result.Error(e.message.toString()))
        }
    }

    suspend fun addLocation(
        location: RequestBody,
        image: MultipartBody.Part
    ): Flow<Result<Response<AddLocationResponse>>> = flow {
        emit(Result.Loading)
        try {
            val response = api.api.uploadLocation(location, image)
            emit(Result.Success(response))
        } catch (e : Exception) {
            emit(Result.Error(e.message.toString()))
        }
    }

    suspend fun getMainLocations(): Flow<Result<Response<MainLocationsResponse>>> = flow {
        emit(Result.Loading)
        try {
            val response = api.api.getMainLocations()
            emit(Result.Success(response))
        } catch (e : Exception) {
            emit(Result.Error(e.message.toString()))
        }
    }

    suspend fun uploadSubLocations(
        subLocation: RequestBody,
        mainLocation: RequestBody,
        description: RequestBody,
        phone_no: RequestBody,
        longitude: RequestBody,
        latitude: RequestBody,
        image: MultipartBody.Part
    ): Flow<Result<Response<AddLocationResponse>>> = flow {
        emit(Result.Loading)
        try {
            val response = api.api.uploadSubLocations(subLocation, mainLocation, description, phone_no, longitude, latitude, image)
            emit(Result.Success(response))
        } catch (e : Exception) {
            emit(Result.Error(e.message.toString()))
        }
    }

    suspend fun dashboard(): Flow<Result<Response<DashboardResponse>>> = flow {
        emit(Result.Loading)
        try {
            val response = api.api.dashboard()
            emit(Result.Success(response))
        } catch (e : Exception) {
            emit(Result.Error(e.message.toString()))
        }
    }

    suspend fun getMoreLocations(): Flow<Result<Response<GetMoreLocationsResponse>>> = flow {
        emit(Result.Loading)
        try {
            val response = api.api.getMoreLocations()
            emit(Result.Success(response))
        } catch (e : Exception) {
            emit(Result.Error(e.message.toString()))
        }
    }

    suspend fun updateLocationCategory(
        old_name: RequestBody,
        new_name: RequestBody?,
        image: MultipartBody.Part?
    ): Flow<Result<Response<AddLocationResponse>>> = flow {
        emit(Result.Loading)
        try {
            val response = api.api.updateLocationCategory(old_name, new_name, image)
            emit(Result.Success(response))
        } catch (e : Exception) {
            emit(Result.Error(e.message.toString()))
        }
    }

    suspend fun getSpecificLocation(
        name: String
    ): Flow<Result<Response<GetSpecificLocationResponse>>> = flow {
        emit(Result.Loading)
        try {
            val response = api.api.getSpecificLocation(name)
            emit(Result.Success(response))
        } catch (e : Exception) {
            emit(Result.Error(e.message.toString()))
        }
    }

    suspend fun deleteLocationCategory(
        location_name: String
    ): Flow<Result<Response<AddLocationResponse>>> = flow {
        emit(Result.Loading)
        try {
            val response = api.api.deleteLocationCategory(location_name)
            emit(Result.Success(response))
        } catch (e : Exception) {
            emit(Result.Error(e.message.toString()))
        }
    }

    suspend fun updateSubLocation(
        old_name: RequestBody,
        main_location_name: RequestBody?,
        sub_location_name: RequestBody?,
        sub_location_description: RequestBody?,
        sub_location_phone_no: RequestBody?,
        longitude: RequestBody?,
        latitude: RequestBody?,
        image: MultipartBody.Part?
    ): Flow<Result<Response<AddLocationResponse>>> = flow {
        emit(Result.Loading)
        try {
            val response = api.api.updateSubLocation(
                old_name,
                main_location_name,
                sub_location_name,
                sub_location_description,
                sub_location_phone_no,
                longitude,
                latitude,
                image
            )
            emit(Result.Success(response))
        }
        catch (e : Exception) {
            emit(Result.Error(e.message.toString()))
        }
    }

    suspend fun deleteSubLocation(
        sub_location_name: String
    ): Flow<Result<Response<AddLocationResponse>>> = flow {
        emit(Result.Loading)
        try {
            val response = api.api.deleteSubLocation(sub_location_name)
            emit(Result.Success(response))
        } catch (e : Exception) {
            emit(Result.Error(e.message.toString()))
        }
    }

    suspend fun getSpecificSubLocation(
        name: String
    ): Flow<Result<Response<GetSpecificSubLocationResponse>>> = flow {
        emit(Result.Loading)
        try {
            val response = api.api.getSpecificSubLocation(name)
            emit(Result.Success(response))
        } catch (e : Exception) {
            emit(Result.Error(e.message.toString()))
        }
    }

    suspend fun getCategoryWiseLocationCount(
        category: String
    ): Flow<Result<Response<CategoryWiseLocationCountResponse>>> = flow {
        emit(Result.Loading)
        try {
            val response = api.api.getCategoryWiseLocationCount(category)
            emit(Result.Success(response))
        } catch (e : Exception) {
            emit(Result.Error(e.message.toString()))
        }
    }

    suspend fun getCategoryWiseLocations(
        category: String
    ): Flow<Result<Response<MainLocationsResponse>>> = flow {
        emit(Result.Loading)
        try {
            val response = api.api.getCategoryWiseLocations(category)
            emit(Result.Success(response))
        } catch (e : Exception) {
            emit(Result.Error(e.message.toString()))
        }
    }
}
package com.devkt.adminguninevigation.viewModel

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devkt.adminguninevigation.common.Result
import com.devkt.adminguninevigation.model.AddLocationResponse
import com.devkt.adminguninevigation.model.CategoryWiseLocationCountResponse
import com.devkt.adminguninevigation.model.DashboardResponse
import com.devkt.adminguninevigation.model.GetMoreLocationsResponse
import com.devkt.adminguninevigation.model.GetSpecificLocationResponse
import com.devkt.adminguninevigation.model.GetSpecificSubLocationResponse
import com.devkt.adminguninevigation.model.LoginAdminResponse
import com.devkt.adminguninevigation.model.MainLocationsResponse
import com.devkt.adminguninevigation.repo.Repo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject

@HiltViewModel
class MyViewModel @Inject constructor(private val repo: Repo) : ViewModel() {
    private val _loginAdmin = MutableStateFlow(LoginAdminState())
    val loginAdmin = _loginAdmin.asStateFlow()

    private val _addLocation = MutableStateFlow(AddLocationState())
    val addLocation = _addLocation.asStateFlow()

    private val _mainLocations = MutableStateFlow(MainLocationsState())
    val mainLocations = _mainLocations.asStateFlow()

    private val _addSubLocation = MutableStateFlow(AddSubLocationState())
    val addSubLocation = _addSubLocation.asStateFlow()

    private val _dashboard = MutableStateFlow(DashboardState())
    val dashboard = _dashboard.asStateFlow()

    private val _getMoreLocations = MutableStateFlow(GetMoreLocationsState())
    val getMoreLocations = _getMoreLocations.asStateFlow()

    private val _updateLocationCategory = MutableStateFlow(UpdateLocationCategoryState())
    val updateLocationCategory = _updateLocationCategory.asStateFlow()

    private val _getSpecificLocation = MutableStateFlow(GetSpecificLocationState())
    val getSpecificLocation = _getSpecificLocation.asStateFlow()

    private val _deleteLocationCategory = MutableStateFlow(DeleteLocationCategoryState())
    val deleteLocationCategory = _deleteLocationCategory.asStateFlow()

    private val _updateSubLocation = MutableStateFlow(UpdateSubLocationState())
    val updateSubLocation = _updateSubLocation.asStateFlow()

    private val _getSpecificSubLocation = MutableStateFlow(GetSpecificSubLocationState())
    val getSpecificSubLocation = _getSpecificSubLocation.asStateFlow()

    private val _deleteSubLocation = MutableStateFlow(DeleteSubLocationState())
    val deleteSubLocation = _deleteSubLocation.asStateFlow()

    private val _getCategoryWiseLocationCount = MutableStateFlow(CategoryWiseLocationCountState())
    val getCategoryWiseLocationCount = _getCategoryWiseLocationCount.asStateFlow()

    private val _getCategoryWiseLocations = MutableStateFlow(GetCategoryWiseLocationsState())
    val getCategoryWiseLocations = _getCategoryWiseLocations.asStateFlow()

    fun loginAdmin(name: String, password: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repo.loginAdmin(name, password).collect {
                when (it) {
                    is Result.Loading -> {
                        _loginAdmin.value = LoginAdminState(isLoading = true)
                    }

                    is Result.Success -> {
                        _loginAdmin.value = LoginAdminState(data = it.data.body())
                    }

                    is Result.Error -> {
                        _loginAdmin.value = LoginAdminState(error = it.message)
                    }
                }
            }
        }
    }

    private fun uriToFile(context: Context, uri: Uri): File {
        val inputStream = context.contentResolver.openInputStream(uri)!!
        val file = File(context.cacheDir, "upload_image.jpg")
        val outputStream = FileOutputStream(file)
        inputStream.copyTo(outputStream)
        outputStream.close()
        inputStream.close()
        return file
    }

    fun addLocation(context: Context, location: String, imageUri: Uri) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val imageFile = uriToFile(context, imageUri)
                val requestFile = imageFile.asRequestBody("image/*".toMediaTypeOrNull())
                val imagePart =
                    MultipartBody.Part.createFormData("image", imageFile.name, requestFile)
                val locationPart = location.toRequestBody("text/plain".toMediaTypeOrNull())
                repo.addLocation(locationPart, imagePart).collect {
                    when (it) {
                        is Result.Loading -> {
                            _addLocation.value = AddLocationState(isLoading = true)
                        }

                        is Result.Success -> {
                            _addLocation.value = AddLocationState(data = it.data.body())
                        }

                        is Result.Error -> {
                            _addLocation.value = AddLocationState(error = it.message)
                        }
                    }
                }
            } catch (e: Exception) {
                _addLocation.value = AddLocationState(error = e.message)
            }
        }
    }

    fun fetchMainLocations() {
        viewModelScope.launch(Dispatchers.IO) {
            repo.getMainLocations().collect {
                when (it) {
                    is Result.Loading -> {
                        _mainLocations.value = MainLocationsState(isLoading = true)
                    }

                    is Result.Success -> {
                        _mainLocations.value = MainLocationsState(data = it.data.body())
                    }

                    is Result.Error -> {
                        _mainLocations.value = MainLocationsState(error = it.message)
                    }
                }
            }
        }
    }

    fun uploadSubLocations(
        context: Context,
        subLocation: String,
        mainLocation: String,
        description: String,
        phone_no: String,
        longitude: String,
        latitude: String,
        imageUri: Uri
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val imageFile = uriToFile(context, imageUri)
                val requestFile = imageFile.asRequestBody("image/*".toMediaTypeOrNull())
                val imagePart =
                    MultipartBody.Part.createFormData("image", imageFile.name, requestFile)
                val locationPart = subLocation.toRequestBody("text/plain".toMediaTypeOrNull())
                val mainLocationPart = mainLocation.toRequestBody("text/plain".toMediaTypeOrNull())
                val description = description.toRequestBody("text/plain".toMediaTypeOrNull())
                val phone_no = phone_no.toRequestBody("text/plain".toMediaTypeOrNull())
                val longitude = longitude.toRequestBody("text/plain".toMediaTypeOrNull())
                val latitude = latitude.toRequestBody("text/plain".toMediaTypeOrNull())
                repo.uploadSubLocations(
                    locationPart,
                    mainLocationPart,
                    description,
                    phone_no,
                    longitude,
                    latitude,
                    imagePart
                ).collect {
                    when (it) {
                        is Result.Loading -> {
                            _addSubLocation.value = AddSubLocationState(isLoading = true)
                        }

                        is Result.Success -> {
                            _addSubLocation.value = AddSubLocationState(data = it.data.body())
                        }

                        is Result.Error -> {
                            _addSubLocation.value = AddSubLocationState(error = it.message)
                        }
                    }
                }
            } catch (e: Exception) {
                _addLocation.value = AddLocationState(error = e.message)
            }
        }
    }

    fun dashboard() {
        viewModelScope.launch(Dispatchers.IO) {
            repo.dashboard().collect {
                when (it) {
                    is Result.Loading -> {
                        _dashboard.value = DashboardState(isLoading = true)
                    }

                    is Result.Success -> {
                        _dashboard.value = DashboardState(data = it.data.body())
                    }

                    is Result.Error -> {
                        _dashboard.value = DashboardState(error = it.message)
                    }
                }
            }
        }
    }

    fun getMoreLocations() {
        viewModelScope.launch(Dispatchers.IO) {
            repo.getMoreLocations().collect {
                when (it) {
                    is Result.Loading -> {
                        _getMoreLocations.value = GetMoreLocationsState(isLoading = true)
                    }

                    is Result.Success -> {
                        _getMoreLocations.value = GetMoreLocationsState(data = it.data.body())
                    }

                    is Result.Error -> {
                        _getMoreLocations.value = GetMoreLocationsState(error = it.message)
                    }
                }
            }
        }
    }

    private fun uriToFileWithNull(context: Context, uri: Uri?): File? {
        if (uri == null) {
            return null
        } else {
            val inputStream = context.contentResolver.openInputStream(uri)!!
            val file = File(context.cacheDir, "upload_image.jpg")
            val outputStream = FileOutputStream(file)
            inputStream.copyTo(outputStream)
            outputStream.close()
            inputStream.close()
            return file
        }
    }

    fun updateLocationCategory(
        context: Context,
        old_name: String,
        new_name: String?,
        imageUri: Uri?
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val imageFile = uriToFileWithNull(context, imageUri)
                val requestFile = imageFile?.asRequestBody("image/*".toMediaTypeOrNull())
                val imagePart: MultipartBody.Part? = if (requestFile != null) {
                    MultipartBody.Part.createFormData("image", imageFile.name, requestFile)
                } else {
                    null
                }
                val old_name = old_name.toRequestBody("text/plain".toMediaTypeOrNull())
                val new_name = new_name?.toRequestBody("text/plain".toMediaTypeOrNull())
                repo.updateLocationCategory(old_name, new_name, imagePart).collect {
                    when (it) {
                        is Result.Loading -> {
                            _updateLocationCategory.value =
                                UpdateLocationCategoryState(isLoading = true)
                        }

                        is Result.Success -> {
                            _updateLocationCategory.value =
                                UpdateLocationCategoryState(data = it.data.body())
                        }

                        is Result.Error -> {
                            _updateLocationCategory.value =
                                UpdateLocationCategoryState(error = it.message)
                        }
                    }
                }
            } catch (e: Exception) {
                _updateLocationCategory.value = UpdateLocationCategoryState(error = e.message)
            }
        }
    }

    fun getSpecificLocation(name: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repo.getSpecificLocation(name).collect {
                when (it) {
                    is Result.Loading -> {
                        _getSpecificLocation.value = GetSpecificLocationState(isLoading = true)
                    }

                    is Result.Success -> {
                        _getSpecificLocation.value = GetSpecificLocationState(data = it.data.body())
                    }

                    is Result.Error -> {
                        _getSpecificLocation.value = GetSpecificLocationState(error = it.message)
                    }
                }
            }
        }
    }

    fun deleteLocationCategory(location_name: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repo.deleteLocationCategory(location_name).collect {
                when (it) {
                    is Result.Loading -> {
                        _deleteLocationCategory.value =
                            DeleteLocationCategoryState(isLoading = true)
                    }

                    is Result.Success -> {
                        _deleteLocationCategory.value =
                            DeleteLocationCategoryState(data = it.data.body())
                    }

                    is Result.Error -> {
                        _deleteLocationCategory.value =
                            DeleteLocationCategoryState(error = it.message)
                    }
                }
            }
        }
    }

    fun updateSubLocation(
        context: Context,
        old_name: String,
        main_location_name: String?,
        sub_location_name: String?,
        sub_location_description: String?,
        sub_location_phone_no: String?,
        longitude: String?,
        latitude: String?,
        imageUri: Uri?
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val imageFile = uriToFileWithNull(context, imageUri)
                val requestFile = imageFile?.asRequestBody("image/*".toMediaTypeOrNull())
                val imagePart: MultipartBody.Part? = if (requestFile != null) {
                    MultipartBody.Part.createFormData("image", imageFile.name, requestFile)
                } else {
                    null
                }
                val old_name = old_name.toRequestBody("text/plain".toMediaTypeOrNull())
                val main_location_name =
                    main_location_name?.toRequestBody("text/plain".toMediaTypeOrNull())
                val sub_location_name =
                    sub_location_name?.toRequestBody("text/plain".toMediaTypeOrNull())
                val sub_location_description =
                    sub_location_description?.toRequestBody("text/plain".toMediaTypeOrNull())
                val sub_location_phone_no =
                    sub_location_phone_no?.toRequestBody("text/plain".toMediaTypeOrNull())
                val longitude = longitude?.toRequestBody("text/plain".toMediaTypeOrNull())
                val latitude = latitude?.toRequestBody("text/plain".toMediaTypeOrNull())
                repo.updateSubLocation(
                    old_name,
                    main_location_name,
                    sub_location_name,
                    sub_location_description,
                    sub_location_phone_no,
                    longitude,
                    latitude,
                    imagePart
                ).collect {
                    when (it) {
                        is Result.Loading -> {
                            _updateSubLocation.value = UpdateSubLocationState(isLoading = true)
                        }

                        is Result.Success -> {
                            _updateSubLocation.value = UpdateSubLocationState(data = it.data.body())
                        }

                        is Result.Error -> {
                            _updateSubLocation.value = UpdateSubLocationState(error = it.message)
                        }
                    }
                }
            } catch (e: Exception) {
                _updateSubLocation.value = UpdateSubLocationState(error = e.message)
            }
        }
    }

    fun deleteSubLocation(sub_location_name: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repo.deleteSubLocation(sub_location_name).collect {
                when (it) {
                    is Result.Loading -> {
                        _deleteSubLocation.value = DeleteSubLocationState(isLoading = true)
                    }
                    is Result.Success -> {
                        _deleteSubLocation.value = DeleteSubLocationState(data = it.data.body())
                    }
                    is Result.Error -> {
                        _deleteSubLocation.value = DeleteSubLocationState(error = it.message)
                    }
                }
            }
        }
    }

    fun getSpecificSubLocation(name: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repo.getSpecificSubLocation(name).collect {
                when (it) {
                    is Result.Loading -> {
                        _getSpecificSubLocation.value =
                            GetSpecificSubLocationState(isLoading = true)
                    }

                    is Result.Success -> {
                        _getSpecificSubLocation.value =
                            GetSpecificSubLocationState(data = it.data.body())
                    }

                    is Result.Error -> {
                        _getSpecificSubLocation.value =
                            GetSpecificSubLocationState(error = it.message)
                    }
                }
            }
        }
    }

    fun categoryWiseLocationCount(category: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repo.getCategoryWiseLocationCount(category).collect {
                when (it) {
                    is Result.Loading -> {
                        _getCategoryWiseLocationCount.value =
                            CategoryWiseLocationCountState(isLoading = true)
                    }
                    is Result.Success -> {
                        _getCategoryWiseLocationCount.value =
                            CategoryWiseLocationCountState(data = it.data.body())
                    }
                    is Result.Error -> {
                        _getCategoryWiseLocationCount.value =
                            CategoryWiseLocationCountState(error = it.message)
                    }
                }
            }
        }
    }

    fun getCategoryWiseLocations(category: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repo.getCategoryWiseLocations(category).collect {
                when (it) {
                    is Result.Loading -> {
                        _getCategoryWiseLocations.value =
                            GetCategoryWiseLocationsState(isLoading = true)
                    }

                    is Result.Success -> {
                        _getCategoryWiseLocations.value =
                            GetCategoryWiseLocationsState(data = it.data.body())
                    }

                    is Result.Error -> {
                        _getCategoryWiseLocations.value =
                            GetCategoryWiseLocationsState(error = it.message)
                    }
                }
            }
        }
    }
}

data class LoginAdminState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val data: LoginAdminResponse? = null
)

data class AddLocationState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val data: AddLocationResponse? = null
)

data class MainLocationsState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val data: MainLocationsResponse? = null
)

data class AddSubLocationState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val data: AddLocationResponse? = null
)

data class DashboardState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val data: DashboardResponse? = null
)

data class GetMoreLocationsState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val data: GetMoreLocationsResponse? = null
)

data class UpdateLocationCategoryState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val data: AddLocationResponse? = null
)

data class GetSpecificLocationState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val data: GetSpecificLocationResponse? = null
)

data class DeleteLocationCategoryState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val data: AddLocationResponse? = null
)

data class UpdateSubLocationState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val data: AddLocationResponse? = null
)

data class GetSpecificSubLocationState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val data: GetSpecificSubLocationResponse? = null
)

data class DeleteSubLocationState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val data: AddLocationResponse? = null
)

data class CategoryWiseLocationCountState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val data: CategoryWiseLocationCountResponse? = null
)

data class GetCategoryWiseLocationsState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val data: MainLocationsResponse? = null
)
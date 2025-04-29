package com.devkt.adminguninevigation.screens

import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts.PickVisualMedia
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import com.devkt.adminguninevigation.R
import com.devkt.adminguninevigation.screens.nav.Routs
import com.devkt.adminguninevigation.viewModel.MyViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UpdateAllLocationScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    viewModel: MyViewModel = hiltViewModel(),
    subLocationName: String
) {
    val fetchState = viewModel.getSpecificSubLocation.collectAsState()
    val updateState = viewModel.updateSubLocation.collectAsState()
    val deleteState = viewModel.deleteSubLocation.collectAsState()
    val mainLocationState = viewModel.mainLocations.collectAsState()
    val context = LocalContext.current

    val initialSubLocationName = remember { mutableStateOf("") }
    val initialImageUri = remember { mutableStateOf<Uri?>(null) }
    val initialMainLocationName = remember { mutableStateOf("") }
    val initialSubLocationDescription = remember { mutableStateOf("") }
    val initialSubLocationPhoneNumber = remember { mutableStateOf("") }
    val initialSubLocationLongitude = remember { mutableStateOf("") }
    val initialSubLocationLatitude = remember { mutableStateOf("") }

    val newSubLocationName = remember { mutableStateOf("") }
    val newImageUri = remember { mutableStateOf("") }
    val newMainLocationName = remember { mutableStateOf("") }
    val newSubLocationDescription = remember { mutableStateOf("") }
    val newSubLocationPhoneNumber = remember { mutableStateOf("") }
    val newSubLocationLongitude = remember { mutableStateOf("") }
    val newSubLocationLatitude = remember { mutableStateOf("") }

    var expanded = remember { mutableStateOf(false) }
    var selectedOption = remember { mutableStateOf("") }
    val mainLocations = remember { mutableStateOf<List<String>>(emptyList()) }
    var checked = remember { mutableStateOf(true) }

    val pickMedia = rememberLauncherForActivityResult(
        PickVisualMedia(),
        onResult = { uri ->
            uri?.let {
                newImageUri.value = it.toString()
                Toast.makeText(context, "Photo selected successfully", Toast.LENGTH_SHORT).show()
            }
        }
    )

    val savedStateHandle = navController.currentBackStackEntry?.savedStateHandle
    val pickedLat = savedStateHandle?.get<Double>("picked_latitude")
    val pickedLng = savedStateHandle?.get<Double>("picked_longitude")

    LaunchedEffect(pickedLat, pickedLng) {
        if (pickedLat != null && pickedLng != null) {
            newSubLocationLongitude.value = pickedLng.toString()
            newSubLocationLatitude.value = pickedLat.toString()

            savedStateHandle.remove<Double>("picked_latitude")
            savedStateHandle.remove<Double>("picked_longitude")
        }
    }

    LaunchedEffect(
        key1 = true
    ) {
        viewModel.fetchMainLocations()
    }

    LaunchedEffect(Unit) {
        viewModel.getSpecificSubLocation(subLocationName)
    }

    LaunchedEffect(updateState.value.data) {
        updateState.value.data?.let {
            Toast.makeText(context, "Location Updated Successfully", Toast.LENGTH_SHORT).show()
        }
    }

    when {
        mainLocationState.value.isLoading -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }

        mainLocationState.value.error != null -> {
            Text(text = mainLocationState.value.error!!, modifier = Modifier.padding(50.dp))
        }

        mainLocationState.value.data != null -> {
            mainLocations.value = mainLocationState.value.data!!.message.map { it[1].toString() }
        }
    }

    when {
        deleteState.value.isLoading -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
        deleteState.value.error != null -> {
            Text(text = deleteState.value.error!!, modifier = Modifier.padding(16.dp))
        }
        deleteState.value.data != null -> {
            Toast.makeText(context, "Data Deleted", Toast.LENGTH_SHORT).show()
        }
    }

    when {
        updateState.value.isLoading || fetchState.value.isLoading -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
            return
        }

        updateState.value.error != null -> {
            Text(text = updateState.value.error!!, modifier = Modifier.padding(16.dp))
            return
        }

        fetchState.value.error != null -> {
            Text(text = fetchState.value.error!!, modifier = Modifier.padding(16.dp))
            return
        }
    }

    fetchState.value.data?.message?.firstOrNull()?.let { location ->
        val nameFromApi = location.name ?: ""
        val imageUrlFromApi = location.imageUrl ?: ""
        val mainLocationFromApi = location.mainLocation ?: ""
        val descriptionFromApi = location.description ?: ""
        val phoneNumberFromApi = location.phoneNo ?: ""
        val longitudeFromApi = location.longitude ?: ""
        val latitudeFromApi = location.latitude ?: ""

        LaunchedEffect(nameFromApi, imageUrlFromApi, mainLocationFromApi, descriptionFromApi, phoneNumberFromApi, longitudeFromApi, latitudeFromApi) {
            if (initialSubLocationName.value.isEmpty()) {
                initialSubLocationName.value = nameFromApi
                newSubLocationName.value = nameFromApi
            }
            if (initialImageUri.value == null) {
                val uri = imageUrlFromApi.toUri()
                initialImageUri.value = uri
                newImageUri.value = uri.toString()
            }
            if (initialMainLocationName.value.isEmpty()) {
                initialMainLocationName.value = mainLocationFromApi
                newMainLocationName.value = mainLocationFromApi
                selectedOption.value = mainLocationFromApi
            }
            if (initialSubLocationDescription.value.isEmpty()) {
                initialSubLocationDescription.value = descriptionFromApi
                newSubLocationDescription.value = descriptionFromApi
            }
            if (initialSubLocationPhoneNumber.value.isEmpty()) {
                initialSubLocationPhoneNumber.value = phoneNumberFromApi
                newSubLocationPhoneNumber.value = phoneNumberFromApi
            }
            if (initialSubLocationLongitude.value.isEmpty()) {
                initialSubLocationLongitude.value = longitudeFromApi
            }
            if (initialSubLocationLatitude.value.isEmpty()) {
                initialSubLocationLatitude.value = latitudeFromApi
            }
        }

        var currentName = newSubLocationName.value ?: ""
        val currentImageUri = newImageUri.value
        var currentMainLocation = if (newMainLocationName.value.isEmpty()) {
            initialMainLocationName.value
        } else {
            newMainLocationName.value
        }
        var currentDescription = newSubLocationDescription.value ?: ""
        var currentPhoneNumber = newSubLocationPhoneNumber.value ?: ""
        val currentLongitude = if (newSubLocationLongitude.value == ""){
            initialSubLocationLongitude.value
        } else {
            newSubLocationLongitude.value
        }
        val currentLatitude = if (newSubLocationLatitude.value == ""){
            initialSubLocationLatitude.value
        } else {
            newSubLocationLatitude.value
        }

        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Preview",
                    fontWeight = FontWeight.Bold,
                    fontSize = 25.sp
                )
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp)
                        .padding(top = 10.dp),
                ) {
                    Row(
                        modifier = Modifier.fillMaxSize(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        AsyncImage(
                            model = currentImageUri,
                            contentDescription = null,
                            modifier = Modifier
                                .fillMaxHeight()
                                .width(70.dp)
                                .padding(top = 5.dp, bottom = 5.dp, start = 10.dp)
                        )
                        Text(
                            text = currentName,
                            fontSize = 15.sp,
                            modifier = Modifier.padding(start = 10.dp)
                        )
                        Spacer(modifier = Modifier.weight(1f))
                        Icon(
                            imageVector = ImageVector.vectorResource(id = R.drawable.white_arrow),
                            contentDescription = null,
                            modifier = Modifier.padding(end = 10.dp)
                        )
                    }
                }
                Button(
                    onClick = {
                        pickMedia.launch(PickVisualMediaRequest(PickVisualMedia.ImageOnly))
                    },
                    modifier = Modifier.padding(top = 10.dp)
                ) {
                    Text(text = "Add Location Image")
                }
                OutlinedTextField(
                    value = currentName,
                    onValueChange = { newSubLocationName.value = it },
                    label = { Text(text = "Location Name") },
                    modifier = Modifier.padding(top = 10.dp)
                )
                ExposedDropdownMenuBox(
                    expanded = expanded.value,
                    onExpandedChange = { expanded.value = !expanded.value },
                    modifier = Modifier.padding(top = 10.dp)
                ) {
                    OutlinedTextField(
                        value = selectedOption.value,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Select Location Category") },
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded.value)
                        },
                        modifier = Modifier
                            .menuAnchor(MenuAnchorType.PrimaryNotEditable, enabled = true)
                    )
                    ExposedDropdownMenu(
                        expanded = expanded.value,
                        onDismissRequest = { expanded.value = false }
                    ) {
                        mainLocations.value.forEach { selectionOption ->
                            DropdownMenuItem(
                                text = { Text(selectionOption) },
                                onClick = {
                                    selectedOption.value = selectionOption
                                    currentMainLocation = selectionOption
                                    expanded.value = false
                                }
                            )
                        }
                    }
                    Log.d("TAG", "UpdateAllLocationScreen: $currentMainLocation")
                }
                Button(
                    onClick = {
                        navController.navigate(Routs.LocationPickerScreen)
                    },
                    modifier = Modifier.padding(top = 10.dp)
                ) {
                    Text(text = "Pick Location Coordinates")
                }
                OutlinedTextField(
                    value = currentLongitude,
                    onValueChange = { },
                    readOnly = true,
                    label = { Text(text = "Longitude") },
                    modifier = Modifier.padding(top = 10.dp)
                )
                OutlinedTextField(
                    value = currentLatitude,
                    onValueChange = { },
                    readOnly = true,
                    label = { Text(text = "Latitude") },
                    modifier = Modifier.padding(top = 10.dp)
                )
                OutlinedTextField(
                    value = currentDescription,
                    onValueChange = { newSubLocationDescription.value = it },
                    label = { Text(text = "Description") },
                    modifier = Modifier.padding(top = 10.dp)
                )
                Row(
                    modifier = Modifier.padding(top = 10.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "Add Phone Number")
                    Switch(
                        checked = checked.value,
                        onCheckedChange = {
                            checked.value = it
                        },
                        modifier = Modifier.padding(start = 10.dp)
                    )
                }
                if (checked.value) {
                    OutlinedTextField(
                        value = currentPhoneNumber,
                        onValueChange = { newSubLocationPhoneNumber.value = it },
                        label = { Text(text = "Phone Number") },
                        modifier = Modifier.padding(top = 10.dp)
                    )
                }
                Button(
                    onClick = {
                        if (!checked.value){
                            currentPhoneNumber = "+000000000000"
                        }
                        if (currentName.isEmpty()) {
                            Toast.makeText(context, "Please enter location name", Toast.LENGTH_SHORT).show()
                        } else if (currentMainLocation.isEmpty()) {
                            Toast.makeText(context, "Please select main location", Toast.LENGTH_SHORT).show()
                        } else if (currentLongitude.isEmpty() || currentLatitude.isEmpty()) {
                            Toast.makeText(context, "Please select location coordinates", Toast.LENGTH_SHORT).show()
                        } else {
                            viewModel.updateSubLocation(
                                context = context,
                                old_name = initialSubLocationName.value,
                                main_location_name = selectedOption.value,
                                sub_location_name = currentName,
                                sub_location_description = currentDescription,
                                sub_location_phone_no = currentPhoneNumber,
                                longitude = currentLongitude,
                                latitude = currentLatitude,
                                imageUri = currentImageUri.toUri()
                            )
                        }
                    },
                    modifier = Modifier.padding(top = 20.dp)
                ) {
                    Text(text = "Save Location")
                }
            }
        }
    }
}
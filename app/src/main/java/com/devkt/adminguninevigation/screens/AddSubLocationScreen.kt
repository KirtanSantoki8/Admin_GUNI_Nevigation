package com.devkt.adminguninevigation.screens

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts.PickVisualMedia
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconToggleButton
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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import com.devkt.adminguninevigation.R
import com.devkt.adminguninevigation.screens.nav.Routs
import com.devkt.adminguninevigation.viewModel.MyViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddSubLocationScreen(
    modifier: Modifier = Modifier,
    viewModel: MyViewModel = hiltViewModel(),
    navController: NavController
) {
    val mainLocationState = viewModel.mainLocations.collectAsState()
    val subLocationState = viewModel.addSubLocation.collectAsState()
    val context = LocalContext.current

    val mainLocations = remember { mutableStateOf<List<String>>(emptyList()) }

    val description = remember { mutableStateOf("") }
    val phone_no = remember { mutableStateOf("") }
    val latitude = remember { mutableStateOf("") }
    val longitude = remember { mutableStateOf("") }

    val savedStateHandle = navController.currentBackStackEntry?.savedStateHandle
    val pickedLat = savedStateHandle?.get<Double>("picked_latitude")
    val pickedLng = savedStateHandle?.get<Double>("picked_longitude")

    var checked = remember { mutableStateOf(true) }

    LaunchedEffect(pickedLat, pickedLng) {
        if (pickedLat != null && pickedLng != null) {
            latitude.value = pickedLat.toString()
            longitude.value = pickedLng.toString()

            savedStateHandle.remove<Double>("picked_latitude")
            savedStateHandle.remove<Double>("picked_longitude")
        }
    }


    val subLocationName = remember { mutableStateOf("") }
    var selectedImageUri = remember { mutableStateOf<Uri?>(null) }
    val pickMedia = rememberLauncherForActivityResult(
        PickVisualMedia(),
        onResult = { uri ->
            if (uri != null) {
                selectedImageUri.value = uri
                Toast.makeText(context, "Photo selected successfully", Toast.LENGTH_SHORT).show()
            }
        }
    )
    var expanded = remember { mutableStateOf(false) }
    var selectedOption = remember { mutableStateOf("") }

    LaunchedEffect(
        key1 = true
    ) {
        viewModel.fetchMainLocations()
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
        subLocationState.value.isLoading -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }

        subLocationState.value.error != null -> {
            Text(text = subLocationState.value.error!!, modifier = Modifier.padding(50.dp))
        }

        subLocationState.value.data != null -> {
            Toast.makeText(context, "Location Added Successfully", Toast.LENGTH_SHORT).show()
        }
    }

    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier.fillMaxSize()
            .verticalScroll(scrollState),
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
                        model = selectedImageUri.value,
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxHeight()
                            .width(70.dp)
                            .padding(top = 5.dp, bottom = 5.dp, start = 10.dp)
                    )
                    Text(
                        text = subLocationName.value,
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
                value = subLocationName.value,
                onValueChange = { subLocationName.value = it },
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
                                expanded.value = false
                            }
                        )
                    }
                }
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
                value = longitude.value,
                onValueChange = { },
                readOnly = true,
                label = { Text(text = "Longitude") },
                modifier = Modifier.padding(top = 10.dp)
            )
            OutlinedTextField(
                value = latitude.value,
                onValueChange = { },
                readOnly = true,
                label = { Text(text = "Latitude") },
                modifier = Modifier.padding(top = 10.dp)
            )
            OutlinedTextField(
                value = description.value,
                onValueChange = { description.value = it },
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
                    value = phone_no.value,
                    onValueChange = { phone_no.value = it },
                    label = { Text(text = "Phone Number") },
                    modifier = Modifier.padding(top = 10.dp)
                )
            }
            Button(
                onClick = {
                    if (!checked.value){
                        phone_no.value = "+000000000000"
                    }
                    if (subLocationName.value == "") {
                        Toast.makeText(context, "Please enter location name", Toast.LENGTH_SHORT)
                            .show()
                    } else if (selectedImageUri.value == null) {
                        Toast.makeText(context, "Please select location photo", Toast.LENGTH_SHORT)
                            .show()
                    } else if (selectedOption.value == "") {
                        Toast.makeText(
                            context,
                            "Please select location category",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else if (longitude.value.isEmpty() || latitude.value.isEmpty()) {
                        Toast.makeText(
                            context,
                            "Please select location coordinates",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else if (description.value == "") {
                        Toast.makeText(context, "Please enter description", Toast.LENGTH_SHORT)
                            .show()
                    } else if (phone_no.value == "" && checked.value) {
                        Toast.makeText(context, "Please enter phone number", Toast.LENGTH_SHORT)
                            .show()
                    } else {
                        viewModel.uploadSubLocations(
                            context,
                            subLocationName.value,
                            selectedOption.value,
                            description.value,
                            phone_no.value,
                            longitude.value,
                            latitude.value,
                            selectedImageUri.value!!
                        )
                    }
                },
                modifier = Modifier.padding(top = 10.dp)
            ) {
                Text(text = "Add Location")
            }
        }
    }
}
package com.devkt.adminguninevigation.screens

import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts.PickVisualMedia
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import com.devkt.adminguninevigation.viewModel.MyViewModel

@Composable
fun UpdateLocationCategoryScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    viewModel: MyViewModel = hiltViewModel(),
    place: String
) {
    val updateState = viewModel.updateLocationCategory.collectAsState()
    val fetchState = viewModel.getSpecificLocation.collectAsState()
    val deleteState = viewModel.deleteLocationCategory.collectAsState()
    val context = LocalContext.current

    val initialLocationName = remember { mutableStateOf("") }
    val initialImageUri = remember { mutableStateOf<Uri?>(null) }

    val userLocationName = remember { mutableStateOf("") }
    val userSelectedImageUri = remember { mutableStateOf<Uri?>(null) }

    val pickMedia = rememberLauncherForActivityResult(
        PickVisualMedia(),
        onResult = { uri ->
            uri?.let {
                userSelectedImageUri.value = it
                Toast.makeText(context, "Photo selected successfully", Toast.LENGTH_SHORT).show()
            }
        }
    )

    LaunchedEffect(Unit) {
        viewModel.getSpecificLocation(place)
    }

    LaunchedEffect(updateState.value.data) {
        updateState.value.data?.let {
            Toast.makeText(context, "Location Updated Successfully", Toast.LENGTH_SHORT).show()
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
        val nameFromApi = location.name
        val imageUrlFromApi = location.imageUrl

        LaunchedEffect(nameFromApi, imageUrlFromApi) {
            if (initialLocationName.value.isEmpty()) {
                initialLocationName.value = nameFromApi
                userLocationName.value = nameFromApi
            }
            if (initialImageUri.value == null) {
                val uri = imageUrlFromApi.toUri()
                initialImageUri.value = uri
                userSelectedImageUri.value = uri
            }
        }

        val currentName = userLocationName.value
        val currentImageUri = userSelectedImageUri.value

        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(16.dp)
                .scrollable(
                    orientation = Orientation.Vertical,
                    enabled = true,
                    state = rememberScrollState()
                ),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Preview",
                fontWeight = FontWeight.Bold,
                fontSize = 25.sp
            )

            Card(
                modifier = Modifier
                    .padding(top = 12.dp)
                    .height(130.dp)
                    .width(90.dp)
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    AsyncImage(
                        model = currentImageUri,
                        contentDescription = "Location Image",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .height(90.dp)
                            .width(90.dp)
                            .fillMaxWidth()
                    )
                    Text(
                        text = currentName,
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        modifier = Modifier.padding(top = 5.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Button(onClick = {
                pickMedia.launch(PickVisualMediaRequest(PickVisualMedia.ImageOnly))
            }) {
                Text(text = "Select Thumbnail Photo")
            }

            OutlinedTextField(
                value = currentName,
                onValueChange = { userLocationName.value = it },
                label = { Text("Location Name") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp)
            )

            Button(
                onClick = {
                    if (currentName.isEmpty()) {
                        Toast.makeText(context, "Please enter location name", Toast.LENGTH_SHORT).show()
                    } else {
                        val isImageChanged = !currentImageUri.toString().contains("https://")
                        val newImageUri = if (isImageChanged) currentImageUri else null
                        viewModel.updateLocationCategory(
                            context = context,
                            old_name = initialLocationName.value,
                            new_name = currentName,
                            imageUri = newImageUri
                        )
                    }
                },
                modifier = Modifier.padding(top = 10.dp)
            ) {
                Text(text = "Update Location")
            }
            Button(
                onClick = {
                    viewModel.deleteLocationCategory(initialLocationName.value)
                    navController.popBackStack()
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
                modifier = Modifier.padding(top = 10.dp)
            ) {
                Row {
                    Text(text = "Delete Location")
                    Spacer(modifier = Modifier.width(10.dp))
                    Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete")
                }
            }
        }
    }
}
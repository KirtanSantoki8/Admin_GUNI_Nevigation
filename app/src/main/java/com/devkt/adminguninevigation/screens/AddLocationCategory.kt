package com.devkt.adminguninevigation.screens

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts.PickVisualMedia
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import com.devkt.adminguninevigation.viewModel.MyViewModel

@Composable
fun AddLocationCategory(
    modifier: Modifier = Modifier,
    viewModel: MyViewModel = hiltViewModel(),
    navController: NavController
) {
    val state = viewModel.addLocation.collectAsState()
    val context = LocalContext.current

    val locationName = remember { mutableStateOf("") }
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

    when {
        state.value.isLoading -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }

        state.value.error != null -> {
            Text(text = state.value.error!!, modifier = Modifier.padding(50.dp))
        }

        state.value.data != null -> {
            Toast.makeText(context, "Location Added Successfully", Toast.LENGTH_SHORT).show()
        }
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
                    .height(130.dp)
                    .width(90.dp)
                    .padding(top = 10.dp)
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    AsyncImage(
                        model = selectedImageUri.value,
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .height(90.dp)
                            .width(90.dp)
                            .fillMaxWidth()
                    )
                    Text(
                        text = locationName.value,
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        modifier = Modifier.padding(top = 5.dp)
                    )
                }
            }
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(
                onClick = {
                    pickMedia.launch(PickVisualMediaRequest(PickVisualMedia.ImageOnly))
                }
            ) {
                Text(
                    text = "Select Thumbnail Photo"
                )
            }
            OutlinedTextField(
                value = locationName.value,
                onValueChange = { locationName.value = it },
                label = { Text(text = "Location Name") },
                modifier = Modifier.padding(top = 10.dp)
            )
            Button(
                onClick = {
                    if (locationName.value == "") {
                        Toast.makeText(context, "Please enter location name", Toast.LENGTH_SHORT)
                            .show()
                    } else if (selectedImageUri.value == null) {
                        Toast.makeText(context, "Please select thumbnail photo", Toast.LENGTH_SHORT)
                            .show()
                    } else {
                        viewModel.addLocation(context, locationName.value, selectedImageUri.value!!,)
                    }
                },
                modifier = Modifier.padding(top = 10.dp)
            ) {
                Text(text = "Add Location")
            }
        }
    }
}
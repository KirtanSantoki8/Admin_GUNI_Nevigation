package com.devkt.adminguninevigation.screens

import android.annotation.SuppressLint
import android.graphics.BitmapFactory
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController
import com.devkt.adminguninevigation.R
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.mapbox.geojson.Point
import com.mapbox.maps.CameraOptions
import com.mapbox.maps.Style
import com.mapbox.maps.plugin.annotation.annotations
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationManager
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationOptions
import com.mapbox.maps.plugin.annotation.generated.createPointAnnotationManager
import com.mapbox.maps.plugin.gestures.gestures

@SuppressLint("PermissionLaunchedDuringComposition")
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun LocationPickerScreen(
    onLocationSelected: (Point) -> Unit,
    navController: NavController
) {
    val context = LocalContext.current
    val mapView = rememberMapViewWithLifecycle()
    val annotationManager = remember { mutableStateOf<PointAnnotationManager?>(null) }
    val selectedPoint = remember { mutableStateOf<Point?>(null) }

    Box(modifier = Modifier.fillMaxSize()) {
        val permissionState = rememberMultiplePermissionsState(
            permissions = listOf(
                android.Manifest.permission.ACCESS_FINE_LOCATION,
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            )
        )

        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            if (permissionState.allPermissionsGranted){
                AndroidView(
                    factory = { mapView },
                    modifier = Modifier.fillMaxSize()
                ) { mapView ->

                    val mapboxMap = mapView.mapboxMap

                    mapboxMap.loadStyleUri(Style.MAPBOX_STREETS) {
                        val defaultPoint = Point.fromLngLat(72.45539292328507, 23.524972596013995)
                        val cameraOptions = CameraOptions.Builder()
                            .center(defaultPoint)
                            .zoom(14.5)
                            .build()
                        mapboxMap.setCamera(cameraOptions)

                        val pointAnnotationManager = mapView.annotations.createPointAnnotationManager()
                        annotationManager.value = pointAnnotationManager

                        mapView.gestures.addOnMapClickListener { geoPoint: Point ->
                            selectedPoint.value = geoPoint
                            onLocationSelected(geoPoint)

                            pointAnnotationManager.deleteAll()

                            val bitmap = BitmapFactory.decodeResource(
                                context.resources,
                                R.drawable.red_marker
                            )

                            val annotationOptions = PointAnnotationOptions()
                                .withPoint(geoPoint)
                                .withIconImage(bitmap)
                                .withIconSize(0.3)

                            pointAnnotationManager.create(annotationOptions)
                            true
                        }
                    }
                }

                Button(
                    onClick = {
                        selectedPoint.value?.let {
                            Toast.makeText(
                                context,
                                "Selected Location:\nLat: ${it.latitude()}, Lng: ${it.longitude()}",
                                Toast.LENGTH_SHORT
                            ).show()
                            navController.previousBackStackEntry
                                ?.savedStateHandle
                                ?.set("picked_latitude", it.latitude())
                            navController.previousBackStackEntry
                                ?.savedStateHandle
                                ?.set("picked_longitude", it.longitude())

                            navController.popBackStack()

                        } ?: run {
                            Toast.makeText(context, "No location selected", Toast.LENGTH_SHORT).show()
                        }
                    },
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(16.dp)
                ) {
                    Text(text = "Select Location")
                }
            }
            else{
                LaunchedEffect(Unit) {
                    permissionState.launchMultiplePermissionRequest()
                }
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(text = "Location Permission Required")
                    Button(
                        onClick = {
                            permissionState.launchMultiplePermissionRequest()
                        },
                        modifier = Modifier.padding(top = 10.dp)
                    ) {
                        Text(
                            text = "Give Permission"
                        )
                    }
                }
            }
        }
    }
}
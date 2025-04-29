package com.devkt.adminguninevigation.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.devkt.adminguninevigation.screens.nav.Routs
import com.devkt.adminguninevigation.viewModel.MyViewModel

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    viewModel: MyViewModel = hiltViewModel()
) {
    val state = viewModel.dashboard.collectAsState()

    LaunchedEffect(
        key1 = true
    ) {
        viewModel.dashboard()
    }

    when{
        state.value.isLoading -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center){
                CircularProgressIndicator()
            }
        }
        state.value.error != null -> {
            Text(text = state.value.error!!, modifier = Modifier.padding(50.dp))
        }
        state.value.data != null -> {
            val locationCount = state.value.data!!.location_count
            val subLocationCount = state.value.data!!.sub_location_count
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Hello Admin...",
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 80.dp, start = 20.dp)
                )
                Card(
                    modifier = Modifier.padding(top = 50.dp, start = 10.dp, end = 10.dp)
                        .fillMaxWidth()
                        .height(80.dp)
                        .clickable {
                            navController.navigate(Routs.ShowLocationCategoryScreen)
                        }
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "Location Categories",
                            fontSize = 25.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Total Location Categories: $locationCount",
                            modifier = Modifier.padding(top = 10.dp)
                        )
                    }
                }
                Card(
                    modifier = Modifier.padding(top = 20.dp, start = 10.dp, end = 10.dp)
                        .fillMaxWidth()
                        .height(80.dp)
                        .clickable {
                            navController.navigate(Routs.ShowAllLocationWithCategoryScreen)
                        }
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "All Location",
                            fontSize = 25.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Total Locations: $subLocationCount",
                            modifier = Modifier.padding(top = 10.dp)
                        )
                    }
                }
            }
        }
    }
}
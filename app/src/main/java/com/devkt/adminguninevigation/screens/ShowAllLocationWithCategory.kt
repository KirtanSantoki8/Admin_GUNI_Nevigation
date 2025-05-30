package com.devkt.adminguninevigation.screens

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.devkt.adminguninevigation.R
import com.devkt.adminguninevigation.screens.nav.Routs
import com.devkt.adminguninevigation.viewModel.MyViewModel
import com.mapbox.maps.logD

@Composable
fun ShowAllLocationWithCategory(
    modifier: Modifier = Modifier,
    navController: NavController,
    viewModel: MyViewModel = hiltViewModel()
) {
    val count = viewModel.getCategoryWiseLocationCount.collectAsState()
    val category = viewModel.mainLocations.collectAsState()

//    var locationCountArray = mutableListOf<Int>()
    val dataCount = category.value.data?.message?.size
    Log.d("TAG", "ShowAllLocationWithCategory: $dataCount")

    LaunchedEffect(Unit) {
        viewModel.fetchMainLocations()
    }

    when{
        count.value.isLoading -> {

        }
        count.value.error != null -> {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "No Data Found.")
            }
        }
        count.value.data != null -> {
            for (index in 0..dataCount!!-1) {
                LaunchedEffect(Unit) {
                    viewModel.categoryWiseLocationCount(category.value.data!!.message[index][1].toString())
                }
                Log.d(
                    "TAG",
                    "ShowAllLocationWithCategory: ${category.value.data!!.message[index][1]}"
                )
//                locationCountArray.add(count.value.data!!.category_wise_sub_location_data)
            }
//            Log.d(
//                "TAG",
//                "ShowAllLocationWithCategory: $locationCountArray"
//            )
        }
    }

    when{
        category.value.isLoading -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
        category.value.error != null -> {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "No Data Found.")
            }
        }
        category.value.data != null -> {
            val data = category.value.data!!.message
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 20.dp, top = 20.dp, end = 10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "All Categories wise places",
                        fontWeight = FontWeight.Bold,
                        fontSize = 22.sp,
                    )
                    Image(
                        painter = painterResource(id = R.drawable.close_sm_svgrepo_com),
                        contentDescription = null,
                        modifier = Modifier
                            .padding(start = 25.dp)
                            .height(45.dp)
                            .clickable(
                                onClick = {
                                    navController.popBackStack()
                                }
                            )
                    )
                }
                LazyColumn(
                    modifier = Modifier.padding(top = 20.dp, start = 10.dp, end = 10.dp)
                ) {
                    items(data.size) {
                        Card(
                            modifier = Modifier
                                .padding(top = 20.dp, start = 10.dp, end = 10.dp)
                                .fillMaxWidth()
                                .height(80.dp)
                                .clickable {
                                    navController.navigate(Routs.ShowAllLocationScreen(data[it][1].toString()))
                                }
                        ) {
                            Column(
                                modifier = Modifier.fillMaxSize(),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Text(
                                    text = data[it][1].toString(),
                                    fontSize = 25.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "Total Location Categories: ",
                                    modifier = Modifier.padding(top = 10.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
package com.devkt.adminguninevigation.screens.nav

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.devkt.adminguninevigation.screens.AddLocationCategory
import com.devkt.adminguninevigation.screens.AddSubLocationScreen
import com.devkt.adminguninevigation.screens.HomeScreen
import com.devkt.adminguninevigation.screens.LocationPickerScreen
import com.devkt.adminguninevigation.screens.LoginScreen
import com.devkt.adminguninevigation.screens.ShowAllLocationScreen
import com.devkt.adminguninevigation.screens.ShowAllLocationWithCategory
import com.devkt.adminguninevigation.screens.ShowLocationCategoryScreen
import com.devkt.adminguninevigation.screens.UpdateAllLocationScreen
import com.devkt.adminguninevigation.screens.UpdateLocationCategoryScreen

@Composable
fun App(modifier: Modifier = Modifier) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Routs.HomeScreen){
        composable<Routs.LoginScreen> {
            LoginScreen(navController = navController)
        }

        composable<Routs.HomeScreen> {
            HomeScreen(navController = navController)
        }

        composable<Routs.AddLocationScreen> {
            AddLocationCategory(navController = navController)
        }

        composable<Routs.AddSubLocationScreen> {
            AddSubLocationScreen(navController = navController)
        }

        composable<Routs.LocationPickerScreen> {
            LocationPickerScreen(navController = navController,onLocationSelected = {})
        }

        composable<Routs.UpdateLocationCategoryScreen> {
            val data = it.toRoute<Routs.UpdateLocationCategoryScreen>()
            UpdateLocationCategoryScreen(navController = navController,place = data.place)
        }

        composable<Routs.ShowLocationCategoryScreen> {
            ShowLocationCategoryScreen(navController = navController)
        }

        composable<Routs.ShowAllLocationScreen> {
            val data = it.toRoute<Routs.ShowAllLocationScreen>()
            ShowAllLocationScreen(navController = navController,category = data.category)
        }

        composable<Routs.UpdateAllLocationScreen> {
            val data = it.toRoute<Routs.UpdateAllLocationScreen>()
            UpdateAllLocationScreen(navController = navController, subLocationName = data.subLocationName)
        }

        composable<Routs.ShowAllLocationWithCategoryScreen> {
            ShowAllLocationWithCategory(navController = navController)
        }

    }
}
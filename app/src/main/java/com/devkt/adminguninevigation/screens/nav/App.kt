package com.devkt.adminguninevigation.screens.nav

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.PostAdd
import androidx.compose.material.icons.outlined.Category
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.PostAdd
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
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
import com.devkt.adminguninevigation.screens.SuggestionScreen
import com.devkt.adminguninevigation.screens.UpdateAllLocationScreen
import com.devkt.adminguninevigation.screens.UpdateLocationCategoryScreen

@Composable
fun App(modifier: Modifier = Modifier) {
    val navController = rememberNavController()

    val bottomNavItems = listOf(
        BottomNavItem(
            name = "Home",
            selectedIcon = Icons.Filled.Home,
            unselectedIcon = Icons.Outlined.Home,
            route = Routs.HomeScreen
        ),
        BottomNavItem(
            name = "AddCategories",
            selectedIcon = Icons.Filled.Category,
            unselectedIcon = Icons.Outlined.Category,
            route = Routs.AddLocationScreen
        ),
        BottomNavItem(
            name = "AddLocation",
            selectedIcon = Icons.Filled.LocationOn,
            unselectedIcon = Icons.Outlined.LocationOn,
            route = Routs.AddSubLocationScreen
        ),
        BottomNavItem(
            name = "Suggestions",
            selectedIcon = Icons.Filled.PostAdd,
            unselectedIcon = Icons.Outlined.PostAdd,
            route = Routs.SuggestionScreen
        )
    )

    var selectedItemIndex by rememberSaveable { mutableIntStateOf(0) }

    DisposableEffect(navController) {
        val listener = NavController.OnDestinationChangedListener { _, destination, _ ->
            val currentRoute = destination.route
            if (currentRoute != null) {
                val index = bottomNavItems.indexOfFirst { navItem ->
                    navItem.route::class.qualifiedName == currentRoute ||
                            navItem.route::class.simpleName == currentRoute
                }
                if (index != -1) {
                    selectedItemIndex = index
                }
            }
        }
        navController.addOnDestinationChangedListener(listener)

        onDispose {
            navController.removeOnDestinationChangedListener(listener)
        }
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        bottomBar = {
            NavigationBar {
                bottomNavItems.forEachIndexed { index, item ->
                    NavigationBarItem(
                        selected = selectedItemIndex == index,
                        onClick = {
                            navController.navigate(item.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = {
                            Icon(
                                imageVector = if (selectedItemIndex == index) item.selectedIcon else item.unselectedIcon,
                                contentDescription = item.name
                            )
                        },
                        label = { Text(item.name) }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Routs.HomeScreen,
            modifier = Modifier.padding(innerPadding)
        ) {
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
                LocationPickerScreen(navController = navController, onLocationSelected = {})
            }

            composable<Routs.UpdateLocationCategoryScreen> {
                val data = it.toRoute<Routs.UpdateLocationCategoryScreen>()
                UpdateLocationCategoryScreen(navController = navController, place = data.place)
            }

            composable<Routs.ShowLocationCategoryScreen> {
                ShowLocationCategoryScreen(navController = navController)
            }

            composable<Routs.ShowAllLocationScreen> {
                val data = it.toRoute<Routs.ShowAllLocationScreen>()
                ShowAllLocationScreen(navController = navController, category = data.category)
            }

            composable<Routs.UpdateAllLocationScreen> {
                val data = it.toRoute<Routs.UpdateAllLocationScreen>()
                UpdateAllLocationScreen(
                    navController = navController,
                    subLocationName = data.subLocationName
                )
            }

            composable<Routs.ShowAllLocationWithCategoryScreen> {
                ShowAllLocationWithCategory(navController = navController)
            }

            composable<Routs.SuggestionScreen> {
                SuggestionScreen(navController = navController)
            }
        }
    }
}

data class BottomNavItem(
    val name: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val route: Any
)
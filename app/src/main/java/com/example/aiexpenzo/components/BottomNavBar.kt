package com.example.aiexpenzo.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.aiexpenzo.R


@Composable
fun BottomNavBar(
    navController: NavController,
    modifier: Modifier = Modifier

){
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    NavigationBar(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White),
        containerColor = Color.White,
            tonalElevation = 0.dp,
        ) {
            BottomNavBarItem.values().forEach { item ->
                NavigationBarItem(
                    selected = currentRoute == item.route,
                    onClick = {
                        if (currentRoute != item.route){
                            navController.navigate(item.route){
                                popUpTo(navController.graph.startDestinationId){ saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    },
                    icon = {
                        val iconId = when (item){
                            BottomNavBarItem.Home -> R.drawable.navbarhome
                            BottomNavBarItem.Expenses -> R.drawable.navbarexpenselog
                            BottomNavBarItem.Analyzer -> R.drawable.navbaranalyser
                            BottomNavBarItem.Profile -> R.drawable.navbarprofile
                        }
                        Image(
                            painter = painterResource(id = iconId),
                            contentDescription = item.label,
                            modifier = Modifier.size(30.dp),
                            alpha = if(currentRoute == item.route) 1f else 0.6f
                        )
                    },
                    label = { Text(item.label)},
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color.Unspecified,
                        unselectedIconColor = Color.Unspecified,
                        selectedTextColor = Color.Black,
                        unselectedTextColor = Color.Gray,
                        indicatorColor = Color.Transparent
                    )

                )
            }
        }

}
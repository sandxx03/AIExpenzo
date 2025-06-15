package com.example.aiexpenzo.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.aiexpenzo.R

enum class NavBarItem(val label: String){
    Home("Home"),
    Expenses("Expenses"),
    Analyzer("AI Analyzer"),
    Profile("Profile")
}

@Composable
fun BottomNavBar(
    selected: NavBarItem,
    onItemSelected: (NavBarItem) -> Unit,
    modifier: Modifier = Modifier

){
    NavigationBar(
        modifier = Modifier.fillMaxWidth()
            .background(Color.White),
        tonalElevation = 0.dp
    ) {
        NavigationBarItem(
            selected = selected == NavBarItem.Home,
            onClick = {onItemSelected(NavBarItem.Home)},
            icon = {
                Image(
                    painter= painterResource(id = R.drawable.navbarhome),
                    contentDescription = "Home",
                    modifier = Modifier.size(30.dp)
                )
            },
            label = {Text("Home")}
        )
        NavigationBarItem(
            selected = selected == NavBarItem.Expenses,
            onClick = {onItemSelected(NavBarItem.Expenses)},
            icon = {
                Image(
                    painter = painterResource(id = R.drawable.navbarexpenselog),
                    contentDescription = "Expenses",
                    modifier = Modifier.size(30.dp)
                )
            },
            label = {Text("Expenses")}
        )
        NavigationBarItem(
            selected = selected == NavBarItem.Analyzer,
            onClick = {onItemSelected(NavBarItem.Analyzer)},
            icon = {
                Image(
                    painter = painterResource(id = R.drawable.navbaranalyser),
                    contentDescription = "AI Analyzer",
                    modifier = Modifier.size(30.dp)
                )
            },
            label = {Text("Analyzer")}
        )
        NavigationBarItem(
            selected = selected == NavBarItem.Profile,
            onClick = {onItemSelected(NavBarItem.Profile)},
            icon = {
                Image(
                    painter = painterResource(id = R.drawable.navbarprofile),
                    contentDescription = "Profile",
                    modifier = Modifier.size(30.dp)
                )
            },
            label = {Text("Profile")}
        )
    }
}
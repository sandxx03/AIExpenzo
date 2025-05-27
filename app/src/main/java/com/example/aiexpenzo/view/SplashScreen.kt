package com.example.aiexpenzo.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.aiexpenzo.R
import kotlinx.coroutines.delay


@Composable
fun SplashScreen(navController: NavController){
    LaunchedEffect(Unit) {
        delay(3000L)
        navController.navigate("onboarding") {
            popUpTo("splash") { inclusive = true }
        }
    }
    Box(modifier = Modifier
        .fillMaxSize()
        .background(Color.White),
        contentAlignment = Alignment.Center

    ){
        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = "Logo",
            modifier = Modifier.size(700.dp)
        )


    }

}

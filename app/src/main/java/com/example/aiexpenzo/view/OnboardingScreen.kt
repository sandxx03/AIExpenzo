package com.example.aiexpenzo.view

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.TopAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.aiexpenzo.R
import com.example.aiexpenzo.components.AppTopBar
import com.example.aiexpenzo.components.BottomNavBar
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.rememberPagerState

data class OnboardingPage(val text: String, val imageRes: Int)

@Composable
fun OnboardingScreen(navController: NavController) {
    val pagerState = rememberPagerState()

    val pages = listOf(
        OnboardingPage("Effortlessly log and track expenses.", R.drawable.onboardingimage1),
        OnboardingPage("Get the insights you need to make smarter financial decisions.", R.drawable.onboardingimage2),
        OnboardingPage("Predict and plan your future expenses.", R.drawable.onboardingimage3)
    )

    Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .statusBarsPadding(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(50.dp))
            Text(
                text = "Welcome to AIExpenzo!",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                color = colorResource(R.color.navyblue),
            )

            Spacer(modifier = Modifier.height(30.dp))

            Text(
                text = "Get Started!",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Medium,
                color = colorResource(R.color.navyblue)
            )

            Spacer(modifier = Modifier.height(10.dp))

            HorizontalPager(
                count = pages.size,
                state = pagerState,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) { page ->
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 24.dp)
                ) {
                    Image(
                        painter = painterResource(id = pages[page].imageRes),
                        contentDescription = "Onboarding Image",
                        modifier = Modifier.size(300.dp)
                    )
                    Spacer(modifier = Modifier.height(30.dp))
                    Text(
                        text = pages[page].text,
                        style = MaterialTheme.typography.bodyLarge,
                        color = Color.Gray,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(horizontal = 24.dp)
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                    DotsIndicator(totalDots = pages.size, selectedIndex = page, pagerState = pagerState)
                }
            }

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Button(
                    onClick = {navController.navigate("signup")},
                    shape = RoundedCornerShape(10.dp),
                    elevation = ButtonDefaults.buttonElevation(
                        defaultElevation = 8.dp,
                        pressedElevation = 12.dp,
                        focusedElevation = 4.dp
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 32.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = colorResource(R.color.navyblue)
                    )
                ) {
                    Text(
                        "SIGN UP",
                        color = colorResource(R.color.white),
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Button(
                    onClick = {navController.navigate("login")},
                    shape = RoundedCornerShape(10.dp),
                    elevation = ButtonDefaults.buttonElevation(
                        defaultElevation = 8.dp,
                        pressedElevation = 12.dp,
                        focusedElevation = 4.dp
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 32.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = colorResource(R.color.lightblue),
                        contentColor = colorResource(R.color.navyblue)
                    )
                ) {
                    Text(
                        "LOGIN",
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(90.dp))
            }
        }


}

@Composable
fun DotsIndicator(totalDots: Int, selectedIndex: Int, pagerState: PagerState) {
    val navyBlue = colorResource(R.color.navyblue)
    Row(horizontalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth()) {
        repeat(totalDots) { index ->
            val isSelected = index == pagerState.currentPage
            val animatedFraction by animateFloatAsState(
                targetValue = if (isSelected) 1f else 0f,
                label = "dot-color"
            )
            val dotColor = lerp(Color.LightGray, navyBlue, animatedFraction)

            Box(
                modifier = Modifier
                    .padding(4.dp)
                    .size(8.dp)
                    .clip(CircleShape)
                    .background(dotColor)
            )
        }
    }
}

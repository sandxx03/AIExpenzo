package com.example.aiexpenzo.view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.aiexpenzo.R
import com.example.aiexpenzo.components.AppGuideSection
import com.example.aiexpenzo.components.AppTopBar
import com.example.aiexpenzo.components.BottomNavBar
import com.example.aiexpenzo.data.constants.AppGuideContentList

@Composable
fun AppGuideScreen(
    navController: NavController
){
    val expandedCardIndex = remember { mutableStateOf<Int?>(null) }
    val appGuideContent = AppGuideContentList
    val scrollState = rememberScrollState()


    Scaffold (
        topBar = { AppTopBar(title = "App Guide") },
        bottomBar = { BottomNavBar(navController) }
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 15.dp)
                .verticalScroll(scrollState)
        ) {
            Spacer(modifier = Modifier.height(8.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                // Back button and header
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back"
                    )
                }
                Spacer(Modifier.width((6.dp)))

                Text(
                    text = "Back to Profile",
                    fontWeight = FontWeight.Bold,
                    fontSize = 22.sp,
                    color = colorResource(R.color.navyblue)
                )

            }
            Spacer(modifier = Modifier.height(36.dp))
            Column( modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Welcome to AIExpenzo — Your Smart Expense Tracker\n",
                    textAlign = TextAlign.Justify,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    color = colorResource(R.color.navyblue)
                )
                Text(
                    text = "AIExpenzo is a modern, AI-powered personal finance app designed to help you track your spending effortlessly, analyze your habits, and save smarter. Whether you're manually logging expenses or scanning QR payment statements, AIExpenzo turns every transaction into meaningful insights.",
                    textAlign = TextAlign.Justify,
                    fontSize = 15.sp,
                    color = colorResource(R.color.navyblue)

                    )
                Spacer(modifier = Modifier.height(36.dp))

                appGuideContent.forEachIndexed{ index, sec ->
                    AppGuideSection(
                        sec = sec,
                        isExpanded = expandedCardIndex.value == index,
                        onCardClick = {
                            expandedCardIndex.value =
                                if (expandedCardIndex.value == index) null else index
                        }
                    )
                }
            }





        }
    }
}

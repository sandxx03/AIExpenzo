package com.example.aiexpenzo.view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
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



    Scaffold (
        topBar = { AppTopBar(title = "App Guide") },
        bottomBar = { BottomNavBar(navController) }
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 15.dp)
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
                    text = "App Guide",
                    fontWeight = FontWeight.Bold,
                    fontSize = 22.sp,
                    color = colorResource(R.color.navyblue)
                )

            }
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

@Preview(showBackground = true)
@Composable
fun AppGuideScreenPreview(){
    AppGuideScreen(
        navController = rememberNavController()
    )
}
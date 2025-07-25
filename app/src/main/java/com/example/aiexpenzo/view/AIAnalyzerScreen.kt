package com.example.aiexpenzo.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.aiexpenzo.R
import com.example.aiexpenzo.components.AiResponseCard
import com.example.aiexpenzo.components.AppTopBar
import com.example.aiexpenzo.components.BottomNavBar
import com.example.aiexpenzo.viewmodel.AiAnalyzerViewModel
import com.example.aiexpenzo.viewmodel.ExpenseViewModel

@Composable
fun AIAnalyzerScreen(
    navController: NavController,
    aiViewModel: AiAnalyzerViewModel = viewModel(),
    expenseViewModel: ExpenseViewModel = viewModel()
) {
    val expenses by expenseViewModel.allExpense.collectAsState(initial = emptyList())
    val isLoading by aiViewModel.isLoading
    val analysisList by aiViewModel.result

    val isNoExpenseState = analysisList?.size == 1
    val headers = if (isNoExpenseState){
        listOf("Log more expenses to enjoy this feature.")
    } else{
        listOf(
            "Category Breakdown & Spending Patterns",
            "Type of Spender",
            "Tips & Advice"
        )
    }

    Scaffold(
        topBar = { AppTopBar(title = "AI Analyzer") },
        bottomBar = { BottomNavBar(navController) },

        ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(Color.White

                )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 24.dp, vertical = 16.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Header
                Text(
                    text = "Let AIExpenzo analyze your spendings.",
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    color = colorResource(R.color.navyblue),
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 12.dp, bottom = 8.dp)

                )
                Spacer(modifier = Modifier.height(8.dp))
                Card(
                    shape = RoundedCornerShape(20.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                    colors = CardDefaults.cardColors(containerColor = colorResource(R.color.lightblue)),
                    modifier = Modifier
                        .padding(horizontal = 14.dp)
                        .padding(top = 4.dp, bottom = 12.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)  // Inner padding to space text from edges
                    ) {
                        Text(
                            text = "Discover your expense breakdown, spending patterns, and spender type, along with tips to help you save smarter.",
                            fontWeight = FontWeight.Normal,
                            fontSize = 14.sp,
                            color = colorResource(R.color.navyblue),
                            textAlign = TextAlign.Justify,

                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "We recommend logging at least 7 days of expense for a more insightful analysis.",
                            fontWeight = FontWeight.Normal,
                            fontSize = 14.sp,
                            color = colorResource(R.color.navyblue),
                            textAlign = TextAlign.Justify,

                        )

                    }

                }

                Spacer(modifier = Modifier.height(18.dp))

                Text(
                    text = "Click the button to start.",
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = colorResource(R.color.navyblue),
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 12.dp, bottom = 8.dp)

                )
                Spacer(modifier = Modifier.height(25.dp))

                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {

                    Button(
                        onClick = {aiViewModel.analyze(expenses, timeRange = "Overall")},
                        shape = CircleShape,
                        modifier = Modifier.size(140.dp)
                            .shadow(8.dp, CircleShape),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = colorResource(R.color.navyblue)
                        )
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_analyzer),
                            contentDescription = "Analyze",
                            modifier = Modifier.fillMaxSize()
                                .background(color = Color.Transparent)
                        )

                    }
                }
                Spacer(modifier = Modifier.height(70.dp))

                if (isLoading) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxSize()
                            .background(Color.Gray.copy(alpha = 0.3f)),
                        contentAlignment = Alignment.Center
                    ){
                        CircularProgressIndicator(color = colorResource(R.color.navyblue))
                    }
                } else {
                    if (analysisList != null){
                        analysisList!!.forEachIndexed { idx, section ->
                            AiResponseCard(
                                header = headers.getOrNull(idx) ?: "Insight",
                                body = section
                            )
                        }
                    }

                }
            }
        }
    }
}

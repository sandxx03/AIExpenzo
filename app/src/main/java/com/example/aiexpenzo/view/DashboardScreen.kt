package com.example.aiexpenzo.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import com.example.aiexpenzo.components.BottomNavBar
import com.example.aiexpenzo.data.constants.CategoryIconMap
import com.example.aiexpenzo.viewmodel.AuthViewModel
import com.example.aiexpenzo.viewmodel.ExpenseViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


@Composable
fun DashboardScreen(
    navController: NavController,
    authViewModel: AuthViewModel = viewModel(),
    expenseViewModel: ExpenseViewModel = viewModel(),



){

    val user by authViewModel.currentUser.collectAsState()
    val name = user?.name ?: ""
    val monthlyIncome = user?.monthlyIncome?: 0f
    val monthlyBudget = user?.monthlyBudget ?: 0f

    var selectedMonth by remember { mutableStateOf(Calendar.getInstance()) }

    val currentMonth = selectedMonth.get(Calendar.MONTH)
    val currentYear = selectedMonth.get(Calendar.YEAR)

    val moneyOut = expenseViewModel.getTotalExpensesForMonth(currentMonth, currentYear)
    val currentUsed = moneyOut
    val topCategories = expenseViewModel.getTopCategories(currentMonth,currentYear)

    // Ratios for progress bar
    val monthlyIncomeRatio = if (monthlyIncome > 0) ((monthlyIncome - moneyOut) / monthlyIncome).coerceIn(0f, 1f) else 0f // moneyIn
    val moneyOutRatio = if (monthlyIncome > 0) (moneyOut / monthlyIncome).coerceIn(0f, 1f) else 0f //moneyOut



    val scrollState = rememberScrollState()

    Scaffold (
        bottomBar = { BottomNavBar(navController)},

    ){ innerPadding ->
        Column(
            modifier = Modifier.fillMaxSize()
                .padding(innerPadding)
                .background(Color.White)
        ){

            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxSize()
                    .verticalScroll(scrollState)


            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth()
                        .padding(horizontal = 24.dp, vertical = 16.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            "Hi, $name",
                            fontWeight = FontWeight.Bold,
                            fontSize = 22.sp,
                            color = colorResource(R.color.navyblue)
                        )
                        Text("Welcome Back", fontSize = 15.sp, color = colorResource(R.color.navyblue))

                    }

                    //Month selector

                    Row(
                        horizontalArrangement = Arrangement.End
                    ){
                        IconButton(onClick = {
                            selectedMonth = (selectedMonth.clone() as Calendar).apply {
                                add(Calendar.MONTH, -1)}
                        }) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Previous Month")
                        }

                        Text(
                            text = SimpleDateFormat("MMM yyyy", Locale.getDefault()).format(selectedMonth.time),
                            fontWeight = FontWeight.Bold,
                            fontSize = 25.sp,
                            color = colorResource(R.color.navyblue)
                        )

                        IconButton(onClick = {
                            selectedMonth = (selectedMonth.clone() as Calendar).apply {
                                add(Calendar.MONTH, 1) }
                        }) {
                            Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = "Next Month")
                        }
                    }
                }

                // Money In/Out & progress bar
                Row(
                    modifier = Modifier.fillMaxWidth()
                        .padding(horizontal = 24.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        Text("Money In", fontSize = 12.sp, color = colorResource(R.color.navyblue))
                        Text(
                            "$${String.format("%,.2f", monthlyIncome)}",
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp,
                            color = colorResource(R.color.navyblue)
                        )

                    }
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        Text("Money Out", fontSize = 12.sp, color = colorResource(R.color.navyblue))
                        Text(
                            "-$${String.format("%,.2f", moneyOut)}",
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp,
                            color = Color.Red

                        )

                    }

                }

                // Progress bar with percent labels
                Column(
                    Modifier.padding(horizontal = 24.dp, vertical = 10.dp),

                    ) {


                    Box(
                        Modifier.fillMaxWidth()
                            .height(32.dp)
                            .background(colorResource(R.color.navyblue), shape = RoundedCornerShape(24.dp))

                    ) {
                        // Money In (navy blue) part
                        Box(
                            modifier = Modifier
                                .fillMaxHeight()
                                .fillMaxWidth(monthlyIncomeRatio)
                                .background(
                                    colorResource(R.color.navyblue),
                                    shape = RoundedCornerShape(24.dp)
                                )
                        )

                        // Money Out (red) part
                        Box(
                            modifier = Modifier
                                .fillMaxHeight()
                                .fillMaxWidth(moneyOutRatio)
                                .align(Alignment.CenterEnd)
                                .background(colorResource(R.color.darkred), shape = RoundedCornerShape(24.dp))
                        )

                        // Labels on top
                        Row(
                            modifier = Modifier.fillMaxSize(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                "${(monthlyIncomeRatio * 100).toInt()}%",
                                color = Color.White,
                                fontSize = 12.sp,
                                modifier = Modifier.padding(start = 10.dp)
                            )
                            Text(
                                "${(moneyOutRatio * 100).toInt()}%",
                                color = Color.White,
                                fontSize = 12.sp,
                                modifier = Modifier.padding(end = 10.dp)

                            )
                        }
                    }
                    Text(
                        text = if (moneyOutRatio < 0.5)"Only ${(moneyOutRatio * 100).toInt()}%  of your income used, not bad!" else "Careful, you've used ${(moneyOutRatio * 100).toInt()}% of your income!",
                        fontSize = 12.sp,
                        color = if (moneyOutRatio <= 0.8f) colorResource(R.color.navyblue) else Color.Red,
                        modifier = Modifier.align(Alignment.CenterHorizontally).padding(top = 2.dp)
                    )
                }

                // Monthly Budget Card
                Column(
                    modifier = Modifier.fillMaxSize()
                        .padding(top = 20.dp, start = 24.dp, end = 24.dp, bottom = 16.dp)
                ) {

                    Card(
                        shape = RoundedCornerShape(20.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                        colors = CardDefaults.cardColors(containerColor = colorResource(R.color.lightblue)),
                        modifier = Modifier.fillMaxWidth()
                            .padding(bottom = 16.dp)
                    ) {
                        Row(
                            Modifier.padding(20.dp),
                            verticalAlignment = Alignment.CenterVertically,

                            ) {
                            // Placeholder for circle icon
                            Box(
                                modifier = Modifier.weight(0.8f)
                                    .background(Color.Transparent),
                                contentAlignment = Alignment.Center
                            ) {
                                Image(
                                    painter = painterResource(R.drawable.dashboardmonthlybudget),
                                    contentDescription = null,
                                    modifier = Modifier.size(80.dp)

                                )

                            }

                            Spacer(modifier = Modifier.width(16.dp))
                            Column(
                                modifier = Modifier.weight(1f)
                                    .padding(start =16.dp, end = 8.dp)
                            ){
                                Text(
                                    "Monthly Budget",
                                    fontSize = 13.sp,
                                    color = colorResource(R.color.navyblue)
                                )
                                Text(
                                    "$${String.format("%,.2f", monthlyBudget)}",
                                    color = colorResource(R.color.navyblue),
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp
                                )
                                Divider(color = colorResource(R.color.navyblue))
                                Spacer(Modifier.height(2.dp))
                                Text(
                                    "Current Used",
                                    fontSize = 12.sp,
                                    color = colorResource(R.color.navyblue)
                                )
                                Text(
                                    "$${String.format("%,.2f", currentUsed)}",
                                    color = if (currentUsed > monthlyBudget) Color.Red else colorResource(R.color.navyblue),
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp

                                )
                            }

                        }
                    }

                }

                // Card background for lower section
                Box(
                    modifier = Modifier.fillMaxWidth()
                        .weight(1f)
                        .background(
                            colorResource(R.color.navyblue),
                            shape = RoundedCornerShape(topStart = 40.dp, topEnd = 40.dp)
                        )
                        .padding(horizontal = 20.dp),
                    contentAlignment = Alignment.TopCenter
                ) {

                    Column(
                        modifier = Modifier.fillMaxSize()
                            .padding(top = 40.dp, start = 24.dp, end = 24.dp, bottom = 16.dp)
                    ) {
                        Text(

                            text = "This Month's Most Spent Categories",
                            color = colorResource(R.color.lightblue),
                            fontWeight = FontWeight.Bold,
                            fontSize = 19.sp,

                            )

                        // Top 3 categories with placeholders

                        Column(Modifier.fillMaxWidth()
                            .padding(top = 20.dp)) {
                            if (topCategories.isEmpty()){
                                Column(
                                    modifier = Modifier.fillMaxWidth()
                                        .padding(vertical = 40.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                ) {

                                    Text(
                                        text = "No expenses added yet.",
                                        color = Color.LightGray,
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.Medium
                                    )
                                }
                            } else{

                                topCategories.forEachIndexed { index, cat ->
                                    Row(
                                        Modifier.fillMaxWidth()
                                            .padding(vertical = 10.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = "${index + 1}.",
                                            color = Color.White,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 16.sp,
                                            modifier = Modifier.padding(end = 8.dp)
                                        )
                                        val iconRes = CategoryIconMap[cat.title] ?: R.drawable.ic_other

                                        Image(
                                            painter = painterResource(id = iconRes),
                                            contentDescription = cat.title,
                                            modifier = Modifier
                                                .size(55.dp)
                                                .background(Color.Transparent,
                                                    shape = RoundedCornerShape(10.dp))
                                                .padding(8.dp)
                                        )
                                        Spacer(Modifier.width(12.dp))
                                        Column(
                                            modifier = Modifier.weight(1f)
                                        ) {
                                            Text(cat.title, color = Color.White, fontWeight = FontWeight.Bold)

                                        }
                                        Text(
                                            "$${String.format("%.2f", cat.amount)}",
                                            color = Color.White,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 15.sp,
                                            modifier = Modifier.padding(start = 4.dp)
                                        )

                                    }
                                    Divider(color = Color.White.copy(alpha = 0.2f))
                                }
                            }

                        }
                        Spacer(modifier = Modifier.height(70.dp))

                    }


                }




            }



        }
    }




}

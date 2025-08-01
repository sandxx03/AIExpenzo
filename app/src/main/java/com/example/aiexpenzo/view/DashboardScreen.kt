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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.aiexpenzo.R
import com.example.aiexpenzo.components.AppTopBar
import com.example.aiexpenzo.components.BottomNavBar
import com.example.aiexpenzo.components.CategoriesBarChart
import com.example.aiexpenzo.components.EditValueDialog
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

    var showEditIncomeDialog by remember { mutableStateOf(false) }
    var showEditBudgetDialog by remember { mutableStateOf(false) }

    val monthFormatter = remember { SimpleDateFormat("MMM yyyy", Locale.getDefault()) }
    var selectedMonth by remember { mutableStateOf(Calendar.getInstance()) }
    val currentMonth = selectedMonth.get(Calendar.MONTH)
    val currentYear = selectedMonth.get(Calendar.YEAR)
    val monthlyIncome by remember(user, selectedMonth){
        derivedStateOf {
            authViewModel.getCurrentMonthIncome(currentMonth, currentYear)
        }
    }
    val monthlyBudget by remember(user, selectedMonth){
        derivedStateOf {
            authViewModel.getCurrentMonthBudget(currentMonth, currentYear)
        }
    }
    val moneyOut = expenseViewModel.getTotalExpensesForMonth(currentMonth, currentYear)
    val currentUsed = moneyOut
    val topCategories = expenseViewModel.getTopCategories(currentMonth,currentYear)
    val allCategories = expenseViewModel.getAllCategories(currentMonth, currentYear)

    // Ratios for progress bar
    val monthlyIncomeRatio = if (monthlyIncome > 0) ((monthlyIncome - moneyOut) / monthlyIncome).coerceIn(0f, 1f) else 0f // moneyIn
    val moneyOutRatio = if (monthlyIncome > 0) (moneyOut / monthlyIncome).coerceIn(0f, 1f) else 0f //moneyOut


    val scrollState = rememberScrollState()
    val isLoading by authViewModel.isLoading.collectAsState()

    if (isLoading){
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Gray.copy(alpha = 0.3f)),
            contentAlignment = Alignment.Center
        ){
            CircularProgressIndicator(color = colorResource(R.color.navyblue))
        }
    }

    LaunchedEffect(isLoading) {
        if(!isLoading){
            showEditIncomeDialog = false
            showEditBudgetDialog = false
        }
    }


    Scaffold (
        topBar = { AppTopBar(title = "Home") },
        bottomBar = { BottomNavBar(navController)}

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
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
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
                        verticalAlignment = Alignment.CenterVertically
                    ){
                        IconButton(onClick = {
                            selectedMonth = (selectedMonth.clone() as Calendar).apply {
                                add(Calendar.MONTH, -1)}
                        }) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Previous Month")
                        }

                        Box(
                            modifier = Modifier.background(Color(0xFF113A49), shape = CircleShape)
                                .padding(horizontal = 14.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = monthFormatter.format(selectedMonth.time),
                                color = Color.White,
                                fontSize = 14.sp
                            )
                        }

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
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {

                        Row(verticalAlignment = Alignment.CenterVertically,

                        ) {
                            // Edit button
                            IconButton(
                                onClick = {showEditIncomeDialog = true},
                                modifier = Modifier.size(30.dp)) {
                                Icon(
                                    painter = painterResource(R.drawable.ic_edit),
                                    contentDescription = "Edit Income",
                                    tint = colorResource(R.color.navyblue),
                                    modifier = Modifier.size(25.dp)
                                        .padding(end = 8.dp )
                                )
                            }

                            Text("Money In", fontSize = 12.sp, color = colorResource(R.color.navyblue))


                        }
                        if (showEditIncomeDialog){
                            EditValueDialog(
                                title = "Edit Monthly Income",
                                currentValue = monthlyIncome,
                                onConfirm = {
                                    authViewModel.setMonthlyIncome(currentMonth, currentYear, it)
                                    showEditIncomeDialog = false
                                    // Force state update
                                    selectedMonth = selectedMonth.clone() as Calendar
                                },
                                onDismiss = { if (!isLoading) showEditIncomeDialog = false},

                            )
                        }


                        Text(
                            "RM${String.format("%,.2f", monthlyIncome)}",
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp,
                            color = colorResource(R.color.navyblue)
                        )

                    }
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Text("Money Out", fontSize = 12.sp, color = colorResource(R.color.navyblue))

                        Text(
                            "-RM${String.format("%,.2f", moneyOut)}",
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
                        .padding(start = 24.dp, end = 24.dp)
                ) {

                    Card(
                        shape = RoundedCornerShape(20.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                        colors = CardDefaults.cardColors(containerColor = colorResource(R.color.lightblue)),
                        modifier = Modifier.fillMaxWidth()
                            .padding(bottom = 8.dp)
                    ) {
                        Row(
                            Modifier.padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically,

                            ) {
                            // circle icon
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
                                Row (verticalAlignment = Alignment.CenterVertically){
                                    // Edit button
                                    IconButton(onClick = {showEditBudgetDialog = true},
                                        modifier = Modifier.size(30.dp)) {
                                        Icon(
                                            painter = painterResource(R.drawable.ic_edit),
                                            contentDescription = "Edit Income",
                                            tint = colorResource(R.color.navyblue),
                                            modifier = Modifier
                                                .size(25.dp)
                                                .padding(end = 8.dp)
                                        )
                                    }
                                    Text(
                                        "Monthly Budget",
                                        fontSize = 13.sp,
                                        color = colorResource(R.color.navyblue)
                                    )


                                }

                                if (showEditBudgetDialog) {
                                    EditValueDialog(
                                        title = "Edit Monthly Budget",
                                        currentValue = monthlyBudget,
                                        onConfirm = { authViewModel.setMonthlyBudget(currentMonth, currentYear, it)},
                                        validate = {authViewModel.isBudgetValid(currentMonth, currentYear, it)},
                                        errorMessage = "Budget cannot exceed income.",
                                        onDismiss = { if (!isLoading) showEditBudgetDialog = false }

                                    )
                                }

                                Text(
                                    "RM${String.format("%,.2f", monthlyBudget)}",
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
                                    "RM${String.format("%,.2f", currentUsed)}",
                                    color = if (currentUsed > monthlyBudget) Color.Red else colorResource(R.color.navyblue),
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp

                                )
                            }

                        }
                    }

                }
                // Top 3 Categories Card
                Card(
                    shape = RoundedCornerShape(20.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                    colors = CardDefaults.cardColors(containerColor = colorResource(R.color.navyblue)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp, vertical = 8.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp)
                    ) {
                        Text(
                            text = "This Month's Top 3 Categories",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(16.dp))

                        if (topCategories.isEmpty()) {
                            Text(
                                text = "No expenses added yet.",
                                color = Color.LightGray,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Medium,
                                modifier = Modifier.align(Alignment.CenterHorizontally)
                            )
                        } else {
                            topCategories.forEach { cat ->
                                Row(
                                    Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 10.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    val iconRes = CategoryIconMap[cat.title] ?: R.drawable.ic_other

                                    Image(
                                        painter = painterResource(id = iconRes),
                                        contentDescription = cat.title,
                                        modifier = Modifier
                                            .size(50.dp)
                                            .padding(8.dp),
                                        colorFilter = ColorFilter.tint(colorResource(R.color.lightblue))
                                    )
                                    Spacer(Modifier.width(12.dp))
                                    Column(
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Text(cat.title, color = Color.White, fontWeight = FontWeight.Bold)
                                    }
                                    Text(
                                        "RM${String.format("%.2f", cat.amount)}",
                                        color = Color.White,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 15.sp
                                    )
                                }
                                Divider(color = Color.White.copy(alpha = 0.2f))
                            }
                        }
                    }
                }
                // Bar Chart Card
                Card (
                    shape = RoundedCornerShape(20.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.LightGray),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp)
                        .padding(top = 4.dp, bottom = 12.dp)
                ){
                    Column(
                        modifier = Modifier.padding(20.dp)
                    ) {
                        Text(
                            text = "This Month's Category Spendings",
                            color = colorResource(R.color.navyblue),
                            fontWeight = FontWeight.Bold,
                            fontSize = 19.sp,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(16.dp))

                        if (allCategories.isNotEmpty()) {
                            CategoriesBarChart(
                                categories = allCategories,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(300.dp)
                            )

                        } else {
                            Text(
                                text = "No data to display.",
                                color = colorResource(R.color.navyblue),
                                fontSize = 14.sp
                            )
                        }
                    }
                }
            }
        }
    }
}

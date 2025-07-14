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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
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
import androidx.navigation.NavController
import com.example.aiexpenzo.R
import com.example.aiexpenzo.components.AppTopBar
import com.example.aiexpenzo.components.BottomNavBar
import com.example.aiexpenzo.components.DailyLineChart
import com.example.aiexpenzo.components.ExpenseListItem
import com.example.aiexpenzo.components.SheetOption
import com.example.aiexpenzo.viewmodel.ExpenseViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpenseListScreen(
    navController: NavController,
    viewModel: ExpenseViewModel,
) {
    val expenses by viewModel.allExpense.collectAsState()

    var showAddOptions by remember { mutableStateOf(false) }
    var selectedMonth by remember { mutableStateOf(Calendar.getInstance()) }
    val monthFormatter = remember { SimpleDateFormat("MMM yyyy", Locale.getDefault()) }

    val expensesByDate = remember(selectedMonth) {
        viewModel.getExpensesForMonth(
            month = selectedMonth.get(Calendar.MONTH),
            year = selectedMonth.get(Calendar.YEAR)
        )
    }

    val expensesExist = expensesByDate.isNotEmpty()
    val dailyTotals = viewModel.getDailyTotalsForMonth(
        month = selectedMonth.get(Calendar.MONTH),
        year = selectedMonth.get(Calendar.YEAR)
    )


    Scaffold (
        topBar = { AppTopBar(title = "Expense Log") },
        bottomBar = {BottomNavBar(navController)}
    ){ innerPadding ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(innerPadding)

        ) {
            Column(
                modifier = Modifier
                    .fillMaxHeight()    // not fillMaxSize() because will exceed bounds if innerPadding not respected

            ) {

                // Header
                //Month selector
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp, vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
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

                // Line Chart
                Box(
                    modifier = Modifier.fillMaxWidth()
                        .height(250.dp)
                        .padding(horizontal = 30.dp)
                        .background(Color.White),
                    contentAlignment = Alignment.Center
                ) {
                    DailyLineChart(
                        modifier = Modifier.fillMaxWidth()
                            .height(250.dp)
                            .padding(horizontal = 16.dp),
                        dailyTotals = dailyTotals
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                if (!expensesExist) {
                    // If there are no expenses added (empty)
                    Column(
                        modifier = Modifier.fillMaxWidth()
                            .padding(vertical = 40.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.noexpensesadded),
                            contentDescription = "No Expenses",
                            modifier = Modifier.height(120.dp)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "No expenses added yet.",
                            color = Color.LightGray,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                } else {

                    // If expenses are added
                    LazyColumn(
                        modifier = Modifier.fillMaxSize()
                    ) {

                        expensesByDate.forEach { (date, expenses) ->
                            item {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 8.dp, horizontal = 16.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = date,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = colorResource(R.color.navyblue),
                                        fontWeight = FontWeight.Bold,


                                    )
                                    val totalAmount = expenses.sumOf { it.amount }
                                    Text(
                                        text = "RM%.2f".format(totalAmount),
                                        style = MaterialTheme.typography.bodySmall,
                                        color =  colorResource(R.color.navyblue),
                                        fontWeight = FontWeight.Bold
                                    )

                                }

                            }
                            items(expenses.size) { idx ->
                                ExpenseListItem(expenses[idx],
                                    onClick = {
                                        navController.currentBackStackEntry?.savedStateHandle?.set("editable_expense", expenses[idx])
                                        navController.navigate("edit_expense")
                                    })
                                Divider(
                                    color = Color.White.copy(alpha = 0.2f),
                                    thickness = 0.5.dp,
                                )
                            }
                        }


                    }


                }

            }

            // FAB with modal bottom sheet
            Box(
                modifier = Modifier.fillMaxSize()
                    .padding(bottom = 20.dp),
                contentAlignment = Alignment.BottomCenter
            ) {
                FloatingActionButton(
                    onClick = { showAddOptions = true },
                    containerColor = colorResource(R.color.navyblue),
                    shape = CircleShape,
                    contentColor = colorResource(R.color.lightblue)
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Add Expense")
                }
            }

            if (showAddOptions) {
                ModalBottomSheet(
                    onDismissRequest = { showAddOptions = false },
                    shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
                    tonalElevation = 2.dp,
                    containerColor = Color.White
                ) {
                    Column(
                        modifier = Modifier.padding(vertical = 8.dp)
                    ) {
                        SheetOption("Manual") {
                            showAddOptions = false
                            navController.navigate("add_expense")
                        }

                        SheetOption("Upload QR Pay Statement") {
                            showAddOptions = false
                            navController.navigate("upload_qr")
                        }

                    }
                }
            }
        }
    }

}

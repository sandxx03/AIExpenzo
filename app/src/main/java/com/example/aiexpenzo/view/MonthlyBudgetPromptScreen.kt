package com.example.aiexpenzo.view

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.NavController
import com.example.aiexpenzo.components.PopupPromptInput
import com.example.aiexpenzo.viewmodel.AuthViewModel
import java.util.Calendar

@Composable
fun MonthlyBudgetPromptScreen(
    navController: NavController,
    viewModel: AuthViewModel)
{
    val calendar = Calendar.getInstance()
    val month = calendar.get(Calendar.MONTH)
    val year = calendar.get(Calendar.YEAR)
    var errorMessage by remember { mutableStateOf<String?>(null)}

    PopupPromptInput(
        promptText = "How much do you plan to spend in a month? (monthly budget)",
        promptDesc = "(You can adjust this later.)",
        placeholderText = "0.00",
        errorMessage = errorMessage,
        onConfirm = { budget ->
            if (budget <= 0f) {
                errorMessage = "Please enter a valid amount."
            } else if (!viewModel.isBudgetValid(month, year, budget)) {
                val income = viewModel.getCurrentMonthIncome(month, year)
                errorMessage = "Budget cannot exceed monthly income ($${"%.2f".format(income)})"
            } else {
                errorMessage = null
                viewModel.setMonthlyBudget(month, year, budget)
                navController.navigate("dashboard") {
                    popUpTo("prompt_monthlyIncome") { inclusive = true }
                }
            }
        }
    )

}
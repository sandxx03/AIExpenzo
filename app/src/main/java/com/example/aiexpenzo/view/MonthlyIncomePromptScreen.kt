package com.example.aiexpenzo.view

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.example.aiexpenzo.components.PopupPromptInput
import com.example.aiexpenzo.viewmodel.AuthViewModel
import java.util.Calendar

@Composable
fun MonthlyIncomePromptScreen(navController: NavController, viewModel: AuthViewModel){

    val calendar = Calendar.getInstance()
    val month = calendar.get(Calendar.MONTH)
    val year = calendar.get(Calendar.YEAR)
    PopupPromptInput(
        promptText = "Please enter your monthly income / source of spending. (You can change this later)",
        placeholderText = "0.00",
        onConfirm = {incomeStr ->
           val income = incomeStr.toFloat() ?: 0f
            viewModel.setMonthlyIncome(month, year, income)
            //Navigate to monthly budget prompt screen
            navController.navigate("prompt_monthlyBudget")
        }
    )
}

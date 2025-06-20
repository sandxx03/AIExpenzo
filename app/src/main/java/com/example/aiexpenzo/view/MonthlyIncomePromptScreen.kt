package com.example.aiexpenzo.view

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.example.aiexpenzo.components.PopupPromptInput
import com.example.aiexpenzo.viewmodel.AuthViewModel

@Composable
fun MonthlyIncomePromptScreen(navController: NavController, viewModel: AuthViewModel){
    PopupPromptInput(
        promptText = "Please enter your monthly income / source of spending. (You can change this later)",
        placeholderText = "0.00",
        onConfirm = {incomeStr ->
           val income = incomeStr.toFloat() ?: 0f
            viewModel.setMonthlyIncome(income)
            //Navigate to monthly budget prompt screen
            navController.navigate("prompt_monthlyBudget")
        }
    )
}

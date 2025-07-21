package com.example.aiexpenzo.view

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.example.aiexpenzo.components.PopupPromptInput
import com.example.aiexpenzo.viewmodel.AuthViewModel
import java.util.Calendar

@Composable
fun MonthlyIncomePromptScreen(
    navController: NavController,
    viewModel: AuthViewModel){

    val calendar = Calendar.getInstance()
    val month = calendar.get(Calendar.MONTH)
    val year = calendar.get(Calendar.YEAR)
    PopupPromptInput(
        promptText = "Please enter your monthly income or money source. (e.g. salary, allowance, savings)",
        promptDesc = "(You can update this later)",
        placeholderText = "0.00",
        onConfirm = {income ->
            viewModel.setMonthlyIncome(month, year, income) // calls this function in AuthVM
            //Navigate to monthly budget prompt screen
            navController.navigate("prompt_monthlyBudget")
        }
    )
}

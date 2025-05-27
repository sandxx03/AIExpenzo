package com.example.aiexpenzo.view

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.example.aiexpenzo.components.PopupPromptInput

@Composable
fun MonthlyIncomePromptScreen(navController: NavController){
    PopupPromptInput(
        promptText = "Please enter your monthly income / source of spending. (You can change this later)",
        placeholderText = "0.00",
        onConfirm = {income ->
            // Todo: Save income somewhere (in viewModel, or pass as nav args)
            //Navigate to monthly budget prompt screen
            navController.navigate("prompt_monthlyBudget")
        }
    )
}

package com.example.aiexpenzo.view

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.example.aiexpenzo.components.PopupPromptInput

@Composable
fun MonthlyBudgetPromptScreen(navController: NavController){
    PopupPromptInput(
        promptText = "Please enter the monthly budget goal that you want to achieve. (You can change this later)",
        placeholderText = "0.00",
        onConfirm = {budget ->
            // Todo: Save monthly budget somewhere
            //Navigate to Dashboard and clear prompt screens from backstack
            navController.navigate("dashboard"){
                popUpTo("prompt_monthlyIncome"){inclusive = true}
            }

        }
    )
}
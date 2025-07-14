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
        promptText = "Please enter the monthly budget goal that you want to achieve. (You can change this later)",
        placeholderText = "0.00",
        errorMessage = errorMessage,
        onConfirm = {budgetStr ->
            val budget = budgetStr.toFloat() ?: 0f
            if(viewModel.isBudgetValid(month, year,budget)){
                viewModel.setMonthlyBudget(month, year,budget)
                //Navigate to Dashboard and clear prompt screens from backstack
                navController.navigate("dashboard"){
                    popUpTo("prompt_monthlyIncome"){inclusive = true}
                    launchSingleTop = true
                }

            }else{
                val income = viewModel.currentUser.value?.monthlyIncome ?: 0f
                errorMessage = "Budget cannot exceed monthly income ($${"%.2f".format(income)})"
            }


        }
    )

}
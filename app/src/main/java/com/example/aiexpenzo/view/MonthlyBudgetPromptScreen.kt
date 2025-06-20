package com.example.aiexpenzo.view

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ModifierLocalBeyondBoundsLayout
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.aiexpenzo.components.PopupPromptInput
import com.example.aiexpenzo.viewmodel.AuthViewModel

@Composable
fun MonthlyBudgetPromptScreen(navController: NavController,viewModel: AuthViewModel)
{
    var errorMessage by remember { mutableStateOf<String?>(null)}

    PopupPromptInput(
        promptText = "Please enter the monthly budget goal that you want to achieve. (You can change this later)",
        placeholderText = "0.00",
        errorMessage = errorMessage,
        onConfirm = {budgetStr ->
            val budget = budgetStr.toFloat() ?: 0f
            if(viewModel.isBudgetValid(budget)){
                viewModel.setMonthlyBudget(budget)
                //Navigate to Dashboard and clear prompt screens from backstack
                navController.navigate("dashboard"){
                    popUpTo("prompt_monthlyIncome"){inclusive = true}
                }

            }else{
                val income = viewModel.currentUser.value?.monthlyIncome ?: 0f
                errorMessage = "Budget cannot exceed monthly income (${income.toInt()})"
            }


        }
    )

}
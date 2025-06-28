package com.example.aiexpenzo.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.aiexpenzo.components.BottomNavBarItem
import com.example.aiexpenzo.data.model.Expense
import com.example.aiexpenzo.view.DashboardScreen
import com.example.aiexpenzo.view.EditProfileScreen
import com.example.aiexpenzo.view.ExpenseListScreen
import com.example.aiexpenzo.view.LoginScreen
import com.example.aiexpenzo.view.ManualAddExpenseScreen
import com.example.aiexpenzo.view.MonthlyBudgetPromptScreen
import com.example.aiexpenzo.view.MonthlyIncomePromptScreen
import com.example.aiexpenzo.view.OnboardingScreen
import com.example.aiexpenzo.view.ProfileScreen
import com.example.aiexpenzo.view.SignUpScreen
import com.example.aiexpenzo.viewmodel.AuthViewModel
import com.example.aiexpenzo.viewmodel.ExpenseViewModel


@Composable
fun AppNavHost(
    navController: NavHostController,
    authViewModel: AuthViewModel,
    expenseViewModel: ExpenseViewModel
){


    NavHost(
        navController = navController,
        startDestination = "onboarding"
    ) {

        // Auth Screens
        composable("onboarding") { OnboardingScreen(navController) }
        composable("login") { LoginScreen(navController, authViewModel) }
        composable("signup") { SignUpScreen(navController, authViewModel) }
        composable("prompt_monthlyIncome"){ MonthlyIncomePromptScreen(navController, authViewModel) }
        composable("prompt_monthlyBudget"){ MonthlyBudgetPromptScreen(navController, authViewModel) }


        // Main Screens
        composable(BottomNavBarItem.Home.route) {
            DashboardScreen(navController, authViewModel, expenseViewModel)
        }

        composable(BottomNavBarItem.Expenses.route){
            ExpenseListScreen(navController, expenseViewModel)
        }

        // Navigate to Manual Add Expense Screen
        composable("add_expense"){
            ManualAddExpenseScreen(
                navController = navController,
                viewModel = expenseViewModel,
                onSave = {expense ->
                    expenseViewModel.addExpense(expense)
                    navController.popBackStack() // return to ExpenseListScreen
                }
            )
        }

        // When user click on expense item, navigate to editable screen
        composable("edit_expense"){
            val expense = navController
                .previousBackStackEntry
                ?.savedStateHandle
                ?.get<Expense>("editable_expense")
            expense?.let{
                ManualAddExpenseScreen(
                    navController = navController,
                    viewModel = expenseViewModel,
                    initialExpense = it,
                    onSave = {updatedExpense ->
                        expenseViewModel.updateExpense(updatedExpense)
                        navController.popBackStack()
                    },
                    onDelete = {
                        expenseViewModel.removeExpense(it)
                        navController.popBackStack()
                    }
                )
            }
        }

        composable(BottomNavBarItem.Analyzer.route){}

        // Navigate to Profile & Settings Screen
        composable(BottomNavBarItem.Profile.route){
            ProfileScreen(navController, authViewModel)}

        composable("edit_profile") {
            EditProfileScreen(navController, authViewModel)}


}
}

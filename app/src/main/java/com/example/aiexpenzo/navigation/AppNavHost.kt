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
    NavHost(navController, startDestination = "onboarding") {
        composable("onboarding") { OnboardingScreen(navController) }
        composable("login") { LoginScreen(navController, authViewModel) }
        composable("signup") { SignUpScreen(navController, authViewModel) }
        composable("prompt_monthlyIncome"){ MonthlyIncomePromptScreen(navController, authViewModel) }
        composable("prompt_monthlyBudget"){ MonthlyBudgetPromptScreen(navController, authViewModel) }
        composable(BottomNavBarItem.Home.route) { DashboardScreen(navController, authViewModel, expenseViewModel) }

        // TODO: placeholder for demo
        val mockExpenses = mapOf<String, List<Expense>>()
        composable(BottomNavBarItem.Expenses.route){
            ExpenseListScreen(
                navController = navController,
                viewModel = expenseViewModel,
                expensesByDate = mockExpenses,
                onManualAdd = { navController.navigate("add_expense") },
                onStatementAdd = { /* navigate to statement parsing */ },
                onReceiptAdd = { /* navigate to receipt scan */ }
            )}

        // Navigate to Manual Add Expense Screen
        composable("add_expense"){
            ManualAddExpenseScreen(
                onBack = {navController.popBackStack()},
                onSave = {expense ->
                    expenseViewModel.addExpense(expense)
                    navController.popBackStack()    // return to ExpenseListScreen
                },
                onCancel = {
                    navController.popBackStack()
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
                    initialExpense = it,
                    onBack = {navController.popBackStack()},
                    onCancel = {navController.popBackStack()},
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
            ProfileScreen(
           navController = navController,
                viewModel = authViewModel,
                onLogOut = {
                    authViewModel.logout()
                navController.navigate("login"){
                    popUpTo("dashboard") { inclusive = true }
                }
                    navController
                        .currentBackStackEntry
                        ?.savedStateHandle
                        ?.set("toast_message", "You have been logged out successfully.")

                }
            )}


        composable("edit_profile") {
            EditProfileScreen(navController = navController, viewModel = authViewModel,
                onCancel = {navController.popBackStack()},
                onSave = { newName, newEmail, newPassword ->
                    val validationError = authViewModel.validateProfileInput(newName, newEmail, newPassword)
                    if (validationError != null) {
                        navController
                            .currentBackStackEntry
                            ?.savedStateHandle
                            ?.set("toast_message", validationError)
                        return@EditProfileScreen
                    }

                    val oldUser = authViewModel.currentUser.value
                    authViewModel.updateUsername(newName)
                    authViewModel.updateEmail(newEmail)
                    authViewModel.updatePassword(newPassword)

                    val oldEmail = oldUser?.email ?: ""
                    val oldPassword = oldUser?.password ?: ""

                    if (newEmail != oldEmail || newPassword != oldPassword) {
                        authViewModel.logout()
                        navController.navigate("login") {
                            popUpTo("dashboard") { inclusive = true }
                        }
                        navController
                            .currentBackStackEntry
                            ?.savedStateHandle
                            ?.set("toast_message", "Profile updated. Please log in with your new credentials.")
                    } else {
                        navController.popBackStack()

                    }
                }
            )}
    }
}

package com.example.aiexpenzo.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.aiexpenzo.components.BottomNavBarItem
import com.example.aiexpenzo.data.model.Expense
import com.example.aiexpenzo.view.AIAnalyzerScreen
import com.example.aiexpenzo.view.AppGuideScreen
import com.example.aiexpenzo.view.DashboardScreen
import com.example.aiexpenzo.view.ExpenseListScreen
import com.example.aiexpenzo.view.LoginScreen
import com.example.aiexpenzo.view.ManualAddExpenseScreen
import com.example.aiexpenzo.view.MonthlyBudgetPromptScreen
import com.example.aiexpenzo.view.MonthlyIncomePromptScreen
import com.example.aiexpenzo.view.OnboardingScreen
import com.example.aiexpenzo.view.ProfileScreen
import com.example.aiexpenzo.view.SettingsScreen
import com.example.aiexpenzo.view.SignUpScreen
import com.example.aiexpenzo.view.UpdateProfileScreen
import com.example.aiexpenzo.view.UploadQrPayStatementScreen
import com.example.aiexpenzo.viewmodel.AiAnalyzerViewModel
import com.example.aiexpenzo.viewmodel.AuthViewModel
import com.example.aiexpenzo.viewmodel.ExpenseViewModel
import com.example.aiexpenzo.viewmodel.QRStatementViewModel
import com.example.aiexpenzo.viewmodel.SettingsViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun AppNavHost(
    navController: NavHostController,
    authViewModel: AuthViewModel,
    expenseViewModel: ExpenseViewModel,
    aiAnalyzerViewModel: AiAnalyzerViewModel,
    qrStatementViewModel: QRStatementViewModel,
    settingsViewModel: SettingsViewModel
){


    NavHost(
        navController = navController,
        startDestination = "onboarding",
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
            // injecting values extracted from QR Pay Statement
            val savedState = navController
                .previousBackStackEntry
                ?.savedStateHandle

            LaunchedEffect(Unit) {
                savedState?.apply {
                    remove<String>("parsed_amount")
                    remove<String>("parsed_method")
                    remove<String>("parsed_description")
                    remove<String>("parsed_date")
                }
            }
            val parsedAmount = savedState?.get<String>("parsed_amount")
            val parsedMethod = savedState?.get<String>("parsed_method")
            val parsedDescription = savedState?.get<String>("parsed_description")
            val parsedDate = savedState?.get<String>("parsed_date")

            val parsedDateObj = try {
                parsedDate?.let {
                    SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(it)
                }
            } catch (e: Exception){null}

            ManualAddExpenseScreen(
                navController = navController,
                expenseViewModel = expenseViewModel,
                qrStatementViewModel = qrStatementViewModel,
                initialExpense = if (parsedAmount != null || parsedMethod != null || parsedDescription != null){
                    Expense(
                        amount = parsedAmount?.toDoubleOrNull() ?: 0.0,
                        paymentMethod = parsedMethod ?: "",
                        description = parsedDescription ?: "",
                        transactionDate = parsedDateObj ?: Date()
                    )
                } else null,
                onSave = {expense ->
                    expenseViewModel.addExpense(expense)

                }
            )
        }

        // Navigate to UploadQRPayStatement Screen
        composable("upload_qr"){
            UploadQrPayStatementScreen(navController, qrStatementViewModel)
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
                    expenseViewModel = expenseViewModel,
                    initialExpense = it,
                    onSave = {updatedExpense ->
                        expenseViewModel.updateExpense(updatedExpense)

                    },
                    onDelete = {
                        expenseViewModel.removeExpense(it)
                    }
                )
            }
        }

        composable(BottomNavBarItem.Analyzer.route){
            AIAnalyzerScreen(navController, aiAnalyzerViewModel, expenseViewModel)
        }

        // Navigate to Profile & Settings Screen
        composable(BottomNavBarItem.Profile.route){
            ProfileScreen(navController, authViewModel)}

        composable("update_profile") {
            UpdateProfileScreen(navController, authViewModel)}

        composable("help"){
            AppGuideScreen(navController)
        }
        composable("settings"){
            SettingsScreen(navController, settingsViewModel)
        }


}
}

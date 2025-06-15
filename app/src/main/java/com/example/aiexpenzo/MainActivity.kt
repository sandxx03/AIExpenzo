package com.example.aiexpenzo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.aiexpenzo.data.model.CategorySpend
import com.example.aiexpenzo.ui.theme.AIExpenzoTheme
import com.example.aiexpenzo.view.DashboardScreen
import com.example.aiexpenzo.view.LoginScreen
import com.example.aiexpenzo.view.MonthlyBudgetPromptScreen
import com.example.aiexpenzo.view.MonthlyIncomePromptScreen
import com.example.aiexpenzo.view.OnboardingScreen
import com.example.aiexpenzo.view.Period
import com.example.aiexpenzo.view.SignUpScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {

            AIExpenzoTheme {
                val navController = rememberNavController()
                AppNavigation(navController)
            }

        }
    }
}

@Composable
fun AppNavigation(navController: NavHostController) {
    val sampleCategories = listOf(
        CategorySpend("CatTitle", 0.0f, "00/00/00"),
        CategorySpend("CatTitle", 0.0f, "00/00/00"),
        CategorySpend("CatTitle", 0.0f, "00/00/00")
    )
    NavHost(navController, startDestination = "onboarding") {
        composable("onboarding") { OnboardingScreen(navController)}
        composable("dashboard") {DashboardScreen(
            monthlyIncome = 10000f,
            moneyOut = 2000f,
            monthlyBudget = 3000f,
            currentUsed = 2000f,
            currentPeriod = Period.Monthly,
            topCategories = sampleCategories,
            onCategoryPeriodChange = { /* handle tab change */ }
        )}
        composable("login") { LoginScreen(navController)}
        composable("signup") { SignUpScreen(navController)}
        composable("prompt_monthlyIncome"){ MonthlyIncomePromptScreen(navController)}
        composable("prompt_monthlyBudget"){ MonthlyBudgetPromptScreen(navController) }
    }
}
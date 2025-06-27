package com.example.aiexpenzo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.aiexpenzo.navigation.AppNavHost
import com.example.aiexpenzo.ui.theme.AIExpenzoTheme
import com.example.aiexpenzo.viewmodel.AuthViewModel
import com.example.aiexpenzo.viewmodel.ExpenseViewModel
import com.google.firebase.FirebaseApp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)

        enableEdgeToEdge()

        setContent {

            AIExpenzoTheme {
                val navController = rememberNavController()
                val authViewModel: AuthViewModel = viewModel()
                val expenseViewModel: ExpenseViewModel = viewModel()
                AppNavHost(navController, authViewModel, expenseViewModel)
            }

        }
    }
}

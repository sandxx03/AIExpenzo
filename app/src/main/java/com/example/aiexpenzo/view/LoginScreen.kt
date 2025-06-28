package com.example.aiexpenzo.view


import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.aiexpenzo.R
import com.example.aiexpenzo.viewmodel.AuthViewModel
import java.util.Calendar

@Composable
fun LoginScreen(navController: NavController, viewModel: AuthViewModel){
    var email by remember { mutableStateOf("")}
    var password by remember { mutableStateOf("")}
    val context = LocalContext.current

    val authSuccess by viewModel.authSuccess.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    Column(modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp)
            .statusBarsPadding(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            Row(verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
                    .padding(bottom=40.dp)){
                IconButton(onClick = {navController.navigate("onboarding")}) {
                    Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back")
                }
                Text(text="Login", fontSize = 20.sp, fontWeight = FontWeight.Bold, color= colorResource(R.color.navyblue),
                    modifier = Modifier.padding(start=4.dp))

            }

            Spacer(modifier = Modifier.height(32.dp))
            Text("Login to your account.", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = colorResource(R.color.navyblue))

            Spacer(modifier = Modifier.height(24.dp))



        OutlinedTextField(
            value = email,
            onValueChange = {email = it},
            label = {Text("Email")},
            shape = RoundedCornerShape(20.dp),
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = colorResource(R.color.navyblue),
                unfocusedIndicatorColor = colorResource(R.color.lightblue)
            )
        )


            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = password,
                onValueChange = {password = it},
                label = {Text("Password")},
                shape = RoundedCornerShape(20.dp),
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth(),
                colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = colorResource(R.color.navyblue),
                    unfocusedIndicatorColor = colorResource(R.color.lightblue)
                )


            )
            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Forgot Password?",
                color = colorResource(R.color.navyblue),
                fontSize = 12.sp,
                modifier = Modifier.align(Alignment.End).clickable {  }
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(onClick = { viewModel.login(email, password)},
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = colorResource(R.color.lightblue),
                    contentColor = colorResource(R.color.navyblue)
                ) ){
                Text("LOGIN", color = colorResource(R.color.navyblue), fontWeight = FontWeight.Bold)
            }



            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text="Don't have an account yet? Sign Up", textDecoration = TextDecoration.Underline,
                color = colorResource(R.color.navyblue),
                modifier = Modifier.clickable { navController.navigate("signup") }
            )

        }

    if (isLoading){
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Gray.copy(alpha = 0.3f)),
            contentAlignment = Alignment.Center
        ){
            CircularProgressIndicator(color = colorResource(R.color.navyblue))
        }
    }

    LaunchedEffect (authSuccess){
        if (authSuccess){
            Toast.makeText(context, "Login successful", Toast.LENGTH_SHORT).show()

            val user = viewModel.currentUser.value
            if (user != null){
                val calendar = Calendar.getInstance()
                val month = calendar.get(Calendar.MONTH)
                val year = calendar.get(Calendar.YEAR)
                val monthYearKey = "$month-$year"
                // Check if current month's values are set
                val hasIncome = user.monthlyIncome[monthYearKey] != null
                val hasBudget = user.monthlyBudget[monthYearKey] != null

                if (!hasIncome || !hasBudget){
                    // Navigate to income prompt if either is missing
                    navController.navigate("prompt_monthlyIncome"){
                        popUpTo("login"){ inclusive = true}
                    }
                } else{
                    navController.navigate("dashboard"){
                        popUpTo("login") { inclusive = true }
                    }
                }

            }

            viewModel.resetAuthStatus()
        }
    }

    LaunchedEffect(errorMessage) {
        errorMessage?.let{
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            viewModel.clearError()
        }
    }

}



/*
@Preview(showBackground=true)
@Composable
fun LoginScreenPreview(){
    AIExpenzoTheme {
        LoginScreen()
    }

}

 */
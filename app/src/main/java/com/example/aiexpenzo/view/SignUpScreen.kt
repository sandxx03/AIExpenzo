package com.example.aiexpenzo.view

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.aiexpenzo.R
import com.example.aiexpenzo.viewmodel.AuthViewModel

@Composable
fun SignUpScreen(navController: NavController, viewModel: AuthViewModel){
    var name by remember{ mutableStateOf("")}
    var email by remember { mutableStateOf("") }

    var password by remember { mutableStateOf("") }
    val context = LocalContext.current


    val authSuccess by viewModel.authSuccess.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()

    Column(modifier = Modifier.fillMaxSize()
        .padding(horizontal = 24.dp)
        .statusBarsPadding(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            Row(verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
                    .padding(bottom=40.dp)){

                // Back Button
                IconButton(onClick = {navController.navigate("onboarding")}) {
                    Icon(imageVector = Icons.AutoMirrored.Default.ArrowBack,
                        contentDescription = "Back")
                }

                // Screen Header - Sign Up
                Text(text="Sign Up", fontSize = 20.sp, fontWeight = FontWeight.Bold, color= colorResource(R.color.navyblue),
                    modifier = Modifier.padding(start=4.dp))

            }
            Spacer(modifier = Modifier.height(32.dp))


            // Instructions
            Text("Create your account.", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = colorResource(R.color.navyblue))

            Spacer(modifier = Modifier.height(24.dp))

            // User Input Field - Name
            OutlinedTextField(value=name,
                onValueChange = {name = it},
                label = {Text("Name")},
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier.fillMaxWidth(),
                colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = colorResource(R.color.navyblue),
                    unfocusedIndicatorColor = colorResource(R.color.lightblue)
                )
            )
            Spacer(modifier = Modifier.height(12.dp))

            // User Input Field - Email
            OutlinedTextField(value = email,
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

            // User Input Field - Password
            OutlinedTextField(
                value = password,
                onValueChange = {password = it},
                label = {Text("Password")},
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier.fillMaxWidth(),
                colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = colorResource(R.color.navyblue),
                    unfocusedIndicatorColor = colorResource(R.color.lightblue)
                )


            )
            Spacer(modifier = Modifier.height(24.dp))

            // Sign Up Button
            Button(onClick = {viewModel.signUp(name, email, password)},
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = colorResource(R.color.navyblue),
                    contentColor = colorResource(R.color.white)
                ) ){
                Text("SIGN UP", color = colorResource(R.color.white), fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(12.dp))

            // More options (If user already has a account)
            Text(
                text="Already have an account? Login", textDecoration = TextDecoration.Underline,
                color = colorResource(R.color.navyblue),
                modifier = Modifier.clickable { navController.navigate("login") }
            )

        }
    LaunchedEffect (authSuccess) {
        if (authSuccess){
            Toast.makeText(context, "Registered Successfully!", Toast.LENGTH_SHORT).show()
            navController.navigate("login"){
                popUpTo("signup"){inclusive = true}
            }
            viewModel.resetAuthStatus()
        }
    }

    LaunchedEffect (errorMessage){
        errorMessage?.let{
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            viewModel.clearError()
        }
    }

}


/*
@Preview(showBackground=true)
@Composable
fun SignUpScreenPreview(){
    AIExpenzoTheme {
        SignUpScreen()
    }
}

*/
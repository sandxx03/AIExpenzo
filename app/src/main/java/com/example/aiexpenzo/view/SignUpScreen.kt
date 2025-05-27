package com.example.aiexpenzo.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.aiexpenzo.R
import com.example.aiexpenzo.viewmodel.AuthViewModel
import android.widget.Toast
import androidx.compose.ui.platform.LocalContext

@Composable
fun SignUpScreen(navController: NavController){
    var name by remember{ mutableStateOf("")}
    var email by remember { mutableStateOf("") }

    var password by remember { mutableStateOf("") }
    val viewModel: AuthViewModel = viewModel() // access the AuthViewModel for logic
    val authSuccess by viewModel.authSuccess.collectAsState()   //collectAsState() - let the Compose react to state changes
    val errorMessage by viewModel.errorMessage.collectAsState()
    val context = LocalContext.current


    Column(modifier = Modifier.fillMaxSize().padding(horizontal = 24.dp).padding(top=70.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            Row(verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
                    .padding(bottom=40.dp)){
                IconButton(onClick = {navController.navigate("onboarding")}) {
                    Icon(imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back")
                }
                Text(text="Sign Up", fontSize = 20.sp, fontWeight = FontWeight.Bold, color= colorResource(R.color.navyblue),
                    modifier = Modifier.padding(start=4.dp))

            }
            Spacer(modifier = Modifier.height(32.dp))

            Text("Create your account.", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = colorResource(R.color.navyblue))

            Spacer(modifier = Modifier.height(24.dp))

            OutlinedTextField(value=name,
                onValueChange = {name = it},
                label = {Text("Name")},
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier.fillMaxWidth(),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = colorResource(R.color.navyblue),
                    unfocusedBorderColor = colorResource(R.color.lightblue)

                )
            )
            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(value = email,
                onValueChange = {email = it},
                label = {Text("Email")},
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier.fillMaxWidth(),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = colorResource(R.color.navyblue),
                    unfocusedBorderColor = colorResource(R.color.lightblue)
                )
            )
            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = password,
                onValueChange = {password = it},
                label = {Text("Password")},
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier.fillMaxWidth(),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = colorResource(R.color.navyblue),
                    unfocusedBorderColor = colorResource(R.color.lightblue))


            )
            Spacer(modifier = Modifier.height(24.dp))
            Button(onClick = {
                viewModel.signUp(name, email, password) },
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = colorResource(R.color.navyblue),
                    contentColor = colorResource(R.color.white)
                ) ){
                Text("SIGN UP", color = colorResource(R.color.white), fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text="Already have an account? Login", textDecoration = TextDecoration.Underline,
                color = colorResource(R.color.navyblue),
                modifier = Modifier.clickable { navController.navigate("login") }
            )

        }

    LaunchedEffect(authSuccess) {   //Reacts to changes and execute only once per change
        if(authSuccess) {
            Toast.makeText(context,"Registered Succesful!", Toast.LENGTH_SHORT).show()
            navController.navigate("login")
            viewModel.resetAuthStatus()
        }
    }
    LaunchedEffect(errorMessage){
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

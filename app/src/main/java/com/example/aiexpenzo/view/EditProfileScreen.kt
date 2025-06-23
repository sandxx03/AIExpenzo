package com.example.aiexpenzo.view

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.aiexpenzo.R
import com.example.aiexpenzo.components.BottomNavBar
import com.example.aiexpenzo.viewmodel.AuthViewModel

@Composable
fun EditProfileScreen(
    navController: NavController,
    viewModel: AuthViewModel,
    onSave: (String, String, String) -> Unit,
    onCancel:() -> Unit,
){

    val user by viewModel.currentUser.collectAsState()
    var name by remember { mutableStateOf(user?.name ?: "") }
    var email by remember { mutableStateOf(user?.email ?: "") }
    var password by remember { mutableStateOf(user?.password ?: "") }
    val context = LocalContext.current


    Scaffold (
        bottomBar = { BottomNavBar(navController) }
    ){ innerPadding ->

        Column (
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 15.dp)
        ){
            Spacer(modifier = Modifier.height(8.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                // Back button and header
                IconButton(onClick = {onCancel()}) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back")
                }
                Spacer(Modifier.width((6.dp)))

                Text(
                    text = "Edit Profile",
                    fontWeight = FontWeight.Bold,
                    fontSize = 22.sp,
                    color = colorResource(R.color.navyblue)
                )

            }
            Spacer(modifier = Modifier.height(36.dp))

            // Editable profile fields
            Column (
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(13.dp)
            ) {
                OutlinedTextField(
                    value = name,
                    onValueChange = {name = it},
                    label = {Text("Username")},
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = email,
                    onValueChange = {email = it},
                    label = {Text("Email")},
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = password,
                    onValueChange = {password = it},
                    label = {Text("Password")},
                    modifier = Modifier.fillMaxWidth()
                )


            }
            Spacer(modifier = Modifier.height(50.dp))

            // Buttons row
            Row(
                modifier = Modifier.fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .padding(bottom = 30.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {


                Button(
                    onClick = {onCancel()},
                    colors = ButtonDefaults.buttonColors(containerColor = colorResource(R.color.lightblue)),
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(10.dp),
                    elevation = ButtonDefaults.buttonElevation(
                        defaultElevation = 8.dp,
                        pressedElevation = 12.dp,
                        focusedElevation = 4.dp
                    ),
                ) {
                    Text(text = "CANCEL", fontWeight = FontWeight.Bold, color = colorResource(R.color.navyblue))
                }
                Spacer(Modifier.width(20.dp))

                Button(
                    onClick = {onSave(name, email, password)},
                    colors = ButtonDefaults.buttonColors(containerColor = colorResource(R.color.navyblue)),
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(10.dp),
                    elevation = ButtonDefaults.buttonElevation(
                        defaultElevation = 8.dp,
                        pressedElevation = 12.dp,
                        focusedElevation = 4.dp
                    )
                ){
                    Text(text = "SAVE CHANGES", fontWeight = FontWeight.Bold)
                }
            }



        }


    }


}

/*
@Preview(showBackground = true)
@Composable
fun EditProfileScreenPreview(){
    val navController = rememberNavController()
    val viewModel = AuthViewModel()
    EditProfileScreen(navController, viewModel)

}

 */
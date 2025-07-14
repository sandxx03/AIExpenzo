package com.example.aiexpenzo.view

import android.widget.Toast
import androidx.compose.foundation.background
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
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.aiexpenzo.R
import com.example.aiexpenzo.components.AppTopBar
import com.example.aiexpenzo.components.BottomNavBar
import com.example.aiexpenzo.viewmodel.AuthViewModel

@Composable
fun UpdateProfileScreen(
    navController: NavController,
    viewModel: AuthViewModel,
){

    val user by viewModel.currentUser.collectAsState()
    var name by remember { mutableStateOf(user?.name ?: "") }
    var email by remember { mutableStateOf(user?.email ?: "") }
    var newPassword by remember { mutableStateOf("") }
    var confirmNewPassword by remember { mutableStateOf("") }
    var showPasswordDialog by remember { mutableStateOf(false) }
    var currentPasswordInDialog by remember { mutableStateOf("") }

    val context = LocalContext.current

    if (showPasswordDialog) {
        AlertDialog(
            onDismissRequest = { showPasswordDialog = false },
            title = { Text("Verify Password") },
            text = {
                Column {
                    Text("Please enter your current password to confirm changes")
                    OutlinedTextField(
                        value = currentPasswordInDialog,
                        onValueChange = { currentPasswordInDialog = it },
                        label = { Text("Current Password") },
                        visualTransformation = PasswordVisualTransformation(),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = colorResource(R.color.navyblue)),
                    onClick = {
                        viewModel.updateProfile(
                            newName = name,
                            newEmail = email,
                            newPassword = newPassword,
                            currentPassword = currentPasswordInDialog,
                            onSuccess = {
                                Toast.makeText(context, "Profile updated successfully", Toast.LENGTH_SHORT).show()
                                navController.popBackStack()
                            },
                            onError = { error ->
                                Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
                            }
                        )
                        showPasswordDialog = false
                    }
                ) {
                    Text(text="Confirm",
                        color = Color.White)
                }
            },
            dismissButton = {
                Button(
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = colorResource(R.color.lightblue)),
                    onClick = { showPasswordDialog = false }
                ) {
                    Text(
                        text="Cancel",
                        color = Color.Black)
                }
            }
        )
    }

    Scaffold (
        topBar = { AppTopBar(title = "Update Profile") },
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
                IconButton(onClick = {navController.popBackStack()}) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back")
                }
                Spacer(Modifier.width((6.dp)))

                Text(
                    text = "Update Profile",
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
                    value = newPassword,
                    onValueChange = { newPassword = it },
                    label = { Text("New Password (leave blank to keep current)") },
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = confirmNewPassword,
                    onValueChange = { confirmNewPassword = it },
                    label = { Text("Confirm New Password") },
                    visualTransformation = PasswordVisualTransformation(),
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
                    onClick = {navController.popBackStack()},
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
                    onClick = {
                        if (newPassword.isNotEmpty() && newPassword != confirmNewPassword) {
                            Toast.makeText(context, "New passwords don't match", Toast.LENGTH_SHORT)
                                .show()
                        } else {
                            showPasswordDialog = true
                        }
                    },
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


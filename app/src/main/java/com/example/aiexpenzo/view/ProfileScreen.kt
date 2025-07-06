package com.example.aiexpenzo.view

import android.widget.Space
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.aiexpenzo.R
import com.example.aiexpenzo.components.AppTopBar
import com.example.aiexpenzo.components.BottomNavBar
import com.example.aiexpenzo.viewmodel.AuthViewModel

@Composable
fun ProfileScreen(
    navController: NavController,
    viewModel: AuthViewModel
){

    val user by viewModel.currentUser.collectAsState()

    Scaffold (
        topBar = { AppTopBar() },
        bottomBar = { BottomNavBar(navController) }
    ){ innerPadding ->

        Column (
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 15.dp)
        ){
            Spacer(modifier = Modifier.height(8.dp))

            // Header
            Text(
                text = "Profile & Settings",
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp,
                color = colorResource(R.color.navyblue),
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp, bottom = 8.dp)

            )
            Spacer(modifier = Modifier.height(60.dp))
            Column (
                modifier = Modifier
                    .padding(vertical = 15.dp)
                    .weight(1f),
                verticalArrangement = Arrangement.spacedBy(13.dp)
            ) {
                //Edit profile button
                Button(
                    onClick = {navController.navigate("edit_profile")},
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = colorResource(R.color.lightblue)),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Row (
                        horizontalArrangement = Arrangement.Start,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.weight(1f)
                    ){
                        Image(
                            painter = painterResource(R.drawable.ic_profilesettings),
                            contentDescription = "Edit Profile",
                            modifier = Modifier.size(30.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Edit Profile",
                            color= colorResource(R.color.navyblue),
                            fontSize = 18.sp)
                    }


                }
                //TODO
                Button(
                    onClick = {navController.navigate("settings")},
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = colorResource(R.color.lightblue)),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Row (
                        horizontalArrangement = Arrangement.Start,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.weight(1f)
                    ){
                        Image(
                            painter = painterResource(R.drawable.ic_settings),
                            contentDescription = "Settings",
                            modifier = Modifier.size(30.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Settings",
                            color= colorResource(R.color.navyblue),
                            fontSize = 18.sp)
                    }
                }
                //TODO
                Button(
                    onClick = {navController.navigate("help")},
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = colorResource(R.color.lightblue)),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Row (
                        horizontalArrangement = Arrangement.Start,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.weight(1f)
                    ){
                        Image(
                            painter = painterResource(R.drawable.ic_guidehelp),
                            contentDescription = "Help",
                            modifier = Modifier.size(30.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Help",
                            color= colorResource(R.color.navyblue),
                            fontSize = 18.sp)
                    }
                }

                Button(
                    onClick = {
                        viewModel.logout()
                              navController.navigate("login"){
                                  popUpTo(0)
                              }
                      },
                    colors = ButtonDefaults.buttonColors(containerColor = colorResource(R.color.darkred)),
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(top = 50.dp)
                ) {
                    Text("LOGOUT", color = Color.White)
                }

            }



        }


    }


}

/*
@Preview(showBackground = true)
@Composable
fun ProfileScreenPreview(){
    val navController = rememberNavController()
    ProfileScreen(navController)

}

 */
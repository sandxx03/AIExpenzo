package com.example.aiexpenzo.view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.aiexpenzo.R
import com.example.aiexpenzo.components.AppTopBar
import com.example.aiexpenzo.components.BottomNavBar
import com.example.aiexpenzo.util.RequestNotificationPermission
import com.example.aiexpenzo.util.cancelDailyReminder
import com.example.aiexpenzo.util.scheduleDailyReminder
import com.example.aiexpenzo.viewmodel.SettingsViewModel


@Composable
fun SettingsScreen(
    navController: NavController,
    viewModel: SettingsViewModel

){
    val context = LocalContext.current
    val reminderEnabled by viewModel.reminderEnabled.collectAsState()

    RequestNotificationPermission()

    Scaffold (
        topBar = { AppTopBar(title = "Settings") },
        bottomBar = { BottomNavBar(navController) }
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 15.dp)
        ) {
            Spacer(modifier = Modifier.height(8.dp))

            Row(verticalAlignment = Alignment.CenterVertically
            ) {
                // Back button and header
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back"
                    )
                }
                Spacer(Modifier.width((6.dp)))

                Text(
                    text = "Back to Profile",
                    fontWeight = FontWeight.Bold,
                    fontSize = 22.sp,
                    color = colorResource(R.color.navyblue)
                )

            }
            Spacer(modifier = Modifier.height(36.dp))
            Card(
                modifier = Modifier.fillMaxWidth()
                    .padding(vertical = 6.dp),
                elevation = CardDefaults.cardElevation(4.dp),
                colors = CardDefaults.cardColors(
                    containerColor = colorResource(R.color.lightblue)
                )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(
                            text = "Reminder Notifications",
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = colorResource(id = R.color.navyblue),
                            modifier = Modifier.weight(1f)
                        )

                        Switch(
                            checked = reminderEnabled,
                            onCheckedChange = { enabled ->
                                viewModel.setReminder(enabled)
                                if (enabled) {
                                    scheduleDailyReminder(context)
                                } else {
                                    cancelDailyReminder(context)
                                }

                            },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = Color.LightGray,
                                checkedTrackColor = colorResource(id = R.color.navyblue),
                                uncheckedThumbColor = Color.LightGray,
                                uncheckedTrackColor = Color.Gray
                            )
                        )

                    }
                }
            }
        }
    }

}

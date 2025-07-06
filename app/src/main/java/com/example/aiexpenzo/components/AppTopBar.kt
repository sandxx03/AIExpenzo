package com.example.aiexpenzo.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.aiexpenzo.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTopBar(){
    TopAppBar(
        title ={
            Row (
                verticalAlignment = Alignment.CenterVertically,
            ){
                Image(
                    painter = painterResource(R.drawable.logo),
                    contentDescription = "App Logo",
                    modifier = Modifier.size(35.dp)
                        .padding(end = 8.dp)
                )
                Text(
                    text = "AIExpenzo",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = colorResource(R.color.navyblue)
                )

            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = colorResource(R.color.lightblue)
        ),
        modifier = Modifier
            .shadow(6.dp)
    )
}

@Preview (showBackground = true)
@ExperimentalMaterial3Api
@Composable
fun AppTopBarPreview(){
    AppTopBar()
}

package com.example.aiexpenzo.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.aiexpenzo.R

@Composable
fun AiResponseCard(
    header: String,
    body: String,
    modifier: Modifier = Modifier
){
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        shape = MaterialTheme.shapes.medium,
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Column (
            modifier = Modifier.padding(16.dp)
        ){
           Text(
               text = header,
               fontSize = 18.sp,
               color = colorResource(R.color.navyblue),
               style = MaterialTheme.typography.titleSmall
           )

            Spacer(Modifier.height(8.dp))
            Text(
                text = body,
                fontSize = 14.sp,
                color = colorResource(R.color.black),
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Justify
            )

        }
    }
}

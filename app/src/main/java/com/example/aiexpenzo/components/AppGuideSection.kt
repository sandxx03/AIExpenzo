package com.example.aiexpenzo.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.aiexpenzo.R
import com.example.aiexpenzo.data.model.AppGuideContent

@Composable
fun AppGuideSection(
    sec: AppGuideContent,
    isExpanded: Boolean,
    onCardClick: () -> Unit
) {

    Column(
        modifier = Modifier.padding(bottom = 18.dp)
    ) {
        Card(
            modifier = Modifier.fillMaxWidth()
                .padding(vertical = 6.dp)
                .clickable { onCardClick()},
            elevation = CardDefaults.cardElevation(4.dp),
            colors = CardDefaults.cardColors(
                containerColor = colorResource(R.color.lightblue)
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {

                Row (
                    verticalAlignment = Alignment.CenterVertically,
                ){
                    Text(
                        text = sec.title,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = colorResource(id = R.color.navyblue),
                        modifier = Modifier.weight(1f)
                    )

                    Icon(
                        Icons.Default.ArrowDropDown,
                        contentDescription = "Dropdown Arrow",

                        )
                }

                AnimatedVisibility(visible = isExpanded) {
                    Column {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = sec.description,
                            fontSize = 14.sp,
                            lineHeight = 20.sp
                        )
                    }
                }

            }
        }
    }
}
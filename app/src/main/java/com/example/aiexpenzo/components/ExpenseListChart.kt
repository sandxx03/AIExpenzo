package com.example.aiexpenzo.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.aiexpenzo.ui.theme.AIExpenzoTheme

@Composable
fun ExpenseListChart(){
    Box(
        modifier = Modifier.fillMaxWidth()
            .height(150.dp)
            .background(Color.White),
        contentAlignment = Alignment.Center
    ){
        Box(
            Modifier
                .size(100.dp)
                .background(
                    Color(0xFF22395D),
                    shape = RoundedCornerShape(10.dp)
                )
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ExpenseLogChartPreview(){
    AIExpenzoTheme {
        ExpenseListChart()




    }
}
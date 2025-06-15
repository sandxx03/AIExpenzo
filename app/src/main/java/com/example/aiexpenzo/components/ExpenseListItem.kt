package com.example.aiexpenzo.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.aiexpenzo.R
import com.example.aiexpenzo.data.model.Expense
import com.example.aiexpenzo.ui.theme.AIExpenzoTheme

@Composable
fun ExpenseListItem(expense: Expense){
    Row(
        modifier = Modifier.fillMaxWidth()
            .background(colorResource(R.color.lightblue))
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ){
        Row(modifier = Modifier.weight(1f), verticalAlignment = Alignment.CenterVertically) {
            Box(
                Modifier
                    .size(40.dp)
                    .background(
                        Color(0xFF22395D),
                        shape = RoundedCornerShape(10.dp)
                    )
            )
            Spacer(Modifier.width(8.dp))
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    expense.description, style = MaterialTheme.typography.bodyLarge
                )
                Text(expense.category, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold)

            }
        }


        Spacer(Modifier.width(8.dp))
        Column(
            horizontalAlignment = Alignment.End
        ){
            Text(expense.paymentMethod, style = MaterialTheme.typography.labelMedium)

            Text(text = String.format("$%.2f", expense.amount),
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold)
        }
    }
}

@Preview(showBackground=true)
@Composable
fun ExpenseListItemPreview(){
    AIExpenzoTheme {
        val mockExpense = Expense(
            description = "Lunch at Campus",
            category = "Food",
            paymentMethod = "Credit Card",
            amount = 23.50
        )
        ExpenseListItem(expense = mockExpense)



    }
}
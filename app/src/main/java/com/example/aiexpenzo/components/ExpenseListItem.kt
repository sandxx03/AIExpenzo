package com.example.aiexpenzo.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.aiexpenzo.R
import com.example.aiexpenzo.data.constants.CategoryIconMap
import com.example.aiexpenzo.data.model.Expense

@Composable
fun ExpenseListItem(
    expense: Expense,
    onClick:() -> Unit,
                    )
{

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(colorResource(R.color.lightblue).copy(alpha = 0.3f))
            .padding(horizontal = 16.dp, vertical = 12.dp)
            .clickable { onClick() }
    ){

        Row(modifier = Modifier.weight(1f), verticalAlignment = Alignment.CenterVertically)
        {
            val iconRes = CategoryIconMap[expense.category] ?: R.drawable.ic_other

            Image(
                painter = painterResource(id = iconRes),
                contentDescription = expense.category,
                modifier = Modifier.size(40.dp)
                    .background(Color.Transparent, shape = RoundedCornerShape(10.dp)),
                colorFilter = ColorFilter.tint(colorResource(R.color.navyblue))
            )

            Spacer(Modifier.width(12.dp))
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    expense.description, style = MaterialTheme.typography.bodyLarge,
                    color = colorResource(R.color.navyblue)
                )
                Text(expense.category,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    color = colorResource(R.color.navyblue))

            }
        }


        Spacer(Modifier.width(8.dp))
        Column(
            horizontalAlignment = Alignment.End
        ){
            Text(expense.paymentMethod,
                style = MaterialTheme.typography.labelMedium,
                color = colorResource(R.color.navyblue)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = String.format("RM%.2f", expense.amount),
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold,
                color = colorResource(R.color.navyblue)
            )

        }
    }
}

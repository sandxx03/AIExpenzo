package com.example.aiexpenzo.util

import android.app.DatePickerDialog
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import com.example.aiexpenzo.R


@Composable
fun DatePickerField(
    dateMillis: Long,
    onDateChange: (Long) -> Unit,
    modifier: Modifier = Modifier
){
    val context = LocalContext.current
    val dateFormat = remember{ SimpleDateFormat("dd MMM yyyy, EEEE", Locale.getDefault())}
    val dateString = remember(dateMillis){dateFormat.format(Date(dateMillis))}
    val calendar = Calendar.getInstance().apply{timeInMillis = dateMillis}
    var showDialog by remember { mutableStateOf(false) }

    if (showDialog){
        DatePickerDialog(
            context,
            { _, year, month, dayOfMonth ->
                calendar.set(year, month, dayOfMonth)
                onDateChange(calendar.timeInMillis)
                showDialog = false
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)

        ).show()
    }

    OutlinedTextField(
        value = dateString,
        onValueChange = {},
        enabled = false,
        modifier = modifier.fillMaxWidth()
            .clickable {showDialog = true },
        trailingIcon = {
            Icon(
                painter = painterResource(id = R.drawable.calendaricon),
                contentDescription = "Choose Date",
                modifier = Modifier.clickable { showDialog = true }
                    .size(30.dp)
            )

        }
    )
}

@Preview(showBackground = true)
@Composable
fun DatePickerFieldPreview() {

    var dateMillis by remember { mutableStateOf(System.currentTimeMillis()) }

    DatePickerField(
        dateMillis = dateMillis,
        onDateChange = { dateMillis = it }
    )
}
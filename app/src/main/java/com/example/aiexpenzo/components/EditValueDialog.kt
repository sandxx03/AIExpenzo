package com.example.aiexpenzo.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType

@Composable
fun EditValueDialog(
    title: String,
    currentValue: Float,
    onConfirm: (Float) -> Unit,
    onDismiss: () -> Unit,
    validate: (Float) -> Boolean = { true },
    errorMessage: String = "Invalid value"
){
    var input by remember { mutableStateOf(currentValue.toString()) }
    var showError by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = {
                val parsed = input.toFloatOrNull()
                if (parsed != null && validate(parsed)){
                    onConfirm(parsed)
                    onDismiss()
                }else{
                    showError = true
                }
            }) {
                Text("SAVE CHANGES")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("CANCEL")
            }
        },
        title = {
            Text(text = title, fontWeight = FontWeight.Bold)
        },
        text = {
            Column {
                OutlinedTextField(
                    value = input,
                    onValueChange = {input = it},
                    label = { Text("Enter value")},
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
                if(showError) {
                    Text(
                        text = errorMessage,
                        color = Color.Red,
                        fontWeight = FontWeight.SemiBold
                    )

                }

            }

        }
    )
}
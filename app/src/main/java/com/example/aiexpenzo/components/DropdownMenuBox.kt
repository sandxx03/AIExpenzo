package com.example.aiexpenzo.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun DropdownMenuBox(
    options: List<String>,
    selectedOption:String,
    onOptionSelected: (String) -> Unit,
    modifier: Modifier
){
    var expanded by remember { mutableStateOf(false) }

    Box{
        OutlinedTextField(
            value = selectedOption,
            onValueChange = {},
            readOnly = true,
            modifier = modifier,
            trailingIcon = {
                Icon(
                    Icons.Default.ArrowDropDown,
                    contentDescription = "Dropdown Arrow",
                    modifier = Modifier.clickable { expanded = true })
            }

        )

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = {expanded = false}
        ) {
            options.forEach{ option ->
                DropdownMenuItem(
                    text = {Text(option)},
                    onClick = {
                        onOptionSelected(option)
                        expanded = false
                    }

                )
            }
        }

    }
}

@Preview(showBackground = true)
@Composable
fun DropdownMenuBoxPreview() {
    // Local state for the selected option (needed for Preview interaction)
    var selected by remember { mutableStateOf("Option 1") }
    val options = listOf("Option 1", "Option 2", "Option 3", "Option 4")

    DropdownMenuBox(
        options = options,
        selectedOption = selected,
        onOptionSelected = { selected = it },
        modifier = Modifier.fillMaxWidth()
    )
}
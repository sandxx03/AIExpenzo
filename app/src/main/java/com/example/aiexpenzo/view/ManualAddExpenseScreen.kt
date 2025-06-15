package com.example.aiexpenzo.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.aiexpenzo.R
import com.example.aiexpenzo.components.DropdownMenuBox
import com.example.aiexpenzo.data.model.Expense
import com.example.aiexpenzo.util.DatePickerField


@Composable
fun ManualAddExpenseScreen(
    initialExpense: Expense? = null,
    onBack:() -> Unit = {},
    onSave: (Expense) -> Unit,
    onCancel: () -> Unit
){
    // State holders
    var amount by remember { mutableStateOf(initialExpense?.amount?.toString() ?: "")}
    var dateMillis by remember{ mutableStateOf(System.currentTimeMillis())}
    var category by remember { mutableStateOf(initialExpense?.category ?: "") }
    var paymentMethod by remember { mutableStateOf(initialExpense?.paymentMethod ?: "") }
    var description by remember { mutableStateOf(initialExpense?.description ?: "") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)

    ){
        Row (
            modifier = Modifier.fillMaxWidth()
                .statusBarsPadding()
                .height(70.dp),
            verticalAlignment = Alignment.CenterVertically
        ){
            IconButton(onClick = onBack) {
                Icon(imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back")
            }
            Spacer(Modifier.width((6.dp)))
            Text("Add Expenses", fontWeight = FontWeight.Bold, fontSize = 22.sp, color = colorResource(
                R.color.navyblue)
            )
        }

        // Amount section with colored background

        Box(
            modifier = Modifier.fillMaxWidth()
                .background(colorResource(R.color.lightblue))
                .padding(vertical = 50.dp)
        ){
            Column (
                modifier = Modifier.padding(start = 32.dp)
            ){
                Text(
                    text = "EXPENSE AMOUNT",
                    color = Color.Gray,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium
                )
                Spacer(Modifier.height(16.dp))
                Row (
                    verticalAlignment = Alignment.CenterVertically
                ){
                    Text(
                        text = "$",
                        fontSize = 38.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black,
                        modifier = Modifier.padding(bottom = 6.dp)

                    )
                    Spacer(Modifier.width(8.dp))
                    OutlinedTextField(
                        value = amount,
                        onValueChange = {amount = it},
                        placeholder = { Text("0.00", fontSize = 38.sp, color = Color.LightGray) },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color.Transparent,
                            unfocusedBorderColor = Color.Transparent,
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent
                        ),
                        textStyle = LocalTextStyle.current.copy(
                            fontSize = 38.sp,
                            fontWeight = FontWeight.Bold
                        ),
                        modifier = Modifier.width(140.dp)

                    )
                }
            }
        }

        // Card section for details

        Column (
            modifier = Modifier.offset(y = (-32).dp)
                .fillMaxWidth()
                .clip(RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp))
                .background(Color.White)
                .padding(horizontal = 20.dp, vertical = 32.dp)
        ){
            // Transaction Date
            Text("TRANSACTION DATE", fontWeight = FontWeight.Bold, color = Color.Black, fontSize = 14.sp)
            Spacer(Modifier.height(4.dp))
            DatePickerField(
                dateMillis = dateMillis,
                onDateChange = {dateMillis = it},
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(18.dp))

            // Category
            Text("CATEGORY", fontWeight = FontWeight.Bold, color = Color.Black, fontSize = 14.sp)
            Spacer(Modifier.height(4.dp))

            Row (verticalAlignment = Alignment.CenterVertically){
                DropdownMenuBox(
                    options = listOf("Housing", "Utilities", "Subscriptions (Media & Software)", "Food", "Transportation", "Health & Insurance", "Personal Care", "Education & Learning", "Entertainment & Leisure", "Shopping", "Gifts & Donations", "Pets", "Family & Children", "Debt Repayment", "Savings & Investments", "Other"),
                    selectedOption = category,
                    onOptionSelected = {category = it},
                    modifier = Modifier.weight(1f)

                )
                Spacer(Modifier.width(10.dp))
                Button(
                    onClick = {/*TODO: Add New Category dialog*/},
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF222222)),
                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp),
                    modifier = Modifier.height(44.dp)
                ){
                    Text("+", fontSize = 22.sp, color = Color.White)
                }
            }

            Spacer(Modifier.height(18.dp))
            // Payment Method
            Text("PAYMENT METHOD", fontWeight = FontWeight.Bold, color = Color.Black, fontSize = 14.sp)
            Spacer(Modifier.height(4.dp))
            DropdownMenuBox(
                options = listOf("Cash", "Credit Card", "Debit Card", "e-Wallet", "Cheque"),
                selectedOption = paymentMethod,
                onOptionSelected = {paymentMethod = it},
                modifier = Modifier.fillMaxWidth()

            )
            Spacer(Modifier.height(18.dp))

            // Description/Remarks
            Text("DESCRIPTION / REMARKS", fontWeight = FontWeight.Bold, color = Color.Black, fontSize = 14.sp)
            Spacer(Modifier.height(4.dp))
            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                placeholder = { Text("Description/Remarks....") },
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedContainerColor = Color(0xFFF4F4F4),
                    focusedContainerColor = Color(0xFFF4F4F4),
                    unfocusedBorderColor = Color.LightGray,
                    focusedBorderColor = Color.LightGray
                ),
                maxLines = 3
            )
        }

        Spacer(Modifier.height(32.dp))
        // Buttons row
        Row(
            modifier = Modifier.fillMaxWidth()
                .padding(horizontal = 20.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(
                onClick = onCancel,
                colors = ButtonDefaults.buttonColors(containerColor = colorResource(R.color.darkred)),
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(10.dp),
                elevation = ButtonDefaults.buttonElevation(
                    defaultElevation = 8.dp,
                    pressedElevation = 12.dp,
                    focusedElevation = 4.dp
                ),
            ) {
                Text(text = "CANCEL", fontWeight = FontWeight.Bold)
            }
            Spacer(Modifier.width(18.dp))
            Button(
                onClick = {
                    val amt = amount.toDoubleOrNull()?: 0.0
                    onSave(
                        Expense(
                            description = description,
                            category = category,
                            paymentMethod = paymentMethod,
                            amount = amt
                        )
                    )
                },
                colors = ButtonDefaults.buttonColors(containerColor = colorResource(R.color.navyblue)),
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(10.dp),
                elevation = ButtonDefaults.buttonElevation(
                    defaultElevation = 8.dp,
                    pressedElevation = 12.dp,
                    focusedElevation = 4.dp
                )
            ){
                Text(text = "SAVE", fontWeight = FontWeight.Bold)
            }
        }

    }
}

@Preview(showBackground = true)
@Composable
fun ManualAddExpensePreview_Empty() {
    ManualAddExpenseScreen(
        initialExpense = null,
        onSave = {},
        onCancel = {}
    )
}
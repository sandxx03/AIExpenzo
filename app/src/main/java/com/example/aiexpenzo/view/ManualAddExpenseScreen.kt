package com.example.aiexpenzo.view

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.AlertDialog
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.aiexpenzo.R
import com.example.aiexpenzo.components.DropdownMenuBox
import com.example.aiexpenzo.data.constants.EXPENSE_CATEGORIES
import com.example.aiexpenzo.data.constants.PAYMENT_METHODS
import com.example.aiexpenzo.data.model.Expense
import com.example.aiexpenzo.util.DatePickerField
import java.util.Date


@Composable
fun ManualAddExpenseScreen(
    initialExpense: Expense? = null,
    onBack:() -> Unit = {},
    onSave: (Expense) -> Unit,
    onCancel: () -> Unit,
    onDelete: ((Expense) -> Unit)? = null
){
    var showDeleteDialog by remember { mutableStateOf(false) }
    val isEditing = initialExpense != null
    // State holders
    var amount by remember { mutableStateOf(initialExpense?.amount?.toString() ?: "")}
    var dateMillis by remember{ mutableStateOf(initialExpense?.transactionDate?.time ?: System.currentTimeMillis())}
    var transactionDate by remember { mutableStateOf(Date(dateMillis)) }
    var category by remember { mutableStateOf(initialExpense?.category ?: "") }
    var paymentMethod by remember { mutableStateOf(initialExpense?.paymentMethod ?: "") }
    var description by remember { mutableStateOf(initialExpense?.description ?: "") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .verticalScroll(rememberScrollState())

    ){
        Row (
            modifier = Modifier.fillMaxWidth()
                .statusBarsPadding()
                .height(70.dp)
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ){
            Row(verticalAlignment = Alignment.CenterVertically) {
                // Back button and header
                IconButton(onClick = onBack) {
                    Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back")
                }
                Spacer(Modifier.width((6.dp)))

                Text(
                    text = if (isEditing) "Edit Expense" else "Add Expense",
                    fontWeight = FontWeight.Bold,
                    fontSize = 22.sp,
                    color = colorResource(R.color.navyblue)
                )

            }
            // Delete button when editing expenses
            if (isEditing && onDelete != null){
                Text(
                    text = "Delete",
                    color = colorResource(R.color.darkred),
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.clickable {
                        showDeleteDialog = true
                    }
                        .padding(8.dp)
                )
            }

        }

        // Show delete dialog if user click on delete button
        if (showDeleteDialog && initialExpense != null){
            AlertDialog(
                onDismissRequest = {showDeleteDialog = false},
                title = { Text("Delete Expense?")},
                text = { Text("Are you sure you want to delete this expense?")},

                confirmButton = {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Button(
                            onClick = {
                                showDeleteDialog = false
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = colorResource(R.color.navyblue)
                            ),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Cancel", color = Color.White)
                        }
                        Spacer(modifier = Modifier.width(16.dp))
                        Button(
                            onClick = {
                                showDeleteDialog = false
                                onDelete?.invoke(initialExpense)
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = colorResource(R.color.darkred)
                            ),
                            modifier = Modifier.weight(1f)

                        ) {
                            Text("Delete", color = Color.White)
                        }
                    }

                }

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
                        onValueChange = {
                            if (it.matches(Regex("^\\d*\\.?\\d{0,2}$"))){
                                amount = it}
                                        },
                        placeholder = { Text("0.00", fontSize = 38.sp, fontWeight = FontWeight.Bold,color = Color.LightGray) },
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
                        modifier = Modifier.fillMaxWidth()

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
                    options = EXPENSE_CATEGORIES,
                    selectedOption = category,
                    onOptionSelected = {category = it},
                    modifier = Modifier.weight(1f)

                )
                Spacer(Modifier.width(10.dp))

            }

            Spacer(Modifier.height(18.dp))
            // Payment Method
            Text("PAYMENT METHOD", fontWeight = FontWeight.Bold, color = Color.Black, fontSize = 14.sp)
            Spacer(Modifier.height(4.dp))
            DropdownMenuBox(
                options = PAYMENT_METHODS,
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
                onValueChange = {
                    if (it.length <=120){    // character limit
                        description = it
                    }
                },
                placeholder = { Text("Description/Remarks....") },
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedContainerColor = Color(0xFFF4F4F4),
                    focusedContainerColor = Color(0xFFF4F4F4),
                    unfocusedBorderColor = Color.LightGray,
                    focusedBorderColor = Color.LightGray
                ),
                maxLines = 2
            )
        }

        Spacer(Modifier.height(25.dp))
        // Buttons row
        Row(
            modifier = Modifier.fillMaxWidth()
                .padding(horizontal = 20.dp)
                .padding(bottom = 30.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {


            Button(
                onClick = onCancel,
                colors = ButtonDefaults.buttonColors(containerColor = colorResource(R.color.lightblue)),
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(10.dp),
                elevation = ButtonDefaults.buttonElevation(
                    defaultElevation = 8.dp,
                    pressedElevation = 12.dp,
                    focusedElevation = 4.dp
                ),
            ) {
                Text(text = "CANCEL", fontWeight = FontWeight.Bold, color = colorResource(R.color.navyblue))
            }
            Spacer(Modifier.width(18.dp))

            val isSaveEnabled = amount.isNotBlank() && category.isNotBlank()
            val context = LocalContext.current
            Button(
                onClick = {
                    val amt = amount.toDoubleOrNull()?: 0.0

                    if (category.isBlank()){
                        //Show a toast error message
                        Toast.makeText(context, "Please select a category!", Toast.LENGTH_SHORT).show()
                        return@Button

                    }
                    onSave(
                        Expense(
                            id = initialExpense?.id ?: System.currentTimeMillis(),  // important for viewModdel to match during update
                            description = description,
                            category = category,
                            paymentMethod = paymentMethod,
                            amount = amt,
                            transactionDate = transactionDate,


                        )
                    )
                },
                enabled = isSaveEnabled,
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

/*
@Preview(showBackground = true)
@Composable
fun ManualAddExpensePreview_Empty() {
    ManualAddExpenseScreen(
        initialExpense = null,
        onSave = {},
        onCancel = {}
    )
}

 */
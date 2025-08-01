package com.example.aiexpenzo.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.aiexpenzo.R

@Composable
fun PopupPromptInput(
    promptText: String,
    promptDesc: String,
    placeholderText: String,
    errorMessage: String? = null,
    onConfirm:(Float) -> Unit,

){
    var inputText by remember { mutableStateOf("") }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .fillMaxWidth(),
        verticalArrangement = Arrangement.Center

    ) {
        Column(modifier = Modifier
            .padding(24.dp)
            .background(colorResource(R.color.lightblue), RoundedCornerShape(20.dp)),
            horizontalAlignment = Alignment.CenterHorizontally,


        ){
            Text(text = promptText,
                fontSize = 18.sp,
                modifier = Modifier
                    .padding(horizontal = 24.dp)
                    .padding(top = 14.dp),
                fontWeight = FontWeight.Bold)

            Text(text = promptDesc,
                fontSize = 15.sp,
                modifier = Modifier
                    .padding(vertical = 5.dp),
                textAlign = TextAlign.Start)
            Spacer(modifier = Modifier.height(5.dp))

            TextField(
                value = inputText,
                onValueChange = {input ->
                    if (input.matches(Regex("^(\\d+(\\.\\d{2})?)?\$"))){
                        // accept input that are empty strings, whole numbers, with 2 decimal places, 0
                        //digits from 0-9
                        inputText = input
                    }
                },
                placeholder = {Text(placeholderText)},
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
                    .padding(horizontal = 24.dp)
                ,
                shape = RoundedCornerShape(15.dp),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    disabledContainerColor = Color.White
                )

            )

            // error message
            errorMessage?.let {
                Text(
                    text = it,
                    color = Color.Red,
                    fontSize = 12.sp,
                    modifier = Modifier
                        .align(Alignment.Start)
                        .padding(start = 24.dp, top = 4.dp)
                )
            }

            Spacer(modifier = Modifier.height(30.dp))
            Button(
                onClick = {
                    val value = inputText.toFloatOrNull()
                    if (value != null && value > 0f){
                        onConfirm(value)
                    } else{
                        // error message
                    }
                },
                modifier = Modifier.fillMaxWidth(0.5f)
                    .padding(bottom = 24.dp).padding(top = 24.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = colorResource(R.color.navyblue),
                    contentColor = colorResource(R.color.lightblue)
                ),
                elevation = ButtonDefaults.buttonElevation(
                    defaultElevation = 8.dp,
                    pressedElevation = 12.dp,
                    focusedElevation = 4.dp
                ),
                shape = RoundedCornerShape(10.dp)

            ) {
                Text("CONFIRM", fontWeight = FontWeight.Bold)
            }

        }

    }

}
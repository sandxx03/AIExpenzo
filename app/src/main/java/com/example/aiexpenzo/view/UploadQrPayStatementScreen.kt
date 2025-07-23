package com.example.aiexpenzo.view

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.example.aiexpenzo.R
import com.example.aiexpenzo.components.BottomNavBarItem
import com.example.aiexpenzo.util.extractTextFromImage
import com.example.aiexpenzo.viewmodel.QRStatementViewModel
import kotlinx.coroutines.launch

@Composable
fun UploadQrPayStatementScreen(
    navController: NavController,
    viewModel: QRStatementViewModel

){
    val isLoading by viewModel.isLoading
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    var selectedImageUri by remember { mutableStateOf<Uri?>(null)}
    // image picker
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        selectedImageUri = uri
    }

    Box(modifier = Modifier
        .fillMaxSize()

    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .verticalScroll(rememberScrollState())

        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .height(70.dp)
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    // Back button and header
                    IconButton(onClick = {navController.popBackStack()}) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                    Spacer(Modifier.width((6.dp)))

                    Text(
                        text = "Add Expense",
                        fontWeight = FontWeight.Bold,
                        fontSize = 22.sp,
                        color = colorResource(R.color.navyblue)
                    )

                }
            }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Automatically log expense with an image of your receipt or screenshots of your e-Wallet/QR Pay transaction statements.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = colorResource(R.color.navyblue)

                )
                Spacer(modifier = Modifier.height(35.dp))

                Column (
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(colorResource(R.color.navyblue), RoundedCornerShape(12.dp))
                        .padding(vertical = 16.dp, horizontal = 16.dp),
                ){

                    Text(
                        text="INSTRUCTIONS",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text="1. Screenshot your transaction statement in your e-Wallet/QR Pay applications OR\n" +
                                "snap an image of your receipt",
                        style = MaterialTheme.typography.bodyLarge,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text="2. Upload your image below.",
                        style = MaterialTheme.typography.bodyLarge,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text="3. Click 'EXTRACT'.",
                        style = MaterialTheme.typography.bodyLarge,
                        color = Color.White
                    )

                }
                Spacer(modifier = Modifier.height(20.dp))
                Text(
                    text = "UPLOAD RECEIPT/TRANSACTION STATEMENT",
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold,
                    color = colorResource(R.color.navyblue)
                )
                Spacer(modifier = Modifier.height(20.dp))
                Column (
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.LightGray.copy(alpha = 0.2f), RoundedCornerShape(12.dp))
                        .padding(16.dp)
                        .clickable { imagePickerLauncher.launch("image/*") },
                    horizontalAlignment = Alignment.CenterHorizontally
                ){
                    if (selectedImageUri != null){
                        AsyncImage(
                            model = selectedImageUri,
                            contentDescription = "Selected image preview",
                            modifier = Modifier.fillMaxWidth()
                                .height(200.dp)
                        )
                    } else{
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.ic_upload),
                                contentDescription = "Select image",
                                tint = Color.DarkGray.copy(alpha=0.4f),
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Tap to select image",
                                color = Color.DarkGray.copy(alpha=0.4f)
                            )
                        }
                    }


                }

            }
            Spacer(modifier = Modifier.height(40.dp))
            // Buttons row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .padding(bottom = 30.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {


                Button(
                    onClick = {navController.popBackStack(BottomNavBarItem.Expenses.route, inclusive = false) },
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
                Spacer(Modifier.width(50.dp))
                Button(
                    onClick = {
                        selectedImageUri?.let { uri ->
                            coroutineScope.launch {
                                val text = extractTextFromImage(context, uri)
                                if (text.isBlank() || text.length < 10){
                                    Toast.makeText(
                                        context,
                                        "Image is not clear or not extractable!",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                } else {
                                    viewModel.extract(text)
                                }

                            }
                        }

                    },
                    enabled = selectedImageUri != null,
                    colors = ButtonDefaults.buttonColors(containerColor = colorResource(R.color.navyblue)),
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(10.dp),
                    elevation = ButtonDefaults.buttonElevation(
                        defaultElevation = 8.dp,
                        pressedElevation = 12.dp,
                        focusedElevation = 4.dp
                    )
                ){
                    Text(text = "EXTRACT", fontWeight = FontWeight.Bold)
                }
            }


        }
        if (isLoading){
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Gray.copy(alpha = 0.3f)),
                contentAlignment = Alignment.Center
            ){
                CircularProgressIndicator(color = colorResource(R.color.navyblue))
            }
        }
    }
    val parsed = viewModel.parsedExpense.value
    // runs once
    LaunchedEffect(Unit) {
        viewModel.clearParsedData()
    }
    // runs when parsed changes (after successful extraction)
    LaunchedEffect (parsed){
        parsed?.let{
            navController
                .currentBackStackEntry
                ?.savedStateHandle
                ?.apply { 
                    set("parsed_amount", it.amount?.toString())
                    set("parsed_method", it.payment_method ?: "")
                    set("parsed_description", it.description ?: "")
                    set("parsed_date", it.transaction_date ?: "")
                }
            navController.navigate("add_expense")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun UploadScreenPreview(){
    UploadQrPayStatementScreen(
        navController = rememberNavController(),
        viewModel = QRStatementViewModel()
    )
}
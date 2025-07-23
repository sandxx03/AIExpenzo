package com.example.aiexpenzo.util

import android.content.Context
import android.net.Uri
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import kotlinx.coroutines.tasks.await

suspend fun extractTextFromImage(context: Context, imageUri: Uri): String{
    val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)   // initialize the recognizer
    val inputImage = InputImage.fromFilePath(context, imageUri) // Converts URI into an ML Kit compatible image
    val result = recognizer.process(inputImage).await() // returns raw extracted text
    return result.text

}
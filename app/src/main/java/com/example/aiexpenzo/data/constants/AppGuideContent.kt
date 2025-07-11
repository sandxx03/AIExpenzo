package com.example.aiexpenzo.data.constants

import com.example.aiexpenzo.data.model.AppGuideContent

val AppGuideContentList = listOf(

    AppGuideContent(
        "Monthly Budget & Income Setup",
        "Set a budget and income so that you can track when you're overspending or doing well!"
    ),
    AppGuideContent(
        "Adding Your Expense",
        "You can add expenses manually or with the QR Pay Parser. Tap the “+” on the Home screen and select a method."
    ),
    AppGuideContent(
        "Using the QR Pay Statement Parser",
        "Screenshot your payment transaction from your eWallet and upload it in AIExpenzo.\n\n" +
                "AIExpenzo will auto-detect the transaction amount, merchant, and payment method.\n\n" +
                "Keep in mind that the AIExpenzo may not be able to extract some payment transactions due to the display format of the details."
    ),
    AppGuideContent(
        "AI-Powered Expense Analysis",
        "The AI gives you personalized insights and saving tips based on your patterns.\n\n" +
                "Users are suggested to log expenses for at least 7 days to get more valuable and accurate insights."
    )

)

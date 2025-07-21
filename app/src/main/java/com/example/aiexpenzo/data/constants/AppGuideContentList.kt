package com.example.aiexpenzo.data.constants

import com.example.aiexpenzo.data.model.AppGuideContent

val AppGuideContentList = listOf(

    AppGuideContent(
        "Monthly Budget & Income Setup",
        "Start by setting your monthly income and target budget in AIExpenzo. This helps track your financial health automatically.\n\n" +
                "Your total expenses will be compared against your budget throughout the month, allowing you to monitor overspending in real-time.\n\n" +
                "Regularly updating your income or budget ensures more accurate financial tracking and recommendations."
    ),
    AppGuideContent(
        "Adding Your Expenses",
        "AIExpenzo lets you log expenses manually or using QR Pay Parser.\n\n" +
                "To add expenses manually:\n" +
                "- Tap the “+” button on the Home screen.\n" +
                "- Enter the amount, category, and optional notes.\n\n" +
                "Using QR Pay Parser:\n" +
                "- Tap the “+” button and select 'Scan QR Pay Statement'.\n" +
                "- Upload a screenshot of your transaction from your eWallet app.\n" +
                "- AIExpenzo will automatically extract key details like amount, merchant name, and payment method.\n\n" +
                "Note: If auto-detection fails due to unusual formats, you can edit the extracted details manually."
    ),
    AppGuideContent(
        "Using the Receipt/QR Pay Statement Parser",
        "Streamline expense tracking by using the Receipt/QR Pay Statement Parser:\n\n" +
                "- Take a clear screenshot of your eWallet payment confirmation OR take a clear image of your receipt.\n" +
                "- Upload the image directly into AIExpenzo.\n\n" +
                "AIExpenzo's parser uses AI to auto-detect:\n" +
                "- Transaction amount\n" +
                "- Merchant name\n" +
                "- Payment method\n\n" +
                "Important:\n" +
                "- For best results, ensure the image clearly displays the transaction details.\n" +
                "- Some statements may not parse correctly due to complex or unusual display formats. Always review parsed data before saving."
    ),
    AppGuideContent(
        "AI-Powered Expense Analysis",
        "AIExpenzo analyzes your spending patterns using AI and offers personalized insights, such as:\n\n" +
                "- Daily, weekly, and monthly spending trends.\n" +
                "- Alerts when you're nearing your budget limit.\n" +
                "- Tailored saving tips based on your actual expenses.\n\n" +
                "For meaningful insights, log expenses consistently over at least 7 days.\n\n" +
                "The more data you provide, the smarter and more accurate your AI-generated recommendations will become."
    )

)
